package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Invoice
import java.math.BigDecimal
import kotlin.reflect.KFunction1


fun Invoice.grossAmount(): BigDecimal = lines.grossAmount()

fun Invoice.taxAmount(): BigDecimal = lines.taxAmount()

fun Invoice.totalAmount(): BigDecimal = lines.totalAmount()

fun List<Invoice>.grossAmount(): BigDecimal = sum(Invoice::grossAmount)

fun List<Invoice>.taxAmount(): BigDecimal = sum(Invoice::taxAmount)

fun List<Invoice>.totalAmount(): BigDecimal = sum(Invoice::totalAmount)

private fun List<Invoice>.sum(getter: KFunction1<Invoice, BigDecimal>): BigDecimal {
    var accumulator: BigDecimal = BigDecimal.ZERO
    for (element: Invoice in this) {
        accumulator = getter(element).add(accumulator)
    }
    return accumulator
}
