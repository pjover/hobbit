package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.ProductRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.model.extension.getChild
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.util.AppException
import cat.hobbiton.hobbit.util.translate
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeParseException

@Service
class BillingServiceImpl(
        private val consumptionRepository: ConsumptionRepository,
        private val customerRepository: CustomerRepository,
        private val productRepository: ProductRepository,
        private val timeService: TimeService
) : BillingService {

    override fun getChildConsumptions(childCode: Int): List<YearMonthConsumptionsDTO> {
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

    private fun getGrossAmount(consumptions: List<Consumption>): Double {
        return consumptions
                .map { getGrossAmount(it) }
                .sumOf { it }
                .toDouble()
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

    private fun groupYearMonth(consumptions: List<Consumption>): List<ChildConsumtionDTO> {
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
        return getChild(childCode).shortName()
    }

    @Cacheable("children")
    fun getChild(code: Int): Child {
        val customer = customerRepository.findByChildCode(code)
                ?: throw AppException(ErrorMessages.ERROR_CHILD_NOT_FOUND, code)
        return customer.getChild(code) ?: throw AppException(ErrorMessages.ERROR_CHILD_NOT_FOUND, code)
    }

    override fun getConsumptions(): List<YearMonthConsumptionsDTO> {
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

    override fun getLastMonthConsumptions(): List<SetYearMonthConsumptionsDTO> {
        return getConsumptions()
                .filter { isLastMonth(it.yearMonth) }
                .map {
                    SetYearMonthConsumptionsDTO(
                            yearMonth = it.yearMonth,
                            children = it.children.map { child ->
                                SetChildConsumtionDTO(
                                        code = child.code,
                                        consumptions = child.consumptions.map { consumption ->
                                            SetConsumtionDTO(
                                                    productId = consumption.productId,
                                                    units = consumption.units,
                                                    note = consumption.note
                                            )
                                        }
                                )
                            }
                    )
                }
    }

    private fun isLastMonth(yearMonth: String) = getYearMonth(yearMonth) == getLastMonth()

    private fun getLastMonth() = timeService.currentYearMonth.minusMonths(1)


    override fun setConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO): List<YearMonthConsumptionsDTO> {
        saveConsumptions(setYearMonthConsumptionsDTO)
        return getConsumptions()
    }

    private fun saveConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO) {
        val yearMonth = getYearMonth(setYearMonthConsumptionsDTO.yearMonth)
        setYearMonthConsumptionsDTO.children.forEach { saveChildConsumtions(yearMonth, it) }
    }

    private fun getYearMonth(yearMonth: String): YearMonth {
        return try {
            YearMonth.parse(yearMonth)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException(ValidationMessages.ERROR_YEAR_MONTH_INVALID.translate(), e)
        }
    }

    private fun saveChildConsumtions(yearMonth: YearMonth, childConsumtionDTO: SetChildConsumtionDTO) {
        childConsumtionDTO.consumptions.forEach { saveChildConsumtion(yearMonth, childConsumtionDTO.code, it) }
    }

    private fun saveChildConsumtion(yearMonth: YearMonth, childCode: Int, consumtionDTO: SetConsumtionDTO) {
        consumptionRepository.save(Consumption(
                childCode = childCode,
                productId = consumtionDTO.productId,
                units = BigDecimal.valueOf(consumtionDTO.units),
                yearMonth = yearMonth,
                note = consumtionDTO.note
        ))
    }
}
