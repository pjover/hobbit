package cat.hobbiton.hobbit.db

import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.extension.getActiveChildrenCodes
import org.springframework.stereotype.Component

@Component
class DatabaseFieldUpdater(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository
) {

    init {
        val invoices = invoiceRepository
            .findAll()
            .map {
                val customer = customerRepository.getCustomer(it.customerId)
                val lines = it.lines
                    .map { line ->
                        line.copy(
                            childCode = getChildCode(line, customer)
                        )
                    }
                it.copy(lines = lines)
            }
        invoiceRepository.saveAll(invoices)
    }

    private fun getChildCode(line: InvoiceLine, customer: Customer): Int {
        return if(line.childCode == 0) customer.getActiveChildrenCodes().first()
        else line.childCode
    }
}