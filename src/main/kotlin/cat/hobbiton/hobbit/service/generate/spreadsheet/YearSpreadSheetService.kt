package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice

interface YearSpreadSheetService {
    fun generate(year: Int, invoices: List<Invoice>, customers: List<Customer>): SpreadSheet
}