package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.service.generate.getCustomersMap
import cat.hobbiton.hobbit.util.error.NotFoundException
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class SpreadSheetServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val monthSpreadSheetService: MonthSpreadSheetService,
    private val yearSpreadSheetService: YearSpreadSheetService,
    private val customersSpreadSheetService: CustomersSpreadSheetService,
    private val spreadSheetBuilderService: SpreadSheetBuilderService
) : SpreadSheetService {

    override fun generateMonthSpreadSheet(yearMonth: String): Resource {
        val ym = YearMonth.parse(yearMonth)
        val invoices = getInvoices(ym)
        val customers = customerRepository.getCustomersMap(invoices)
        val spreadSheetCells = monthSpreadSheetService.generate(ym, invoices, customers)
        return spreadSheetBuilderService.generate(spreadSheetCells)
    }

    private fun getInvoices(yearMonth: YearMonth): List<Invoice> {
        val invoices = invoiceRepository.findByYearMonth(yearMonth)
        if(invoices.isEmpty()) throw NotFoundException(ErrorMessages.ERROR_SPREADSHEET_INVOICES_NOT_FOUND)
        return invoices
    }

    override fun generateYearSpreadSheet(year: Int): Resource {
        val invoices = getInvoices(year)
        val customers = getCustomers(invoices)
        val spreadSheetCells = yearSpreadSheetService.generate(year, invoices, customers)
        return spreadSheetBuilderService.generate(spreadSheetCells)
    }

    private fun getInvoices(year: Int): List<Invoice> {
        val allMonths = (1..12).map { YearMonth.of(year, it) }
        val invoices = invoiceRepository.findByYearMonthIn(allMonths)
        if(invoices.isEmpty()) throw NotFoundException(ErrorMessages.ERROR_SPREADSHEET_INVOICES_NOT_FOUND)
        return invoices
    }

    private fun getCustomers(invoices: List<Invoice>): List<Customer> {
        return invoices
            .map { it.customerId }
            .toSet()
            .map { customerRepository.getCustomer(it) }
    }

    override fun generateCustomersSpreadSheet(): Resource {
        val customers = customerRepository.getActiveCustomers()
        val spreadSheetCells = customersSpreadSheetService.generate(customers)
        return spreadSheetBuilderService.generate(spreadSheetCells)
    }
}