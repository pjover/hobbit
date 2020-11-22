package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.ChildConsumtionDTO
import cat.hobbiton.hobbit.api.model.ConsumtionDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.extension.getChild
import cat.hobbiton.hobbit.model.extension.shortName
import org.springframework.stereotype.Service

@Service
class BillingServiceImpl(
        private val consumptionRepository: ConsumptionRepository,
        private val customerRepository: CustomerRepository
) : BillingService {

    override fun getConsumptions(childCode: Int?): List<YearMonthConsumptionsDTO> {

        return if (childCode == null) getAllChildrenConsumptions()
        else getChildConsumptions(childCode)
    }

    private fun getAllChildrenConsumptions(): List<YearMonthConsumptionsDTO> {

        return consumptionRepository.findByInvoicedOnNull()
                .groupBy { it.yearMonth }
                .map { (yearMonth, consumptions) ->
                    YearMonthConsumptionsDTO(yearMonth = yearMonth.toString(), groupYearMonth(consumptions))
                }
    }

    private fun groupYearMonth(consumptions: List<Consumption>): List<ChildConsumtionDTO>? {
        return consumptions
                .groupBy { it.childCode }
                .map { (childCode, consumptions) -> sumConsumptions(childCode, consumptions) }
                .map {
                    ChildConsumtionDTO(
                            code = it.first,
                            shortName = getChildrenShortName(it.first),
                            consumptions = it.second.map { c -> ConsumtionDTO(c.productId, c.units.toDouble(), c.note) }
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
        return customerRepository.findByChildCode(childCode)?.getChild(childCode)?.shortName() ?: ""
    }

    private fun getChildConsumptions(childCode: Int): List<YearMonthConsumptionsDTO> {
        TODO("Not yet implemented")
    }
}