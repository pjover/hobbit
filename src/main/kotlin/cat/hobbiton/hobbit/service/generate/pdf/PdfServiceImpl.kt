package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.generate.getCustomerInvoicesDTOs
import org.springframework.core.io.Resource
import java.time.YearMonth

class PdfServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val pdfBuilderService: PdfBuilderService
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
        TODO("not implemented")
    }

    override fun generatePDF(invoiceId: String): Resource {
        TODO("not implemented")
    }
}