package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.model.GroupType
import cat.hobbiton.hobbit.testCustomers
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CustomersSpreadSheetServiceImplTest : DescribeSpec() {

    init {
        val sut = CustomersSpreadSheetServiceImpl()

        describe("generate") {

            val actual = sut.generate(testCustomers)

            it("generate the spread sheet") {

                actual.lines shouldHaveSize 5

                actual.lines[0] shouldBe listOf(
                    IntCell(185),
                    IntCell(1850),
                    TextCell("Laura Llull Bibiloni"),
                    TextCell(GroupType.EI_1.text),
                    DateCell(DATE),
                    TextCell(""),
                    TextCell("Joana Bibiloni Oliver"),
                    TextCell("jbibiloni@gmail.com"),
                    TextCell("644432654"),
                    TextCell("12238561P"),
                    TextCell("Pere Llull Adrover"),
                    TextCell("pllull@gmail.com"),
                    TextCell("666432654"),
                    TextCell("58921214K")
                )

                actual.lines[1] shouldBe listOf(
                    IntCell(185),
                    IntCell(1851),
                    TextCell("Aina Llull Bibiloni"),
                    TextCell(GroupType.EI_1.text),
                    DateCell(DATE),
                    TextCell("60235657Z"),
                    TextCell("Joana Bibiloni Oliver"),
                    TextCell("jbibiloni@gmail.com"),
                    TextCell("644432654"),
                    TextCell("12238561P"),
                    TextCell("Pere Llull Adrover"),
                    TextCell("pllull@gmail.com"),
                    TextCell("666432654"),
                    TextCell("58921214K")
                )

                actual.lines[2] shouldBe listOf(
                    IntCell(186),
                    IntCell(1860),
                    TextCell("Laia Mayol Alcover"),
                    TextCell(GroupType.EI_2.text),
                    DateCell(DATE),
                    TextCell(""),
                    TextCell("Silvia Mayol Alcover"),
                    TextCell("silvia@gmail.com"),
                    TextCell("677732654"),
                    TextCell("97505522N"),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell("")
                )

                actual.lines[3] shouldBe listOf(
                    IntCell(187),
                    IntCell(1870),
                    TextCell("Ona Santamaria"),
                    TextCell(GroupType.EI_3.text),
                    DateCell(DATE),
                    TextCell(""),
                    TextCell("Cara Santamaria"),
                    TextCell("cara@sgu.org"),
                    TextCell("+1699932654"),
                    TextCell("X2113060G"),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell("")
                )

                actual.lines[4] shouldBe listOf(
                    IntCell(188),
                    IntCell(1880),
                    TextCell("Nil Brown"),
                    TextCell(GroupType.EI_1.text),
                    DateCell(LocalDate.of(2019, 1, 28)),
                    TextCell("12238561P"),
                    TextCell("Andrew Brown"),
                    TextCell("abrown@gmail.com"),
                    TextCell(""),
                    TextCell("Y8304421D"),
                    TextCell(""),
                    TextCell(""),
                    TextCell(""),
                    TextCell("")
                )
            }

        }
    }

}