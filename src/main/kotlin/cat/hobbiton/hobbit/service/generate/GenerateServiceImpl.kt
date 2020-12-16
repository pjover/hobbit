package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import cat.hobbiton.hobbit.service.generate.pdf.PdfService
import org.springframework.stereotype.Service

@Service
class GenerateServiceImpl(
    private val bddService: BddService,
    private val pdfService: PdfService
) : GenerateService {

    override fun simulateBDD(yearMonth: String?) = bddService.simulateBDD(yearMonth)

    override fun generateBDD(yearMonth: String) = bddService.generateBDD(yearMonth)

    override fun simulatePDFs(yearMonth: String) = pdfService.simulatePDFs(yearMonth)

    override fun generatePDFs(yearMonth: String) = pdfService.generatePDFs(yearMonth)

    override fun generatePDF(invoiceId: String) = pdfService.generatePDF(invoiceId)

    override fun simulateEmails(yearMonth: String): PaymentTypeInvoicesDTO {
        TODO("Not yet implemented")
    }

    override fun generateEmails(yearMonth: String): PaymentTypeInvoicesDTO {
        TODO("Not yet implemented")
    }
}