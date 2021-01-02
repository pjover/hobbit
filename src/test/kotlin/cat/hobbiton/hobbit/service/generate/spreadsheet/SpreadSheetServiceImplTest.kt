package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
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
        val monthSpreadSheetService = mockk<MonthSpreadSheetService>()
        val yearSpreadSheetService = mockk<YearSpreadSheetService>()
        val customersSpreadSheetService = mockk<CustomersSpreadSheetService>()
        val spreadSheetBuilderService = mockk<SpreadSheetBuilderService>()
        val sut = SpreadSheetServiceImpl(invoiceRepository, customerRepository, monthSpreadSheetService, yearSpreadSheetService, customersSpreadSheetService, spreadSheetBuilderService)

        describe("MonthSpreadSheet") {
            context("simulateMonthSpreadSheet") {

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
                            customerRepository.getCustomer(187)
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
                        exception.message shouldBe "There are no invoices to generate the SpreadSheet"
                    }

                    it("calls invoiceRepository") {
                        verify {
                            invoiceRepository.findByYearMonth(YEAR_MONTH)
                        }
                    }
                }
            }

            context("generateMonthSpreadSheet") {

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
                        exception.message shouldBe "There are no invoices to generate the SpreadSheet"
                    }

                    it("calls invoiceRepository") {
                        verify {
                            invoiceRepository.findByYearMonth(YEAR_MONTH)
                        }
                    }
                }
            }
        }

        describe("YearSpreadSheet") {

            context("simulateYearSpreadSheet") {

                context("there are invoices") {
                    mockReaders(invoiceRepository, customerRepository)

                    val actual = sut.simulateYearSpreadSheet(YEAR)

                    it("should be the pending invoices") {
                        actual shouldBe expectedInvoices
                    }

                    it("call the collaborators") {
                        verify {
                            invoiceRepository.findByYearMonthIn(any())
                            customerRepository.getCustomer(185)
                            customerRepository.getCustomer(186)
                            customerRepository.getCustomer(187)
                        }
                    }
                }

                context("there are no invoices") {
                    clearMocks(invoiceRepository, customerRepository)
                    every { invoiceRepository.findByYearMonthIn(any()) } returns emptyList()

                    val executor = {
                        sut.simulateYearSpreadSheet(YEAR)
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                        exception.message shouldBe "There are no invoices to generate the SpreadSheet"
                    }

                    it("calls invoiceRepository") {
                        verify {
                            invoiceRepository.findByYearMonthIn(any())
                        }
                    }
                }
            }

            context("generateYearSpreadSheet") {

                context("there are invoices") {
                    mockReaders(invoiceRepository, customerRepository)
                    every { yearSpreadSheetService.generate(any(), any(), any()) } returns expectedSpreadSheetCells
                    every { spreadSheetBuilderService.generate(any()) } returns
                        FileResource("XLSX".toByteArray(StandardCharsets.UTF_8), yearSpreadSheetFilename)

                    val actual = sut.generateYearSpreadSheet(YEAR)

                    it("returns the spreadsheet resource") {
                        actual.filename shouldBe yearSpreadSheetFilename
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
                        exception.message shouldBe "There are no invoices to generate the SpreadSheet"
                    }

                    it("calls invoiceRepository") {
                        verify {
                            invoiceRepository.findByYearMonthIn(any())
                        }
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

val expectedInvoices = listOf(
    PaymentTypeInvoicesDTO(
        paymentType = PaymentTypeDTO.BANK_DIRECT_DEBIT,
        totalAmount = 105.4.toBigDecimal(),
        customers = listOf(
            CustomerInvoicesDTO(
                code = 185,
                shortName = "Joana Bibiloni",
                totalAmount = 83.6.toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = "??",
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Laura", "Aina"),
                        totalAmount = 83.6.toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 4.toBigDecimal(),
                                totalAmount = 43.6.toBigDecimal(),
                                childCode = 1850
                            ),
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 2.toBigDecimal(),
                                totalAmount = 21.8.toBigDecimal(),
                                childCode = 1851
                            ),
                            InvoiceLineDTO(
                                productId = "XXX",
                                units = 2.toBigDecimal(),
                                totalAmount = 18.2.toBigDecimal(),
                                childCode = 1851
                            )
                        ),
                        note = "Note 1, Note 2, Note 3, Note 4"
                    )
                )
            ),
            CustomerInvoicesDTO(
                code = 186,
                shortName = "Silvia Mayol",
                totalAmount = 21.8.toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = "??",
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Laia"),
                        totalAmount = 21.8.toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 2.toBigDecimal(),
                                totalAmount = 21.8.toBigDecimal(),
                                childCode = 1860
                            )
                        ),
                        note = "Note 5"
                    )
                )
            )
        )
    ),
    PaymentTypeInvoicesDTO(
        paymentType = PaymentTypeDTO.RECTIFICATION,
        totalAmount = (-21.8).toBigDecimal(),
        customers = listOf(
            CustomerInvoicesDTO(
                code = 187,
                shortName = "Cara Santamaria",
                totalAmount = (-21.8).toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = "??",
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Ona"),
                        totalAmount = (-21.8).toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = (-2).toBigDecimal(),
                                totalAmount = (-21.8).toBigDecimal(),
                                childCode = 1870
                            )
                        ),
                        note = "Note 6"
                    )
                )
            )
        )
    )
)