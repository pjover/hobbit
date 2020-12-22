package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
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

class SpreadsheetServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val productRepository = mockk<CachedProductRepository>()
        val monthReportService = mockk<MonthReportService>()
        val spreadSheetBuilderService = mockk<SpreadSheetBuilderService>()
        val sut = SpreadsheetServiceImpl(invoiceRepository, customerRepository, productRepository, monthReportService, spreadSheetBuilderService)

        describe("simulateMonthReport") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository, productRepository)

                val actual = sut.simulateMonthReport(YEAR_MONTH.toString())

                it("should be the pending invoices") {
                    actual shouldBe cat.hobbiton.hobbit.service.generate.pdf.expectedInvoices
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
                    sut.simulateMonthReport(YEAR_MONTH.toString())
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

        describe("generateMonthReport") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository, productRepository)
                every { monthReportService.generate(any(), any(), any()) } returns expectedSpreadSheetCells
                every { spreadSheetBuilderService.generate(any()) } returns
                    FileResource("XLSX".toByteArray(StandardCharsets.UTF_8), monthReportFilename)

                val actual = sut.generateMonthReport(YEAR_MONTH.toString())

                it("returns the spreadsheet resource") {
                    actual.filename shouldBe monthReportFilename
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByYearMonth(YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                        monthReportService.generate(invoices, customerMap, productMap)
                        spreadSheetBuilderService.generate(expectedSpreadSheetCells)
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository, spreadSheetBuilderService)
                every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns emptyList()


                val executor = {
                    sut.generateMonthReport(YEAR_MONTH.toString())
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
private val product1 = Product(
    id = "TST",
    name = "TST product",
    shortName = "TST product",
    price = 10.9.toBigDecimal()
)
private val product2 = Product(
    id = "XXX",
    name = "XXX product",
    shortName = "XXX product",
    price = 9.1.toBigDecimal()
)
private val productMap = mapOf(
    "TST" to product1,
    "XXX" to product2
)
private val invoices = listOf(
    invoice1(),
    invoice2()
)

private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
    clearMocks(invoiceRepository, customerRepository)

    every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns invoices

    every { customerRepository.getCustomer(185) } returns customer1
    every { customerRepository.getCustomer(186) } returns customer2

    every { productRepository.getProduct("TST") } returns product1
    every { productRepository.getProduct("XXX") } returns product2
}


val expectedSpreadSheetCells = SpreadSheetCells(
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