package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.util.i18n.translate
import java.math.BigDecimal
import kotlin.reflect.KFunction1


fun Invoice.grossAmount(): BigDecimal = lines.grossAmount()

fun Invoice.taxAmount(): BigDecimal = lines.taxAmount()

fun Invoice.totalAmount(): BigDecimal = lines.totalAmount()

fun Invoice.validate(maxAmount: Int, currency: String) {

    require(childrenCodes.isNotEmpty()) {
        ValidationMessages.ERROR_INVOICE_CHILDREN_CODES_EMPTY.translate()
    }

    require(lines.isNotEmpty()) {
        ValidationMessages.ERROR_INVOICE_LINES_EMPTY.translate()
    }

    require(totalAmount() < BigDecimal.valueOf(2500)) {
        ValidationMessages.ERROR_INVOICE_AMOUNT_TOO_HIGH.translate(maxAmount, currency)
    }

    lines.forEach { it.validate() }
}

fun Invoice.childrenNames(customer: Customer): String {
    return customer.children
        .filter { this.childrenCodes.contains(it.code) }
        .joinToString(", ") { it.name }
}

fun List<Invoice>.grossAmount(): BigDecimal = sum(Invoice::grossAmount)

fun List<Invoice>.taxAmount(): BigDecimal = sum(Invoice::taxAmount)

fun List<Invoice>.totalAmount(): BigDecimal = sum(Invoice::totalAmount)

private fun List<Invoice>.sum(getter: KFunction1<Invoice, BigDecimal>): BigDecimal {
    var accumulator: BigDecimal = BigDecimal.ZERO
    for(element: Invoice in this) {
        accumulator = getter(element).add(accumulator)
    }
    return accumulator
}
