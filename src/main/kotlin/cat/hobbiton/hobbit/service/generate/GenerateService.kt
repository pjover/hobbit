package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
interface GenerateService {

	fun generateSepa(yearMonth: String?): org.springframework.core.io.Resource

	fun simulateSepa(yearMonth: String?): PaymentTypeInvoicesDTO
}
