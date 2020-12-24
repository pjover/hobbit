package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import java.time.YearMonth

const val monthSpreadSheetFilename = "Month report.xlsx"

interface MonthSpreadSheetService {

    fun generate(yearMonth: YearMonth, invoices: List<Invoice>, customers: Map<Int, Customer>): SpreadSheet
}