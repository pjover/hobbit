package cat.hobbiton.hobbit.service.consumptions

import cat.hobbiton.hobbit.api.model.SetYearMonthConsumptionsDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO

interface ConsumptionsService {

	fun getChildConsumptions(childCode: kotlin.Int): List<YearMonthConsumptionsDTO>

	fun getConsumptions(): List<YearMonthConsumptionsDTO>

	fun getLastMonthConsumptions(): List<SetYearMonthConsumptionsDTO>

	fun setConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO): List<YearMonthConsumptionsDTO>
}
