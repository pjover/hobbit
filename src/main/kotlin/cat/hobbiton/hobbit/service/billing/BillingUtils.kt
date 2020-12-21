package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.InvoiceDTO
import cat.hobbiton.hobbit.api.model.InvoiceLineDTO
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.getChild
import cat.hobbiton.hobbit.model.extension.totalAmount
import java.math.BigDecimal

fun groupConsumptions(childCode: Int, consumptions: List<Consumption>): Pair<Int, List<Consumption>> {
    return Pair(
        childCode,
        consumptions
            .groupBy { it.productId }
            .map { (productId, it) ->
                Consumption(
                    childCode = childCode,
                    productId = productId,
                    units = it.sumOf { it.units },
                    yearMonth = it.first().yearMonth,
                    note = it.map { it.note }.joinToString(separator = ", ")
                )
            }
    )
}

fun getInvoiceDto(customer: Customer, invoice: Invoice): InvoiceDTO {

    return InvoiceDTO(
        code = invoice.id,
        yearMonth = invoice.yearMonth.toString(),
        children = invoice.childrenCodes.map { customer.getChild(it).name },
        totalAmount = invoice.totalAmount(),
        subsidizedAmount = getSubsidizedAmount(customer),
        note = invoice.note,
        lines = invoice.lines
            .map {
                InvoiceLineDTO(
                    productId = it.productId,
                    units = it.units,
                    totalAmount = it.totalAmount(),
                    childCode = it.childCode
                )
            }
    )
}

private fun getSubsidizedAmount(customer: Customer): BigDecimal? {
    return if(customer.invoiceHolder.subsidizedAmount == BigDecimal.ZERO) null
    else customer.invoiceHolder.subsidizedAmount
}