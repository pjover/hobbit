package cat.hobbiton.hobbit.service.generate.spreadsheet.poi

import cat.hobbiton.hobbit.init.FormattingProperties
import cat.hobbiton.hobbit.service.generate.spreadsheet.DateCell
import cat.hobbiton.hobbit.service.generate.spreadsheet.DecimalCell
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheet
import cat.hobbiton.hobbit.service.generate.spreadsheet.TextCell
import cat.hobbiton.hobbit.util.error.AppException
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.ByteArrayInputStream
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertFailsWith


class PoiSpreadSheetBuilderServiceTest : DescribeSpec() {

    init {
        val formattingProperties = mockk<FormattingProperties>()
        val sut = PoiSpreadSheetBuilderService(formattingProperties)

        every { formattingProperties.spreadSheetDateFormat } returns "d-mmm-yy"
        every { formattingProperties.spreadSheetCurrencyFormat } returns "#,##0.00_);[Red](#,##0.00)"

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

        describe("generate") {
            val spreadSheetCells = SpreadSheet(
                "test.xlsx",
                "Test SpreadSheet",
                listOf("Data",
                    "Description",
                    "Value"),
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

            val actual = sut.generate(spreadSheetCells)
            val wb = WorkbookFactory.create(ByteArrayInputStream(actual.byteArray))
            val numberOfSheets = wb.numberOfSheets
            val sheet = wb.getSheetAt(0)
            val sheetName = sheet.sheetName
            val rowIterator = sheet.rowIterator()
            val titles = rowIterator.next()
            val titlescount = titles.count()
            val title0 = titles.getCell(0).stringCellValue
            val title1 = titles.getCell(1).stringCellValue
            val title2 = titles.getCell(2).stringCellValue
            val line1 = rowIterator.next()
            val line1count = line1.count()
            val cell10 = line1.getCell(0).localDateTimeCellValue
            val cell11 = line1.getCell(1).stringCellValue
            val cell12 = line1.getCell(2).numericCellValue
            val line2 = rowIterator.next()
            val line2count = line2.count()
            val cell20 = line2.getCell(0).localDateTimeCellValue
            val cell21 = line2.getCell(1).stringCellValue
            val cell22 = line2.getCell(2).numericCellValue
            val hasNext = rowIterator.hasNext()
            wb.close()

            it("generates a valid XLSX") {
                actual.filename shouldBe "test.xlsx"
                sheetName shouldBe "Test SpreadSheet"
                numberOfSheets shouldBe 1
                titlescount shouldBe 3
                line1count shouldBe 3
                line2count shouldBe 3
                hasNext shouldBe false
                title0 shouldBe "Data"
                title1 shouldBe "Description"
                title2 shouldBe "Value"
                cell10 shouldBe LocalDateTime.of(2019, 5, 1, 0, 0, 0)
                cell11 shouldBe "Line 1"
                cell12 shouldBe 11.5
                cell20 shouldBe LocalDateTime.of(2019, 5, 2, 0, 0, 0)
                cell21 shouldBe "Line 2"
                cell22 shouldBe 22.0
            }
        }
    }
}
