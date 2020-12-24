package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.model.*
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

class YearSpreadSheetServiceImplTest : DescribeSpec() {

    init {
        val sut = YearSpreadSheetServiceImpl()

        describe("generate") {

            val actual = sut.generate(YEAR, invoices(), customers)

            it("generate the spread sheet") {

                actual.title shouldBe "2,019 yearly report"

                actual.headers shouldBe listOf("ID",
                    "First adult VAT",
                    "First adult name",
                    "First adult surname",
                    "First adult second surname",
                    "Second adult VAT",
                    "Second adult name",
                    "Second adult surname",
                    "Second adult second surname",
                    "Child VAT",
                    "Child name",
                    "Child surname",
                    "Child  second surname",
                    "Child birth date",
                    "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "June",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December",
                    "Total"
                )

                actual.lines shouldHaveSize 4

                actual.lines[0] shouldBe listOf(
                    IntCell(185),
                    TextCell("12238561P"),
                    TextCell("Joana"),
                    TextCell("Bibiloni"),
                    TextCell("Oliver"),
                    TextCell(""),
                    TextCell("Pere"),
                    TextCell("Llull"),
                    TextCell("Adrover"),
                    TextCell(""),
                    TextCell("Laura"),
                    TextCell("Llull"),
                    TextCell("Bibiloni"),
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
                    IntCell(185),
                    TextCell("12238561P"),
                    TextCell("Joana"),
                    TextCell("Bibiloni"),
                    TextCell("Oliver"),
                    TextCell(""),
                    TextCell("Pere"),
                    TextCell("Llull"),
                    TextCell("Adrover"),
                    TextCell("60235657Z"),
                    TextCell("Aina"),
                    TextCell("Llull"),
                    TextCell("Bibiloni"),
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
                    IntCell(186),
                    TextCell("12238561P"),
                    TextCell("Silvia"),
                    TextCell("Mayol"),
                    TextCell("Oliver"),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell("Laia"),
                    TextCell("Llull"),
                    TextCell("Bibiloni"),
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
                    IntCell(187),
                    TextCell("Y8304421D"),
                    TextCell("Andrew"),
                    TextCell("Brown"),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell("Ona"),
                    TextCell("Llull"),
                    TextCell("Bibiloni"),
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

private val customer1 = testCustomer()
private val customer2 = testCustomer(
    id = 186,
    adults = listOf(testAdultMother().copy(name = "Silvia", surname = "Mayol")),
    children = listOf(testChild3())
)
private val customer3 = testCustomer(
    id = 187,
    adults = listOf(Adult(name = "Andrew", surname = "Brown", taxId = "Y8304421D", role = AdultRole.TUTOR)),
    children = listOf(testChild4())
)
private val customers = listOf(customer1, customer2, customer3)

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
    childrenCodes = listOf(1852),
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = 2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1852
        )
    ),
    note = "Note 5"
)

fun invoice3() = Invoice(
    id = "??",
    customerId = 187,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1853),
    paymentType = PaymentType.CASH,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = BigDecimal.ONE,
            productPrice = 9.9.toBigDecimal(),
            childCode = 1853
        )
    ),
    note = "Note 6"
)