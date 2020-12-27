package cat.hobbiton.hobbit.db

import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.util.Logging
import org.springframework.stereotype.Component

@Component
class DatabaseFieldUpdater(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CustomerRepository
) {

    private val logger by Logging()

    init {
//        updateCustomers()
//        updateInvoices()
        logger.info("🍺 no fields to update today️️")
    }

    private fun updateCustomers() {
        val customers = customerRepository.findAll()
        customerRepository.saveAll(customers)
        logger.warn("✅ updated ${customers.size} customers ‼️️")
    }

    private fun updateInvoices() {
        val invoices = invoiceRepository.findAll()
        val updatedInvoices = invoices
//            .filter { it.subsidizedAmount != null }
            .map { updateInvoice(it) }
        invoiceRepository.saveAll(updatedInvoices)
        logger.warn("✅ updated ${updatedInvoices.size} invoices ‼️️")
    }

    private fun updateInvoice(invoice: Invoice): Invoice {
        logger.info("Updating invoice $invoice")
        val updatedInvoice = invoice.copy()
        logger.info("Updated invoice $updatedInvoice")
        return updatedInvoice
    }
}