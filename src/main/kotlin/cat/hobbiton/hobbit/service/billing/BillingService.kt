package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO

interface BillingService {

	fun getConsumptions(childCode: Int?): List<YearMonthConsumptionsDTO>
}
