package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource


interface GenerateService {

    fun simulateBDD(yearMonth: String): PaymentTypeInvoicesDTO

    fun generateBDD(yearMonth: String): Resource

    fun generatePDFs(yearMonth: String, notYetPrinted: Boolean): Resource

    fun generatePDF(invoiceId: String): Resource

    fun simulateEmails(yearMonth: String): PaymentTypeInvoicesDTO

    fun generateEmails(yearMonth: String): PaymentTypeInvoicesDTO

    fun generateMonthSpreadSheet(yearMonth: String): Resource

    fun generateYearSpreadSheet(year: Int): Resource

    fun generateCustomersSpreadSheet(): Resource
}
