package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource


interface GenerateService {

	fun simulateBDD(yearMonth: String?): PaymentTypeInvoicesDTO

	fun generateBDD(yearMonth: String): Resource

	fun simulatePDFs(yearMonth: String): List<PaymentTypeInvoicesDTO>

	fun generatePDFs(yearMonth: String): Resource

	fun generatePDF(invoiceId: String): Resource

	fun simulateEmails(yearMonth: String): PaymentTypeInvoicesDTO

	fun generateEmails(yearMonth: String): PaymentTypeInvoicesDTO

	fun simulateMonthReport(yearMonth: String): List<PaymentTypeInvoicesDTO>

	fun generateMonthReport(yearMonth: String): Resource
}
