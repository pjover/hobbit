package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.util.error.NotFoundException
import cat.hobbiton.hobbit.util.resource.FileResource
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
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
        val monthSpreadSheetService = mockk<MonthSpreadSheetService>()
        val yearSpreadSheetService = mockk<YearSpreadSheetService>()
        val customersSpreadSheetService = mockk<CustomersSpreadSheetService>()
        val spreadSheetBuilderService = mockk<SpreadSheetBuilderService>()
        val sut = SpreadSheetServiceImpl(invoiceRepository, customerRepository, monthSpreadSheetService, yearSpreadSheetService, customersSpreadSheetService, spreadSheetBuilderService)

        describe("MonthSpreadSheet") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository)
                every { monthSpreadSheetService.generate(any(), any(), any()) } returns expectedSpreadSheetCells
                every { spreadSheetBuilderService.generate(any()) } returns
                    FileResource("XLSX".toByteArray(StandardCharsets.UTF_8), "Month report.xlsx")

                val actual = sut.generateMonthSpreadSheet(YEAR_MONTH.toString())

                it("returns the spreadsheet resource") {
                    actual.filename shouldBe "Month report.xlsx"
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByYearMonth(YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                        customerRepository.getCustomer(187)
                        monthSpreadSheetService.generate(YEAR_MONTH, testInvoices, any())
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
                    exception.errorMessage shouldBe ErrorMessages.ERROR_SPREADSHEET_INVOICES_NOT_FOUND
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByYearMonth(YEAR_MONTH)
                    }
                }
            }
        }

        describe("YearSpreadSheet") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository)
                every { yearSpreadSheetService.generate(any(), any(), any()) } returns expectedSpreadSheetCells
                every { spreadSheetBuilderService.generate(any()) } returns
                    FileResource("XLSX".toByteArray(StandardCharsets.UTF_8), "Year report.xlsx")

                val actual = sut.generateYearSpreadSheet(YEAR)

                it("returns the spreadsheet resource") {
                    actual.filename shouldBe "Year report.xlsx"
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByYearMonthIn(any())
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                        customerRepository.getCustomer(187)
                        yearSpreadSheetService.generate(YEAR, testInvoices, any())
                        spreadSheetBuilderService.generate(expectedSpreadSheetCells)
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository, spreadSheetBuilderService)
                every { invoiceRepository.findByYearMonthIn(any()) } returns emptyList()


                val executor = {
                    sut.generateYearSpreadSheet(YEAR)
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.errorMessage shouldBe ErrorMessages.ERROR_SPREADSHEET_INVOICES_NOT_FOUND
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByYearMonthIn(any())
                    }
                }
            }
        }

        describe("generateCustomersSpreadSheet") {
            clearMocks(customerRepository)
            every { customerRepository.getActiveCustomers() } returns testCustomers
            every { customersSpreadSheetService.generate(any()) } returns expectedSpreadSheetCells
            every { spreadSheetBuilderService.generate(any()) } returns
                FileResource("XLSX".toByteArray(StandardCharsets.UTF_8), "Customers.xlsx")

            val actual = sut.generateCustomersSpreadSheet()

            it("returns the spreadsheet resource") {
                actual.filename shouldBe "Customers.xlsx"
            }

            it("calls customerRepository") {
                verify {
                    customerRepository.getActiveCustomers()
                }
            }
        }
    }
}

private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
    clearMocks(invoiceRepository, customerRepository)

    every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns testInvoices
    every { invoiceRepository.findByYearMonthIn(any()) } returns testInvoices

    every { customerRepository.getCustomer(185) } returns testCustomer185
    every { customerRepository.getCustomer(186) } returns testCustomer186
    every { customerRepository.getCustomer(187) } returns testCustomer187
}


val expectedSpreadSheetCells = SpreadSheet(
    "Month report.xlsx",
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