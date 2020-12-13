package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource


interface PdfService {

	fun simulatePDFs(yearMonth: String): PaymentTypeInvoicesDTO

	fun generatePDFs(yearMonth: String): Resource

	fun generatePDF(invoiceId: String): Resource

}
