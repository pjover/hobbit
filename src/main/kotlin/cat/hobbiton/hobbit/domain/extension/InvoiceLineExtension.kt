package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.InvoiceLine
import java.math.BigDecimal
import kotlin.reflect.KFunction1

fun InvoiceLine.grossAmount(): BigDecimal = productPrice.multiply(units)

fun InvoiceLine.taxAmount(): BigDecimal = grossAmount().multiply(taxPercentage)

fun InvoiceLine.totalAmount(): BigDecimal = grossAmount().add(taxAmount())

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
