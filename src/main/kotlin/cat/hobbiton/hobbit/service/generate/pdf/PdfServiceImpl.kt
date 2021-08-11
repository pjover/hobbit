package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.util.error.NotFoundException
import cat.hobbiton.hobbit.util.resource.FileResource
import cat.hobbiton.hobbit.util.resource.ZipService
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class PdfServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val productRepository: CachedProductRepository,
    private val pdfBuilderService: PdfBuilderService,
    private val zipService: ZipService
) : PdfService {

    override fun generatePDFs(yearMonth: String, notYetPrinted: Boolean): Resource {
        val invoices = getInvoices(yearMonth, notYetPrinted)
        val pdfs = invoices.map { getPdf(it) }
        val zip = zipService.zipFiles(pdfs, pdfsZipFilename)
        invoices.forEach { updateInvoice(it) }
        return zip
    }

    private fun getInvoices(yearMonth: String, notYetPrinted: Boolean): List<Invoice> {
        val invoices = if(notYetPrinted) {
            invoiceRepository.findByPrintedAndYearMonth(printed = false, yearMonth = YearMonth.parse(yearMonth))
        } else {
            invoiceRepository.findByYearMonth(yearMonth = YearMonth.parse(yearMonth))
        }
        if(invoices.isEmpty()) throw NotFoundException(ErrorMessages.ERROR_PDFS_TO_GENERATE_NOT_FOUND)
        return invoices
    }

    private fun getPdf(invoice: Invoice): FileResource {
        val customer = getInvoiceCustomer(invoice)
        val products = getInvoiceProducts(invoice)
        return pdfBuilderService.generate(invoice, customer, products)
    }

    private fun getInvoiceCustomer(invoice: Invoice) =
        customerRepository.getCustomer(invoice.customerId)

    private fun getInvoiceProducts(invoice: Invoice) =
        invoice.lines.associate { it.productId to productRepository.getProduct(it.productId) }

    override fun generatePDF(invoiceId: String): Resource {
        val invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow { NotFoundException(ErrorMessages.ERROR_INVOICE_NOT_FOUND, invoiceId) }
        val pdf = getPdf(invoice)
        updateInvoice(invoice)
        return pdf
    }

    private fun updateInvoice(invoice: Invoice) {
        invoiceRepository.save(invoice.copy(printed = true))
    }
}