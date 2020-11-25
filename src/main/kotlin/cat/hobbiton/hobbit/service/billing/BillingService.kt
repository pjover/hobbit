package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.SetYearMonthConsumptionsDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
interface BillingService {

	fun getConsumptions(childCode: kotlin.Int?): List<YearMonthConsumptionsDTO>

	fun setConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO): List<YearMonthConsumptionsDTO>
}
