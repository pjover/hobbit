package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.service.generate.pdf.expectedInvoices
import cat.hobbiton.hobbit.testAdultMother
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.util.error.NotFoundException
import cat.hobbiton.hobbit.util.resource.FileResource
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import kotlin.test.assertFailsWith

class SpreadSheetServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val monthReportService = mockk<MonthSpreadSheetService>()
        val spreadSheetBuilderService = mockk<SpreadSheetBuilderService>()
        val sut = SpreadSheetServiceImpl(invoiceRepository, customerRepository, monthReportService, spreadSheetBuilderService)

        describe("simulateMonthSpreadSheet") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository)

                val actual = sut.simulateMonthSpreadSheet(YEAR_MONTH.toString())

                it("should be the pending invoices") {
                    actual shouldBe expectedInvoices
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByYearMonth(YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository)
                every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns emptyList()

                val executor = {
                    sut.simulateMonthSpreadSheet(YEAR_MONTH.toString())
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.message shouldBe "There are no pending SpreadSheets to generate"
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByYearMonth(YEAR_MONTH)
                    }
                }
            }
        }

        describe("generateMonthSpreadSheet") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository)
                every { monthReportService.generate(any(), any()) } returns expectedSpreadSheetCells
                every { spreadSheetBuilderService.generate(any()) } returns
                    FileResource("XLSX".toByteArray(StandardCharsets.UTF_8), monthSpreadSheetFilename)

                val actual = sut.generateMonthSpreadSheet(YEAR_MONTH.toString())

                it("returns the spreadsheet resource") {
                    actual.filename shouldBe monthSpreadSheetFilename
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByYearMonth(YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                        monthReportService.generate(invoices, customerMap)
                        spreadSheetBuilderService.generate(expectedSpreadSheetCells)
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository, spreadSheetBuilderService)
                every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns emptyList()


                val executor = {
                    sut.generateMonthSpreadSheet(YEAR_MONTH.toString())
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.message shouldBe "There are no pending SpreadSheets to generate"
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByYearMonth(YEAR_MONTH)
                    }
                }
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
private val customerMap = mapOf(
    185 to customer1,
    186 to customer2
)

private val invoices = listOf(
    invoice1(),
    invoice2()
)

private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
    clearMocks(invoiceRepository, customerRepository)

    every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns invoices

    every { customerRepository.getCustomer(185) } returns customer1
    every { customerRepository.getCustomer(186) } returns customer2
}


val expectedSpreadSheetCells = SpreadSheet(
    monthSpreadSheetFilename,
    "Llistat de prova",
    listOf("Data",
        "Valor",
        "Valor",
        "Descripció"),
    listOf(
        listOf(
            DateCell(LocalDate.of(2019, 5, 1)),
            TextCell("Linia 1"),
            DecimalCell(BigDecimal.valueOf(11.5))),
        listOf(
            DateCell(LocalDate.of(2019, 5, 2)),
            TextCell("Linia 2"),
            DecimalCell(BigDecimal.valueOf(22))))
)