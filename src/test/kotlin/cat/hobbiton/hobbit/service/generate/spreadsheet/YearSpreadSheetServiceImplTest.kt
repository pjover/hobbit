package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.YEAR
import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.testCustomers
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

class YearSpreadSheetServiceImplTest : DescribeSpec() {

    init {
        val sut = YearSpreadSheetServiceImpl()

        describe("generate") {

            val actual = sut.generate(YEAR, invoices(), testCustomers)

            it("generate the spread sheet") {

                actual.lines shouldHaveSize 4

                actual.lines[0] shouldBe listOf(
                    IntCell(1850),
                    TextCell("12238561P"),
                    TextCell("Bibiloni"),
                    TextCell("Oliver"),
                    TextCell("Joana"),
                    TextCell("58921214K"),
                    TextCell("Llull"),
                    TextCell("Adrover"),
                    TextCell("Pere"),
                    TextCell(""),
                    TextCell("Llull"),
                    TextCell("Bibiloni"),
                    TextCell("Laura"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(43.6)),
                    CurrencyCell(BigDecimal.valueOf(523.2))
                )

                actual.lines[1] shouldBe listOf(
                    IntCell(1851),
                    TextCell("12238561P"),
                    TextCell("Bibiloni"),
                    TextCell("Oliver"),
                    TextCell("Joana"),
                    TextCell("58921214K"),
                    TextCell("Llull"),
                    TextCell("Adrover"),
                    TextCell("Pere"),
                    TextCell("60235657Z"),
                    TextCell("Llull"),
                    TextCell("Bibiloni"),
                    TextCell("Aina"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(40.0)),
                    CurrencyCell(BigDecimal.valueOf(480.0))
                )

                actual.lines[2] shouldBe listOf(
                    IntCell(1860),
                    TextCell("97505522N"),
                    TextCell("Mayol"),
                    TextCell("Alcover"),
                    TextCell("Silvia"),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell("Mayol"),
                    TextCell("Alcover"),
                    TextCell("Laia"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    CurrencyCell(BigDecimal.valueOf(261.6))
                )

                actual.lines[3] shouldBe listOf(
                    IntCell(1870),
                    TextCell("X2113060G"),
                    TextCell("Santamaria"),
                    TextCell(""),
                    TextCell("Cara"),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell("Santamaria"),
                    TextCell(""),
                    TextCell("Ona"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    CurrencyCell(BigDecimal.valueOf(118.8))
                )
            }
        }
    }
}

private fun invoices(): List<Invoice> {
    val invoices = mutableListOf<Invoice>()
    for(i in 1..12) {
        invoices.add(invoice1().copy(id = "F-${i * 10 + 1}", yearMonth = YearMonth.of(YEAR, i)))
        invoices.add(invoice2().copy(id = "F-${i * 10 + 2}", yearMonth = YearMonth.of(YEAR, i)))
        invoices.add(invoice3().copy(id = "F-${i * 10 + 3}", yearMonth = YearMonth.of(YEAR, i)))
    }
    return invoices
}

fun invoice1() = Invoice(
    id = "??",
    customerId = 185,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1850, 1851),
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = 4.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1850
        ),
        InvoiceLine(
            productId = "TST",
            units = 2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1851
        ),
        InvoiceLine(
            productId = "XXX",
            units = 2.toBigDecimal(),
            productPrice = 9.1.toBigDecimal(),
            childCode = 1851
        )
    ),
    note = "Note 1, Note 2, Note 3, Note 4"
)

fun invoice2() = Invoice(
    id = "??",
    customerId = 186,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1860),
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = 2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1860
        )
    ),
    note = "Note 5"
)

fun invoice3() = Invoice(
    id = "??",
    customerId = 187,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1870),
    paymentType = PaymentType.CASH,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = BigDecimal.ONE,
            productPrice = 9.9.toBigDecimal(),
            childCode = 1870
        )
    ),
    note = "Note 6"
)