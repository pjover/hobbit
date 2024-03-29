package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.util.error.NotFoundException
import cat.hobbiton.hobbit.util.resource.FileResource
import cat.hobbiton.hobbit.util.resource.ZipService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.test.assertFailsWith

class PdfServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val productRepository = mockk<CachedProductRepository>()
        val pdfBuilderService = mockk<PdfBuilderService>()
        val zipService = mockk<ZipService>()
        val sut = PdfServiceImpl(invoiceRepository, customerRepository, productRepository, pdfBuilderService, zipService)

        describe("generatePDFs") {

            context("not printed") {
                context("there are invoices") {
                    clearMocks(pdfBuilderService)
                    mockReaders(invoiceRepository, customerRepository, productRepository)
                    mockWriters(invoiceRepository)
                    mockZipService(zipService)
                    every { pdfBuilderService.generate(testInvoice185, testCustomer185, any()) } returns
                        FileResource("PDF1".toByteArray(StandardCharsets.UTF_8), testInvoice185.getPdfName())
                    every { pdfBuilderService.generate(testInvoice186, testCustomer186, any()) } returns
                        FileResource("PDF2".toByteArray(StandardCharsets.UTF_8), testInvoice186.getPdfName())

                    val actual = sut.generatePDFs(yearMonth = YEAR_MONTH.toString(), notYetPrinted = true)

                    it("returns the zip resource") {
                        actual.filename shouldBe pdfsZipFilename
                    }

                    it("call the collaborators") {
                        verify {
                            invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH)
                            customerRepository.getCustomer(185)
                            customerRepository.getCustomer(186)
                            pdfBuilderService.generate(testInvoice185, testCustomer185, any())
                            pdfBuilderService.generate(testInvoice186, testCustomer186, any())
                            zipService.zipFiles(any(), pdfsZipFilename)
                        }
                    }

                    it("updates the invoices") {
                        verify {
                            invoiceRepository.save(testInvoice185.copy(printed = true))
                            invoiceRepository.save(testInvoice186.copy(printed = true))
                        }
                    }
                }

                context("there are no invoices") {
                    clearMocks(invoiceRepository, customerRepository, pdfBuilderService)
                    every { invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH) } returns emptyList()


                    val executor = {
                        sut.generatePDFs(yearMonth = YEAR_MONTH.toString(), notYetPrinted = true)
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                        exception.errorMessage shouldBe ErrorMessages.ERROR_PDFS_TO_GENERATE_NOT_FOUND
                    }

                    it("calls invoiceRepository") {
                        verify {
                            invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH)
                        }
                    }
                }
            }

            context("all") {
                context("there are invoices") {
                    clearMocks(pdfBuilderService)
                    mockReaders(invoiceRepository, customerRepository, productRepository)
                    mockWriters(invoiceRepository)
                    mockZipService(zipService)
                    every { pdfBuilderService.generate(testInvoice185, testCustomer185, any()) } returns
                        FileResource("PDF1".toByteArray(StandardCharsets.UTF_8), testInvoice185.getPdfName())
                    every { pdfBuilderService.generate(testInvoice186, testCustomer186, any()) } returns
                        FileResource("PDF2".toByteArray(StandardCharsets.UTF_8), testInvoice186.getPdfName())
                    every { pdfBuilderService.generate(testInvoice187, testCustomer187, any()) } returns
                        FileResource("PDF2".toByteArray(StandardCharsets.UTF_8), testInvoice187.getPdfName())

                    val actual = sut.generatePDFs(yearMonth = YEAR_MONTH.toString(), notYetPrinted = false)

                    it("returns the zip resource") {
                        actual.filename shouldBe pdfsZipFilename
                    }

                    it("call the collaborators") {
                        verify {
                            invoiceRepository.findByYearMonth(YEAR_MONTH)
                            customerRepository.getCustomer(185)
                            customerRepository.getCustomer(186)
                            customerRepository.getCustomer(187)
                            pdfBuilderService.generate(testInvoice185, testCustomer185, any())
                            pdfBuilderService.generate(testInvoice186, testCustomer186, any())
                            pdfBuilderService.generate(testInvoice187, testCustomer187, any())
                            zipService.zipFiles(any(), pdfsZipFilename)
                        }
                    }

                    it("updates the invoices") {
                        verify {
                            invoiceRepository.save(testInvoice185.copy(printed = true))
                            invoiceRepository.save(testInvoice186.copy(printed = true))
                            invoiceRepository.save(testInvoice187.copy(printed = true))
                        }
                    }
                }

                context("there are no invoices") {
                    clearMocks(invoiceRepository, customerRepository, pdfBuilderService)
                    every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns emptyList()


                    val executor = {
                        sut.generatePDFs(yearMonth = YEAR_MONTH.toString(), notYetPrinted = false)
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                        exception.errorMessage shouldBe ErrorMessages.ERROR_PDFS_TO_GENERATE_NOT_FOUND
                    }

                    it("calls invoiceRepository") {
                        verify {
                            invoiceRepository.findByYearMonth(YEAR_MONTH)
                        }
                    }
                }
            }
        }

        describe("generatePDF") {

            context("invoice found") {
                clearMocks(invoiceRepository, customerRepository, pdfBuilderService)
                mockWriters(invoiceRepository)
                mockZipService(zipService)
                every { customerRepository.getCustomer(185) } returns testCustomer185
                every { invoiceRepository.findById(any()) } returns Optional.of(testInvoice185)
                val pdf = FileResource("PDF1".toByteArray(StandardCharsets.UTF_8), "XX (185).pdf")
                every { pdfBuilderService.generate(testInvoice185, testCustomer185, any()) } returns pdf

                val actual = sut.generatePDF("XX")

                it("returns the PDF") {
                    actual shouldBe pdf
                }

                it("call the collaborators") {
                    verify {
                        actual.filename shouldBe "XX (185).pdf"
                        invoiceRepository.findById("XX")
                        customerRepository.getCustomer(185)
                        pdfBuilderService.generate(testInvoice185, testCustomer185, any())
                    }
                }
                it("updates the invoice") {
                    verify {
                        invoiceRepository.save(testInvoice185.copy(printed = true))
                    }
                }
            }

            context("invoice not found") {
                clearMocks(invoiceRepository, customerRepository, pdfBuilderService)
                every { customerRepository.getCustomer(185) } returns testCustomer185
                every { invoiceRepository.findById(any()) } returns Optional.empty()

                val executor = {
                    sut.generatePDF("XX")
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.errorMessage shouldBe ErrorMessages.ERROR_INVOICE_NOT_FOUND
                }
            }
        }
    }
}

