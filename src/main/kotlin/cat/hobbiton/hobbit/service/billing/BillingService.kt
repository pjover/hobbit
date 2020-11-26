package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.SetChildConsumtionDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
interface BillingService {

	fun getChildConsumptions(childCode: kotlin.Int): List<YearMonthConsumptionsDTO>

	fun getConsumptions(): List<YearMonthConsumptionsDTO>

	fun getMonthConsumptions(yearMonth: kotlin.String?): List<SetChildConsumtionDTO>

	fun setMonthConsumptions(setChildConsumtionDTO: SetChildConsumtionDTO, yearMonth: kotlin.String?): List<YearMonthConsumptionsDTO>
}
