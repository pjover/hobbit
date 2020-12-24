package cat.hobbiton.hobbit.service.generate.spreadsheet.poi

import cat.hobbiton.hobbit.service.generate.spreadsheet.DateCell
import cat.hobbiton.hobbit.service.generate.spreadsheet.DecimalCell
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheet
import cat.hobbiton.hobbit.service.generate.spreadsheet.TextCell
import cat.hobbiton.hobbit.util.error.AppException
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertFailsWith

class PoiSpreadSheetBuilderServiceTest : DescribeSpec() {

    init {
        val sut = PoiSpreadSheetBuilderService()

        describe("SpreadSheet validation") {

            context("without filename") {
                val spreadSheetCells = SpreadSheet(
                    "",
                    "Test SpreadSheet",
                    listOf("Data",
                        "Value",
                        "Value",
                        "Description"),
                    listOf(
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 1)),
                            TextCell("Line 1"),
                            DecimalCell(BigDecimal.valueOf(11.5))),
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 2)),
                            TextCell("Line 2"),
                            DecimalCell(BigDecimal.valueOf(22))))
                )

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "SpreadSheet filename cannot be blank"
                }
            }

            context("without title") {
                val spreadSheetCells = SpreadSheet(
                    "test.xlsx",
                    "",
                    listOf("Data",
                        "Value",
                        "Value",
                        "Description"),
                    listOf(
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 1)),
                            TextCell("Line 1"),
                            DecimalCell(BigDecimal.valueOf(11.5))),
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 2)),
                            TextCell("Line 2"),
                            DecimalCell(BigDecimal.valueOf(22))))
                )

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "SpreadSheet title cannot be blank"
                }
            }

            context("without headers") {
                val spreadSheetCells = SpreadSheet(
                    "test.xlsx",
                    "Test SpreadSheet",
                    listOf(),
                    listOf(
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 1)),
                            TextCell("Line 1"),
                            DecimalCell(BigDecimal.valueOf(11.5))),
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 2)),
                            TextCell("Line 2"),
                            DecimalCell(BigDecimal.valueOf(22))))
                )

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "There are no headers defined on the SpreadSheet"
                }
            }

            context("without data") {
                val spreadSheetCells = SpreadSheet(
                    "test.xlsx",
                    "Test SpreadSheet",
                    listOf("Data",
                        "Value",
                        "Value",
                        "Description"),
                    listOf()
                )

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "There are no data rows on the SpreadSheet"
                }
            }

            context("with more headers than columns count") {
                val spreadSheetCells = SpreadSheet(
                    "test.xlsx",
                    "Test SpreadSheet",
                    listOf("Data",
                        "Value",
                        "Value",
                        "Description"),
                    listOf(
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 1)),
                            TextCell("Line 1"),
                            DecimalCell(BigDecimal.valueOf(11.5))),
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 2)),
                            TextCell("Line 2"),
                            DecimalCell(BigDecimal.valueOf(22)))))

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "The number of headers colums does not match with the number of data columns"
                }
            }

            context("with less headers than columns count") {
                val spreadSheetCells = SpreadSheet(
                    "test.xlsx",
                    "Test SpreadSheet",
                    listOf("Data",
                        "Description"),
                    listOf(
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 1)),
                            TextCell("Line 1"),
                            DecimalCell(BigDecimal.valueOf(11.5))),
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 2)),
                            TextCell("Line 2"),
                            DecimalCell(BigDecimal.valueOf(22)))))

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "The number of headers colums does not match with the number of data columns"
                }
            }

            context("with more line columns than headers columns") {
                val spreadSheetCells = SpreadSheet(
                    "test.xlsx",
                    "Test SpreadSheet",
                    listOf("Data",
                        "Value",
                        "Description"),
                    listOf(
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 1)),
                            TextCell("Line 1"),
                            DecimalCell(BigDecimal.valueOf(11.5))),
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 2)),
                            TextCell("Line 2"),
                            TextCell("Line 2"),
                            DecimalCell(BigDecimal.valueOf(22)))))

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "The number of headers colums does not match with the number of data columns"
                }
            }

            context("with less line columns than headers columns") {
                val spreadSheetCells = SpreadSheet(
                    "test.xlsx",
                    "Test SpreadSheet",
                    listOf("Data",
                        "Value",
                        "Description"),
                    listOf(
                        listOf(
                            DateCell(LocalDate.of(2019, 5, 1)),
                            TextCell("Line 1"),
                            DecimalCell(BigDecimal.valueOf(11.5))),
                        listOf(
                            TextCell("Line 2"),
                            DecimalCell(BigDecimal.valueOf(22)))))

                val executor = { sut.generate(spreadSheetCells) }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "The number of headers colums does not match with the number of data columns"
                }
            }
        }
    }
}
