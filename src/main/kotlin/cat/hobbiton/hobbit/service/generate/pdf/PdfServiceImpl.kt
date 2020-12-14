package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Customer
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
    private val productRepository: CachedProductRepository,
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
        val invoices = invoiceRepository.findByPrintedAndYearMonth(printed = false, yearMonth = YearMonth.parse(yearMonth))
        if(invoices.isEmpty()) throw AppException(ErrorMessages.ERROR_PDFS_TO_GENERATE_NOT_FOUND)
        return invoices
    }

    override fun generatePDFs(yearMonth: String): Resource {
        val invoices = getInvoices(yearMonth)
        val pdfs = zipService.zipFiles(invoices.map { getZipFile(it) })
        invoices.forEach { updateInvoice(it) }
        return pdfs
    }

    private fun getZipFile(invoice: Invoice): ZipFile {
        val customer = customerRepository.getCustomer(invoice.customerId)
        val pdfName = "${invoice.id} (${customer.id}).pdf"
        return ZipFile(pdfName, getPdf(invoice, customer).inputStream)
    }

    override fun generatePDF(invoiceId: String): Resource {
        val invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow { AppException(ErrorMessages.ERROR_INVOICE_NOT_FOUND, invoiceId) }
        return getPdf(invoice)
    }
}