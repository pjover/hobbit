package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.SetYearMonthConsumptionsDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
interface BillingService {

	fun getChildConsumptions(childCode: kotlin.Int): List<YearMonthConsumptionsDTO>

	fun getConsumptions(): List<YearMonthConsumptionsDTO>

	fun getLastMonthConsumptions(): List<SetYearMonthConsumptionsDTO>

	fun setConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO): List<YearMonthConsumptionsDTO>
}
