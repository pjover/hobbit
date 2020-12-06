package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO

interface GenerateService {

	fun generateSepa(): org.springframework.core.io.Resource

	fun simulateSepa(): List<PaymentTypeInvoicesDTO>
}
