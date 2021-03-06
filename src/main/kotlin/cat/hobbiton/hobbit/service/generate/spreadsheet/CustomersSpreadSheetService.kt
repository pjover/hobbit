package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.model.Customer

interface CustomersSpreadSheetService {
    fun generate(customers: List<Customer>): SpreadSheet
}
