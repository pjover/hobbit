package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.domain.Invoice
import cat.hobbiton.hobbit.domain.extension.validate
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler(Invoice::class)
class InvoiceEventHandler(
        @Value("\${invoices.maxAmount}") private val maxAmount: Int,
        @Value("\${invoices.currency}") private val currency: String
) {

    @HandleBeforeSave
    @HandleBeforeCreate
    fun validate(invoice: Invoice) = invoice.validate(maxAmount, currency)
}