private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
    clearMocks(invoiceRepository, customerRepository)

    every { invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH) } returns listOf(
        testInvoice185,
        testInvoice186
    )

    every { invoiceRepository.findByYearMonth(YEAR_MONTH) } returns testInvoices

    every { customerRepository.getCustomer(185) } returns testCustomer185
    every { customerRepository.getCustomer(186) } returns testCustomer186
    every { customerRepository.getCustomer(187) } returns testCustomer187

    every { productRepository.getProduct("TST") } returns testProduct1
    every { productRepository.getProduct("XXX") } returns testProduct2
}

private fun mockWriters(invoiceRepository: InvoiceRepository) {
    val invoiceSlot = slot<Invoice>()
    every { invoiceRepository.save(capture(invoiceSlot)) } answers { invoiceSlot.captured }
}

private fun mockZipService(zipService: ZipService) {
    val filenameSlot = slot<String>()
    every { zipService.zipFiles(any(), capture(filenameSlot)) } answers {
        FileResource("ZIP".toByteArray(StandardCharsets.UTF_8), filenameSlot.captured)
    }
}

val expectedInvoices = listOf(
    PaymentTypeInvoicesDTO(
        paymentType = PaymentTypeDTO.BANK_DIRECT_DEBIT,
        totalAmount = 105.4.toBigDecimal(),
        numberOfInvoices = 2,
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
    )
)