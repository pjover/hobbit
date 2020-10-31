package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Invoice
import cat.hobbiton.hobbit.domain.InvoiceLine
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
@RepositoryEventHandler(Invoice::class)
class InvoiceEventHandler(
        @Value("\${invoices.maxAmount}") private val maxAmount: Int,
        @Value("\${invoices.currency}") private val currency: String
) {


    @HandleBeforeSave
    @HandleBeforeCreate
    fun validate(invoice: Invoice) {
        require(invoice.childrenCodes.isNotEmpty()) { ValidationMessages.ERROR_INVOICE_CHILDREN_CODES_EMPTY.translate() }
        require(invoice.lines.isNotEmpty()) { ValidationMessages.ERROR_INVOICE_LINES_EMPTY.translate() }
        require(invoice.totalAmount() < BigDecimal.valueOf(2500)) { ValidationMessages.ERROR_INVOICE_AMOUNT_TOO_HIGH.translate(maxAmount, currency) }
        invoice.lines.forEach { validate(it) }
    }

    private fun validate(invoiceLine: InvoiceLine) {
        require(invoiceLine.productId.isNotBlank()) { ValidationMessages.ERROR_INVOICE_LINE_PRODUCT_ID_BLANK.translate() }
        require(invoiceLine.units != BigDecimal.ZERO) { ValidationMessages.ERROR_INVOICE_LINE_PRODUCT_UNITS_ZERO.translate() }

    }
}