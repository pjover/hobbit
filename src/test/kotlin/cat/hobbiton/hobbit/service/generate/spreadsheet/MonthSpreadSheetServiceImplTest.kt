package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.testCustomersMap
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.time.LocalDate

class MonthSpreadSheetServiceImplTest : DescribeSpec() {

    init {
        val sut = MonthSpreadSheetServiceImpl()

        describe("generate") {

            val actual = sut.generate(YEAR_MONTH, invoices, testCustomersMap)

            it("generate the spread sheet") {

                actual.lines shouldHaveSize 3

                actual.lines[0] shouldBe listOf(
                    IntCell(185),
                    TextCell("Joana Bibiloni"),
                    TextCell("Laura, Aina"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    TextCell("2019-05"),
                    TextCell("F-1"),
                    CurrencyCell(BigDecimal.valueOf(83.6)),
                    TextCell("4xTST, 2xTST, 2xXXX"),
                    TextCell("Rebut bancari"),
                    TextCell("Note 1, Note 2, Note 3, Note 4")
                )

                actual.lines[1] shouldBe listOf(
                    IntCell(186),
                    TextCell("Silvia Mayol"),
                    TextCell("Laia"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    TextCell("2019-05"),
                    TextCell("F-2"),
                    CurrencyCell(BigDecimal.valueOf(21.8)),
                    TextCell("2xTST"),
                    TextCell("Rebut bancari"),
                    TextCell("Note 5")
                )

                actual.lines[2] shouldBe listOf(
                    IntCell(187),
                    TextCell("Cara Santamaria"),
                    TextCell("Ona"),
                    DateCell(LocalDate.of(2019, 5, 25)),
                    TextCell("2019-05"),
                    TextCell("F-3"),
                    CurrencyCell(BigDecimal.valueOf(9.9)),
                    TextCell("1xTST"),
                    TextCell("Metàl·lic"),
                    TextCell("Note 6")
                )
            }
        }
    }
}

private val invoices = listOf(
    invoice1().copy(id = "F-1"),
    invoice2().copy(id = "F-2"),
    invoice3().copy(id = "F-3", customerId = 187)
)