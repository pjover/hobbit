package cat.hobbiton.hobbit.service.consumptions

import cat.hobbiton.hobbit.api.model.SetYearMonthConsumptionsDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
interface ConsumptionsService {

	fun getChildConsumptions(childCode: Int): List<YearMonthConsumptionsDTO>

	fun getConsumptions(): List<YearMonthConsumptionsDTO>

	fun getLastMonthConsumptions(): SetYearMonthConsumptionsDTO

	fun setConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO): List<YearMonthConsumptionsDTO>
}
