package cat.hobbiton.hobbit.db

import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
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
            .filter { it.childrenCodes.size > 1 }
            .map { updateManyChildrenInvoice(it) }
        invoiceRepository.saveAll(updatedInvoices)
        logger.warn("✅ updated ${updatedInvoices.size} invoices ‼️️")
    }

    private fun updateManyChildrenInvoice(invoice: Invoice): Invoice {
        logger.info("Updating invoice $invoice")
        val updatedLines = mutableListOf<InvoiceLine>()
        val childrenCount = invoice.childrenCodes.size.toBigDecimal()
        for(childCode in invoice.childrenCodes) {
            for(line in invoice.lines) {
                val updatedUnits = line.units.divide(childrenCount)
                updatedLines.add(line.copy(childCode = childCode, units = updatedUnits))
            }
        }
        val updatedInvoice = invoice.copy(lines = updatedLines)
        logger.info("Updated invoice $updatedInvoice")
        return updatedInvoice
    }
}