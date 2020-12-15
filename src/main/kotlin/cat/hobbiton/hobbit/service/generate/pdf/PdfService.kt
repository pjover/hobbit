package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource

const val pdfsZipFilename = "pdfs.zip"

interface PdfService {

	fun simulatePDFs(yearMonth: String): List<PaymentTypeInvoicesDTO>

	fun generatePDFs(yearMonth: String): Resource

	fun generatePDF(invoiceId: String): Resource

}
