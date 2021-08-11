package cat.hobbiton.hobbit.service.generate.pdf

import org.springframework.core.io.Resource

const val pdfsZipFilename = "pdfs.zip"

interface PdfService {

    fun generatePDFs(yearMonth: String, notYetPrinted: Boolean): Resource

    fun generatePDF(invoiceId: String): Resource

}
