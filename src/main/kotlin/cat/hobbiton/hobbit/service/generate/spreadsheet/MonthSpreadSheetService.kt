package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice

const val monthSpreadSheetFilename = "Month report.xlsx"

interface MonthSpreadSheetService {

    fun generate(invoices: List<Invoice>, customers: Map<Int, Customer>): SpreadSheetCells
}