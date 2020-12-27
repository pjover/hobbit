package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.PaymentType
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
        val spreadSheetBuilderService = mockk<SpreadSheetBuilderService>()
        val sut = SpreadSheetServiceImpl(invoiceRepository, customerRepository, monthSpreadSheetService, yearSpreadSheetService, spreadSheetBuilderService)

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
                            monthSpreadSheetService.generate(YEAR_MONTH, invoices, customerMap)
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
                            yearSpreadSheetService.generate(YEAR, invoices, customers)
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
    }
}

private val customer1 = testCustomer()
private val customer2 = testCustomer(
    id = 186,
    adults = listOf(testAdultMother().copy(name = "Silvia", surname = "Mayol")),
    children = listOf(testChild1860)
)
private val customerMap = mapOf(
    185 to customer1,
    186 to customer2
)

private val customers = listOf(customer1, customer2)

private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
    clearMocks(invoiceRepository, customerRepository)

    every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns invoices
    every { invoiceRepository.findByYearMonthIn(any()) } returns invoices

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

private val invoice1 = Invoice(
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

private val invoice2 = Invoice(
    id = "??",
    customerId = 186,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1860),
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = 2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1860
        )
    ),
    note = "Note 5"
)

private val invoice3 = Invoice(
    id = "??",
    customerId = 186,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1860),
    paymentType = PaymentType.RECTIFICATION,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = (-2).toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1860
        )
    ),
    note = "Note 6"
)


private val invoices = listOf(
    invoice1,
    invoice2,
    invoice3
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
                code = 186,
                shortName = "Silvia Mayol",
                totalAmount = (-21.8).toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = "??",
                        yearMonth = "2019-05",
                        children = listOf("Laia"),
                        totalAmount = (-21.8).toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = (-2).toBigDecimal(),
                                totalAmount = (-21.8).toBigDecimal(),
                                childCode = 1860)
                        ),
                        note = "Note 6"
                    )
                )
            )
        )
    )
)