package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.messages.TextMessages
import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.getLastAdult
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.util.i18n.translate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Month

@Service
class YearSpreadSheetServiceImpl : YearSpreadSheetService {

    override fun generate(year: Int, invoices: List<Invoice>, customers: List<Customer>): SpreadSheet {
        return SpreadSheet(
            yearSpreadSheetFilename,
            getTitle(year),
            getHeaders(),
            getLines(invoices, customers)
        )
    }

    private fun getTitle(year: Int): String {
        return TextMessages.YEAR_SPREADSHEET_TITLE.translate(year)
    }

    private fun getHeaders(): List<String> {
        return listOf(
            TextMessages.YEAR_SPREADSHEET_ID.translate(),
            TextMessages.YEAR_SPREADSHEET_FIRST_ADULT_TAX_ID.translate(),
            TextMessages.YEAR_SPREADSHEET_FIRST_ADULT_NAME.translate(),
            TextMessages.YEAR_SPREADSHEET_FIRST_ADULT_SURNAME.translate(),
            TextMessages.YEAR_SPREADSHEET_FIRST_ADULT_SECOND_SURNAME.translate(),
            TextMessages.YEAR_SPREADSHEET_SECOND_ADULT_TAX_ID.translate(),
            TextMessages.YEAR_SPREADSHEET_SECOND_ADULT_NAME.translate(),
            TextMessages.YEAR_SPREADSHEET_SECOND_ADULT_SURNAME.translate(),
            TextMessages.YEAR_SPREADSHEET_SECOND_ADULT_SECOND_SURNAME.translate(),
            TextMessages.YEAR_SPREADSHEET_CHILD_TAX_ID.translate(),
            TextMessages.YEAR_SPREADSHEET_CHILD_NAME.translate(),
            TextMessages.YEAR_SPREADSHEET_CHILD_SURNAME.translate(),
            TextMessages.YEAR_SPREADSHEET_CHILD_SECOND_SURNAME.translate(),
            TextMessages.YEAR_SPREADSHEET_CHILD_BIRTH_DATE.translate(),
            TextMessages.YEAR_SPREADSHEET_JANUARY.translate(),
            TextMessages.YEAR_SPREADSHEET_FEBRUARY.translate(),
            TextMessages.YEAR_SPREADSHEET_MARCH.translate(),
            TextMessages.YEAR_SPREADSHEET_APRIL.translate(),
            TextMessages.YEAR_SPREADSHEET_MAY.translate(),
            TextMessages.YEAR_SPREADSHEET_JUNE.translate(),
            TextMessages.YEAR_SPREADSHEET_JULY.translate(),
            TextMessages.YEAR_SPREADSHEET_AUGUST.translate(),
            TextMessages.YEAR_SPREADSHEET_SEPTEMBER.translate(),
            TextMessages.YEAR_SPREADSHEET_OCTOBER.translate(),
            TextMessages.YEAR_SPREADSHEET_NOVEMBER.translate(),
            TextMessages.YEAR_SPREADSHEET_DECEMBER.translate(),
            TextMessages.YEAR_SPREADSHEET_TOTAL.translate()
        )
    }

    private fun getLines(invoices: List<Invoice>, customers: List<Customer>): List<List<Cell>> {
        return customers
            .sortedBy { it.id }
            .map {
                getCustomerLines(
                    it,
                    invoices.filter { invoice -> invoice.customerId == it.id }
                )
            }
            .flatten()
    }

    private fun getCustomerLines(customer: Customer, invoices: List<Invoice>): List<List<Cell>> {
        return customer.children
            .sortedBy { it.code }
            .filter { child ->
                invoices.any { it.childrenCodes.contains(child.code) }
            }
            .map {
                getChildLine(
                    it,
                    customer,
                    invoices.filter { invoice -> invoice.childrenCodes.contains(it.code) }
                )
            }
    }

    private fun getChildLine(child: Child, customer: Customer, invoices: List<Invoice>): List<Cell> {

        val firstAdult = customer.getFirstAdult()
        val lastAdult = customer.getLastAdult()
        return listOf(
            IntCell(customer.id),
            TextCell(firstAdult.taxId ?: ""),
            TextCell(firstAdult.name),
            TextCell(firstAdult.surname),
            TextCell(firstAdult.secondSurname ?: ""),
            TextCell(lastAdult?.taxId ?: ""),
            TextCell(lastAdult?.name ?: ""),
            TextCell(lastAdult?.surname ?: ""),
            TextCell(lastAdult?.secondSurname ?: ""),
            TextCell(child.taxId ?: ""),
            TextCell(child.name),
            TextCell(child.surname),
            TextCell(child.secondSurname ?: ""),
            DateCell(child.birthDate),
            CurrencyCell(monthAmount(Month.JANUARY, child, invoices)),
            CurrencyCell(monthAmount(Month.FEBRUARY, child, invoices)),
            CurrencyCell(monthAmount(Month.MARCH, child, invoices)),
            CurrencyCell(monthAmount(Month.APRIL, child, invoices)),
            CurrencyCell(monthAmount(Month.MAY, child, invoices)),
            CurrencyCell(monthAmount(Month.JUNE, child, invoices)),
            CurrencyCell(monthAmount(Month.JULY, child, invoices)),
            CurrencyCell(monthAmount(Month.AUGUST, child, invoices)),
            CurrencyCell(monthAmount(Month.SEPTEMBER, child, invoices)),
            CurrencyCell(monthAmount(Month.OCTOBER, child, invoices)),
            CurrencyCell(monthAmount(Month.NOVEMBER, child, invoices)),
            CurrencyCell(monthAmount(Month.DECEMBER, child, invoices)),
            CurrencyCell(totalAmount(child, invoices)),
        )
    }

    private fun monthAmount(month: Month, child: Child, invoices: List<Invoice>): BigDecimal {
        return invoices
            .filter { it.yearMonth.month == month }
            .flatMap { it.lines }
            .filter { it.childCode == child.code }
            .sumOf { it.totalAmount() }
    }

    private fun totalAmount(child: Child, invoices: List<Invoice>): BigDecimal {
        return invoices
            .flatMap { it.lines }
            .filter { it.childCode == child.code }
            .sumOf { it.totalAmount() }
    }
}