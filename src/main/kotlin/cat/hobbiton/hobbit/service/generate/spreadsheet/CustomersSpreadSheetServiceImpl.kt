package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.messages.TextMessages
import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.extension.completeName
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.getLastAdult
import cat.hobbiton.hobbit.util.i18n.translate
import org.springframework.stereotype.Service

@Service
class CustomersSpreadSheetServiceImpl : CustomersSpreadSheetService {

    override fun generate(customers: List<Customer>): SpreadSheet {
        return SpreadSheet(
            customersSpreadSheetFilename,
            getTitle(),
            getHeaders(),
            getLines(customers)
        )
    }

    private fun getTitle(): String {
        return TextMessages.CUSTOMERS_SPREADSHEET_TITLE.translate()
    }

    private fun getHeaders(): List<String> {
        return listOf(
            TextMessages.CUSTOMERS_SPREADSHEET_ID.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_CHILD_CODE.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_CHILD_NAME.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_CHILD_GROUP.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_CHILD_BIRTH_DATE.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_CHILD_TAX_ID.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_FIRST_ADULT_NAME.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_FIRST_ADULT_EMAIL.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_FIRST_ADULT_MOBILE.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_FIRST_ADULT_TAX_ID.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_SECOND_ADULT_NAME.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_SECOND_ADULT_EMAIL.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_SECOND_ADULT_MOBILE.translate(),
            TextMessages.CUSTOMERS_SPREADSHEET_SECOND_ADULT_TAX_ID.translate()
        )
    }

    private fun getLines(customers: List<Customer>): List<List<Cell>> {
        return customers
            .sortedBy { it.id }
            .map { getCustomerLines(it) }
            .flatten()
    }

    private fun getCustomerLines(customer: Customer): List<List<Cell>> {
        return customer.children
            .filter { it.active }
            .sortedBy { it.code }
            .map { getChildLine(it, customer) }
    }

    private fun getChildLine(child: Child, customer: Customer): List<Cell> {

        val firstAdult = customer.getFirstAdult()
        val lastAdult = customer.getLastAdult()
        return listOf(
            IntCell(customer.id),
            IntCell(child.code),
            TextCell(child.completeName()),
            TextCell(child.group.text),
            DateCell(child.birthDate),
            TextCell(child.taxId ?: ""),
            TextCell(firstAdult.completeName()),
            TextCell(firstAdult.email ?: ""),
            TextCell(firstAdult.mobilePhone ?: ""),
            TextCell(firstAdult.taxId ?: ""),
            TextCell(lastAdult?.completeName() ?: ""),
            TextCell(lastAdult?.email ?: ""),
            TextCell(lastAdult?.mobilePhone ?: ""),
            TextCell(lastAdult?.taxId ?: "")
        )
    }
}