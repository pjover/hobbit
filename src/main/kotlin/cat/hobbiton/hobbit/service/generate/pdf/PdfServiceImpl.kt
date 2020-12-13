package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.generate.getCustomerInvoicesDTOs
import cat.hobbiton.hobbit.util.AppException
import cat.hobbiton.hobbit.util.ZipFile
import cat.hobbiton.hobbit.util.ZipService
import org.springframework.core.io.Resource
import java.time.YearMonth

class PdfServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val pdfBuilderService: PdfBuilderService,
    private val zipService: ZipService
) : PdfService {

    override fun simulatePDFs(yearMonth: String): List<PaymentTypeInvoicesDTO> {
        return getInvoices(yearMonth)
            .groupBy { it.paymentType }
            .map { (paymentType, invoices) ->
                PaymentTypeInvoicesDTO(
                    paymentType = PaymentTypeDTO.valueOf(paymentType.name),
                    totalAmount = invoices.totalAmount().toDouble(),
                    customers = getCustomerInvoicesDTOs(invoices, customerRepository)
                )
            }
    }

    private fun getInvoices(yearMonth: String): List<Invoice> {
        return invoiceRepository.findByPrintedAndYearMonth(printed = false, yearMonth = YearMonth.parse(yearMonth))
    }

    override fun generatePDFs(yearMonth: String): Resource {
        return zipService.zipFiles(
            getInvoices(yearMonth)
                .map { getZipFile(it) })
    }

    private fun getZipFile(invoice: Invoice): ZipFile {
        return ZipFile("${invoice.id}.pdf", getPdf(invoice).inputStream)
    }

    private fun getPdf(invoice: Invoice): Resource {
        return pdfBuilderService.generate(invoice, customerRepository.getCustomer(invoice.customerId))
    }

    override fun generatePDF(invoiceId: String): Resource {
        val invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow { AppException(ErrorMessages.ERROR_INVOICE_NOT_FOUND, invoiceId) }
        return getPdf(invoice)
    }
}