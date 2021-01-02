package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.model.Customer

const val customersSpreadSheetFilename = "Customers.xlsx"

interface CustomersSpreadSheetService {
    fun generate(customers: List<Customer>): SpreadSheet
}
