package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import java.time.YearMonth

interface MonthSpreadSheetService {
    fun generate(yearMonth: YearMonth, invoices: List<Invoice>, customers: Map<Int, Customer>): SpreadSheet
}