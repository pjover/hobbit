package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.service.generate.bdd.BddService
import cat.hobbiton.hobbit.service.generate.email.EmailService
import cat.hobbiton.hobbit.service.generate.pdf.PdfService
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheetService
import org.springframework.stereotype.Service

@Service
class GenerateServiceImpl(
    private val bddService: BddService,
    private val pdfService: PdfService,
    private val emailService: EmailService,
    private val spreadSheetService: SpreadSheetService
) : GenerateService {

    override fun simulateBDD(yearMonth: String) = bddService.simulateBDD(yearMonth)

    override fun generateBDD(yearMonth: String) = bddService.generateBDD(yearMonth)

    override fun generatePDFs(yearMonth: String, notYetPrinted: Boolean) = pdfService.generatePDFs(yearMonth, notYetPrinted)

    override fun generatePDF(invoiceId: String) = pdfService.generatePDF(invoiceId)

    override fun simulateEmails(yearMonth: String) = emailService.simulateEmails(yearMonth)

    override fun generateEmails(yearMonth: String) = emailService.generateEmails(yearMonth)

    override fun generateMonthSpreadSheet(yearMonth: String) = spreadSheetService.generateMonthSpreadSheet(yearMonth)

    override fun generateYearSpreadSheet(year: Int) = spreadSheetService.generateYearSpreadSheet(year)

    override fun generateCustomersSpreadSheet() = spreadSheetService.generateCustomersSpreadSheet()
}