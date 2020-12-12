package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
interface GenerateService {

	fun generateBDD(yearMonth: kotlin.String?): org.springframework.core.io.Resource

	fun generatePDF(invoiceId: kotlin.String): org.springframework.core.io.Resource

	fun generatePDFs(yearMonth: kotlin.String?): org.springframework.core.io.Resource

	fun simulateBDD(yearMonth: kotlin.String?): PaymentTypeInvoicesDTO

	fun simulatePDFs(yearMonth: kotlin.String?): PaymentTypeInvoicesDTO
}
