package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.service.billing.invoice3
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import java.time.LocalDate

class MonthSpreadSheetServiceImplTest : DescribeSpec() {

    init {
        val sut = MonthSpreadSheetServiceImpl()

        describe("generate") {

            val actual = sut.generate(YEAR_MONTH, invoices, customers)

            it("generate the spread sheet") {

                actual.title shouldBe "2019-05 monthly report"

                actual.headers shouldBe listOf("ID",
                    "Customer",
                    "Children",
                    "Invoice date",
                    "Refered to month",
                    "Invoice",
                    "Total",
                    "Products",
                    "Payment type",
                    "Note")

                actual.lines shouldHaveSize 3

                actual.lines[0] shouldBe listOf(IntCell(185),
                    TextCell("Joana Bibiloni"),
                    TextCell("Laura, Aina"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    TextCell("2019-05"),
                    TextCell("F-1"),
                    CurrencyCell(BigDecimal.valueOf(83.6)),
                    TextCell("4xTST, 2xTST, 2xXXX"),
                    TextCell("Bank direct debit"),
                    TextCell("Note 1, Note 2, Note 3, Note 4"))

                actual.lines[1] shouldBe listOf(IntCell(186),
                    TextCell("Silvia Mayol"),
                    TextCell("Laia"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    TextCell("2019-05"),
                    TextCell("F-2"),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    TextCell("2xTST"),
                    TextCell("Bank direct debit"),
                    TextCell("Note 5"))

                actual.lines[2] shouldBe listOf(IntCell(187),
                    TextCell("Andrew Brown"),
                    TextCell("Ona"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    TextCell("2019-05"),
                    TextCell("F-3"),
                    CurrencyCell(BigDecimal.valueOf(-21.8)),
                    TextCell("-2xTST"),
                    TextCell("Rectification"),
                    TextCell("Note 6"))
            }
        }
    }
}

private val customer1 = testCustomer()
private val customer2 = testCustomer(
    id = 186,
    adults = listOf(testAdultMother().copy(name = "Silvia", surname = "Mayol")),
    children = listOf(testChild1860)
)
private val customer3 = testCustomer(
    id = 187,
    adults = listOf(testAdultMother().copy(name = "Andrew", surname = "Brown")),
    children = listOf(testChild1870)
)
private val customers = mapOf(
    185 to customer1,
    186 to customer2,
    187 to customer3
)

private val invoices = listOf(
    invoice1().copy(id = "F-1"),
    invoice2().copy(id = "F-2"),
    invoice3().copy(id = "F-3", customerId = 187)
)