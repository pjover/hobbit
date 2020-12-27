package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.PaymentType
import java.math.BigDecimal
import java.time.LocalDate

fun testInvoice(
    id: Int = 103,
    paymentType: PaymentType = PaymentType.BANK_DIRECT_DEBIT,
    invoiceDate: LocalDate = DATE,
    childrenCodes: List<Int> = listOf(1850, 1851),
    lines: List<InvoiceLine> = testInvoiceLines()) = Invoice(
    id = "${paymentType.sequenceType.prefix}-$id",
    date = invoiceDate,
    customerId = 148,
    lines = lines,
    note = "Invoice note",
    emailed = false,
    printed = false,
    paymentType = paymentType,
    childrenCodes = childrenCodes
)

fun testInvoiceLines() = listOf(
    InvoiceLine(productId = "AAA",
        units = 1.toBigDecimal(),
        productPrice = 11.toBigDecimal(),
        taxPercentage = BigDecimal.ZERO,
        childCode = 1850
    ),
    InvoiceLine(productId = "BBB",
        units = 3.toBigDecimal(),
        productPrice = 5.5.toBigDecimal(),
        taxPercentage = 0.1.toBigDecimal(),
        childCode = 1850
    ),
    InvoiceLine(productId = "CCC",
        units = 1.5.toBigDecimal(),
        productPrice = 5.toBigDecimal(),
        taxPercentage = BigDecimal.ZERO,
        childCode = 1851
    )
)

fun testInvoices(firstId: Int = 100, paymentType: PaymentType = PaymentType.BANK_DIRECT_DEBIT, invoiceDate: LocalDate = DATE) = listOf(
    testInvoice(firstId, paymentType, invoiceDate),
    testInvoice(firstId + 1, paymentType, invoiceDate, listOf(1851, 1852)),
    testInvoice(firstId + 2, paymentType, invoiceDate, listOf(1850, 1851, 1852)),
    testInvoice(firstId + 3, paymentType, invoiceDate, listOf(1850))
)
