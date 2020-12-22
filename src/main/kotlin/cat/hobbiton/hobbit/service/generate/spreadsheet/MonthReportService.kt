package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product

const val monthReportFilename = "Month report.zip"

interface MonthReportService {

    fun generate(invoices: List<Invoice>, customers: Map<Int, Customer>, products: Map<String, Product>): SpreadSheetCells
}