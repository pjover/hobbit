package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.ChildConsumtionDTO
import cat.hobbiton.hobbit.api.model.ConsumtionDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.ProductRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.model.extension.getChild
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.util.AppException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BillingServiceImpl(
        private val consumptionRepository: ConsumptionRepository,
        private val customerRepository: CustomerRepository,
        private val productRepository: ProductRepository
) : BillingService {

    override fun getConsumptions(childCode: Int?): List<YearMonthConsumptionsDTO> {
        return if (childCode == null) getAllChildrenConsumptions()
        else getChildConsumptions(childCode)
    }

    private fun getAllChildrenConsumptions(): List<YearMonthConsumptionsDTO> {
        return consumptionRepository.findByInvoicedOnNull()
                .groupBy { it.yearMonth }
                .map { (yearMonth, consumptions) ->
                    YearMonthConsumptionsDTO(
                            yearMonth.toString(),
                            getGrossAmount(consumptions),
                            groupYearMonth(consumptions)
                    )
                }
    }

    private fun getGrossAmount(consumptions: List<Consumption>): Double {
        return consumptions
                .map { getGrossAmount(it) }
                .sumOf { it }
                .toDouble()
    }

    private fun groupYearMonth(consumptions: List<Consumption>): List<ChildConsumtionDTO>? {
        return consumptions
                .groupBy { it.childCode }
                .map { (childCode, consumptions) -> sumConsumptions(childCode, consumptions) }
                .map {
                    ChildConsumtionDTO(
                            code = it.first,
                            shortName = getChildrenShortName(it.first),
                            grossAmount = getGrossAmount(it.second),
                            consumptions = it.second.map { c ->
                                ConsumtionDTO(
                                        c.productId,
                                        c.units.toDouble(),
                                        getGrossAmount(c).toDouble(),
                                        c.note
                                )
                            }
                    )
                }
    }

    private fun getGrossAmount(consumption: Consumption): BigDecimal {
        val product = getProduct(consumption.productId)
        return product.price.multiply(consumption.units)
    }

    @Cacheable("products")
    fun getProduct(id: String): Product {
        return productRepository.findById(id)
                .orElseThrow { AppException(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, id) }
    }

    private fun sumConsumptions(childCode: Int, consumptions: List<Consumption>): Pair<Int, List<Consumption>> {
        return Pair(
                childCode,
                consumptions
                        .groupBy { it.productId }
                        .map { (productId, it) ->
                            Consumption(
                                    childCode = childCode,
                                    productId = productId,
                                    units = it.sumOf { it.units },
                                    yearMonth = it.first().yearMonth,
                                    note = it.map { it.note }.joinToString(separator = ", ")
                            )
                        }
        )
    }

    private fun getChildrenShortName(childCode: Int): String {
        return getChild(childCode)?.getChild(childCode)?.shortName() ?: ""
    }

    @Cacheable("children")
    fun getChild(code: Int) = customerRepository.findByChildCode(code)

    private fun getChildConsumptions(childCode: Int): List<YearMonthConsumptionsDTO> {
        return consumptionRepository.findByInvoicedOnNullAndChildCode(childCode)
                .groupBy { it.yearMonth }
                .map { (yearMonth, consumptions) ->
                    YearMonthConsumptionsDTO(
                            yearMonth.toString(),
                            getGrossAmount(consumptions),
                            groupYearMonth(consumptions)
                    )
                }
    }
}