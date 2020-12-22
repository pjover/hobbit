package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.generate.getCustomerInvoicesDTOs
import cat.hobbiton.hobbit.util.error.NotFoundException
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class SpreadsheetServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val productRepository: CachedProductRepository,
    private val monthReportService: MonthReportService,
    private val spreadSheetBuilderService: SpreadSheetBuilderService
) : SpreadsheetService {

    override fun simulateMonthReport(yearMonth: String): List<PaymentTypeInvoicesDTO> {
        return getInvoices(yearMonth)
            .groupBy { it.paymentType }
            .map { (paymentType, invoices) ->
                PaymentTypeInvoicesDTO(
                    paymentType = PaymentTypeDTO.valueOf(paymentType.name),
                    totalAmount = invoices.totalAmount(),
                    customers = getCustomerInvoicesDTOs(invoices, customerRepository)
                )
            }
    }

    private fun getInvoices(yearMonth: String): List<Invoice> {
        val invoices = invoiceRepository.findByYearMonth(YearMonth.parse(yearMonth))
        if(invoices.isEmpty()) throw NotFoundException(ErrorMessages.ERROR_SPREAD_SHEET_TO_GENERATE_NOT_FOUND)
        return invoices
    }

    private fun getCustomers(invoices: List<Invoice>): Map<Int, Customer> {
        return invoices
            .map { it.customerId }
            .toSet()
            .map { it to customerRepository.getCustomer(it) }
            .toMap()
    }

    private fun getProducts(invoices: List<Invoice>): Map<String, Product> {
        return invoices
            .flatMap { it.lines }
            .map { it.productId }
            .toSet()
            .map { it to productRepository.getProduct(it) }
            .toMap()
    }

    override fun generateMonthReport(yearMonth: String): Resource {
        val invoices = getInvoices(yearMonth)
        val customers = getCustomers(invoices)
        val products = getProducts(invoices)
        val spreadSheetCells = monthReportService.generate(invoices, customers, products)
        return spreadSheetBuilderService.generate(spreadSheetCells)
    }
}