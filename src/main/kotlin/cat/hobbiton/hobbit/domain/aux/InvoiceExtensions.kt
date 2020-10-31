package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Invoice
import cat.hobbiton.hobbit.domain.PaymentType
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import kotlin.reflect.KFunction1


fun Invoice.grossAmount(): BigDecimal = lines.grossAmount()

fun Invoice.taxAmount(): BigDecimal = lines.taxAmount()

fun Invoice.totalAmount(): BigDecimal = lines.totalAmount()

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

fun Invoice.formattedTextWithoutId(): String {
    return "%s (%.2f€) %s [%s: %s]"
            .format(dateFormatter.format(yearMonth), totalAmount(), getLinesText(), customerId, getChildrenText())
}

fun Invoice.formattedTextWithId(): String = "[$id] ${formattedTextWithoutId()}"

fun Invoice.idPrefix(): String = this.id.split('-')[0]

fun Invoice.idSequence(): Int = this.id.split('-')[1].toInt()


fun Invoice.getLinesText(): String = lines.joinToString { line -> line.formattedText() }

fun Invoice.getChildrenText(): String = childrenCodes.joinToString(", ")

private fun Invoice.getChildrenLabel() = if (childrenCodes.size > 1) "Infants" else "Infant"

fun Invoice.getChildrenTextWithLabel() = "${getChildrenLabel()}: ${getChildrenText()}"


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

fun List<Invoice>.getSummary(): String {
    return arrayListOf(getSummaryByPaymentTypes(), totalAmountSummary())
            .joinToString("\n")
}

fun List<Invoice>.getSummary(firstLine: String): String {
    return arrayListOf(firstLine, this.getSummary())
            .joinToString("\n")
}

private fun List<Invoice>.getSummaryByPaymentTypes(): String {
    return this.groupBy { it.paymentType }
            .toSortedMap()
            .map { entry -> getSummaryOfPaymentTypeInvoices(entry) }
            .joinToString("\n")
}

private fun getSummaryOfPaymentTypeInvoices(entry: Map.Entry<PaymentType, List<Invoice>>): String {
    val invoiceCodes = entry.value.map { it.customerId }.joinToString(",")
    return "  - %d %s (%s): %.2f€".format(entry.value.size, entry.key.text, invoiceCodes, entry.value.totalAmount())
}

private fun List<Invoice>.totalAmountSummary(): String = "Total: %.2f€".format(this.totalAmount())
