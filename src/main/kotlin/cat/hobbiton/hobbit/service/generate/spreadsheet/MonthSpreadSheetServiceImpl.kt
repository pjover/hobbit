package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.messages.TextMessages
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.getChild
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.generate.getCustomer
import cat.hobbiton.hobbit.util.i18n.translate
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class MonthSpreadSheetServiceImpl : MonthSpreadSheetService {

    override fun generate(yearMonth: YearMonth, invoices: List<Invoice>, customers: Map<Int, Customer>): SpreadSheet {
        return SpreadSheet(
            monthSpreadSheetFilename,
            getTitle(yearMonth),
            getHeaders(),
            getLines(invoices, customers)
        )
    }

    private fun getTitle(yearMonth: YearMonth): String {
        return TextMessages.MONTH_SPREADSHEET_TITLE.translate(yearMonth.toString())
    }

    private fun getHeaders(): List<String> {
        return listOf(
            TextMessages.MONTH_SPREADSHEET_ID.translate(),
            TextMessages.MONTH_SPREADSHEET_CUSTOMER.translate(),
            TextMessages.MONTH_SPREADSHEET_CHILDREN.translate(),
            TextMessages.MONTH_SPREADSHEET_DATE.translate(),
            TextMessages.MONTH_SPREADSHEET_YEAR_MONTH.translate(),
            TextMessages.MONTH_SPREADSHEET_INVOICE.translate(),
            TextMessages.MONTH_SPREADSHEET_TOTAL.translate(),
            TextMessages.MONTH_SPREADSHEET_PRODUCTS.translate(),
            TextMessages.MONTH_SPREADSHEET_PAYMENT.translate(),
            TextMessages.MONTH_SPREADSHEET_NOTE.translate()
        )
    }

    private fun getLines(invoices: List<Invoice>, customers: Map<Int, Customer>): List<List<Cell>> {
        return invoices
            .sortedBy { it.customerId }
            .map { getLine(it, customers) }
    }

    private fun getLine(invoice: Invoice, customers: Map<Int, Customer>): List<Cell> {
        val customer = customers.getCustomer(invoice.customerId)
        return listOf(
            IntCell(customer.id),
            TextCell(customer.getFirstAdult().shortName()),
            TextCell(getChildren(invoice, customer)),
            DateCell(invoice.date),
            TextCell(invoice.yearMonth.toString()),
            TextCell(invoice.id),
            CurrencyCell(invoice.totalAmount()),
            TextCell(getProducts(invoice)),
            TextCell(invoice.paymentType.message.translate()),
            TextCell(invoice.note ?: "")
        )
    }

    private fun getChildren(invoice: Invoice, customer: Customer): String {
        return invoice.childrenCodes
            .joinToString(", ") { customer.getChild(it).name }
    }

    private fun getProducts(invoice: Invoice): String {
        return invoice.lines
            .joinToString(", ") { "${it.units}x${it.productId}" }
    }
}