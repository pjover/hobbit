package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.InvoiceLine
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate
import java.math.BigDecimal
import kotlin.reflect.KFunction1

fun InvoiceLine.grossAmount(): BigDecimal = productPrice.multiply(units)

fun InvoiceLine.taxAmount(): BigDecimal = grossAmount().multiply(taxPercentage)

fun InvoiceLine.totalAmount(): BigDecimal = grossAmount().add(taxAmount())

fun InvoiceLine.validate() {

    require(productId.isNotBlank()) {
        ValidationMessages.ERROR_INVOICE_LINE_PRODUCT_ID_BLANK.translate()
    }

    require(units != BigDecimal.ZERO) {
        ValidationMessages.ERROR_INVOICE_LINE_PRODUCT_UNITS_ZERO.translate()
    }
}

fun List<InvoiceLine>.grossAmount(): BigDecimal = sum(InvoiceLine::grossAmount)

fun List<InvoiceLine>.taxAmount(): BigDecimal = sum(InvoiceLine::taxAmount)

fun List<InvoiceLine>.totalAmount(): BigDecimal = sum(InvoiceLine::totalAmount)

private fun List<InvoiceLine>.sum(getter: KFunction1<InvoiceLine, BigDecimal>): BigDecimal {
    var accumulator: BigDecimal = BigDecimal.ZERO
    for (element: InvoiceLine in this) {
        accumulator = getter(element).add(accumulator)
    }
    return accumulator
}
