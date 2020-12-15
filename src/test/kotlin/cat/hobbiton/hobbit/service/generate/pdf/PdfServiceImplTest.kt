package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.testAdultMother
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.util.AppException
import cat.hobbiton.hobbit.util.ByteArrayFilenameResource
import cat.hobbiton.hobbit.util.ZipService
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import java.math.BigDecimal
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

        describe("simulatePDFs") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository, productRepository)

                val actual = sut.simulatePDFs(YEAR_MONTH.toString())

                it("should be the pending invoices") {
                    actual shouldBe expectedInvoices("??")
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository, pdfBuilderService, zipService)
                every { invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH) } returns emptyList()

                val executor = {
                    sut.simulatePDFs(YEAR_MONTH.toString())
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "There are no pending PDFs to generate"
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH)
                    }
                }
            }
        }

        describe("generatePDFs") {

            context("there are invoices") {
                clearMocks(pdfBuilderService)
                mockReaders(invoiceRepository, customerRepository, productRepository)
                mockWriters(invoiceRepository)
                mockZipService(zipService)
                every { pdfBuilderService.generate(invoice1(), customer1, productMap1) } returns ByteArrayFilenameResource("PDF1".toByteArray(StandardCharsets.UTF_8), invoice1().getPdfName())
                every { pdfBuilderService.generate(invoice2(), customer2, productMap2) } returns ByteArrayFilenameResource("PDF2".toByteArray(StandardCharsets.UTF_8), invoice2().getPdfName())

                val actual = sut.generatePDFs(YEAR_MONTH.toString())

                it("returns the zip resource") {
                    actual.filename shouldBe pdfsZipFilename
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                        pdfBuilderService.generate(invoice1(), customer1, productMap1)
                        pdfBuilderService.generate(invoice2(), customer2, productMap2)
                        zipService.zipFiles(any(), pdfsZipFilename)
                    }
                }

                it("updates the invoices") {
                    verify {
                        invoiceRepository.save(invoice1().copy(printed = true))
                        invoiceRepository.save(invoice2().copy(printed = true))
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository, pdfBuilderService)
                every { invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH) } returns emptyList()


                val executor = {
                    sut.generatePDFs(YEAR_MONTH.toString())
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "There are no pending PDFs to generate"
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH)
                    }
                }
            }
        }

        describe("generatePDF") {

            context("invoice found") {
                clearMocks(invoiceRepository, customerRepository, pdfBuilderService)
                mockWriters(invoiceRepository)
                mockZipService(zipService)
                every { customerRepository.getCustomer(185) } returns customer1
                every { invoiceRepository.findById(any()) } returns Optional.of(invoice1())
                val pdf = ByteArrayFilenameResource("PDF1".toByteArray(StandardCharsets.UTF_8), "XX (185).pdf")
                every { pdfBuilderService.generate(invoice1(), customer1, productMap1) } returns pdf

                val actual = sut.generatePDF("XX")

                it("returns the PDF") {
                    actual shouldBe pdf
                }

                it("call the collaborators") {
                    verify {
                        actual.filename shouldBe "XX (185).pdf"
                        invoiceRepository.findById("XX")
                        customerRepository.getCustomer(185)
                        pdfBuilderService.generate(invoice1(), customer1, productMap1)
                    }
                }
                it("updates the invoice") {
                    verify {
                        invoiceRepository.save(invoice1().copy(printed = true))
                    }
                }
            }

            context("invoice not found") {
                clearMocks(invoiceRepository, customerRepository, pdfBuilderService)
                every { customerRepository.getCustomer(185) } returns customer1
                every { invoiceRepository.findById(any()) } returns Optional.empty()

                val executor = {
                    sut.generatePDF("XX")
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "Cannot find invoice with id XX"
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
private val product1 = Product(
    id = "TST",
    name = "TST product",
    shortName = "TST product",
    price = BigDecimal.valueOf(10.9)
)
private val product2 = Product(
    id = "XXX",
    name = "XXX product",
    shortName = "XXX product",
    price = BigDecimal.valueOf(9.1)
)
private val productMap1 = mapOf(
    "TST" to product1,
    "XXX" to product2
)
private val productMap2 = mapOf(
    "TST" to product1
)

private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
    clearMocks(invoiceRepository, customerRepository)

    every { invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH) } returns listOf(
        invoice1(),
        invoice2()
    )

    every { customerRepository.getCustomer(185) } returns customer1
    every { customerRepository.getCustomer(186) } returns customer2

    every { productRepository.getProduct("TST") } returns product1
    every { productRepository.getProduct("XXX") } returns product2
}

private fun mockWriters(invoiceRepository: InvoiceRepository) {
    val invoiceSlot = slot<Invoice>()
    every { invoiceRepository.save(capture(invoiceSlot)) } answers { invoiceSlot.captured }
}

private fun mockZipService(zipService: ZipService) {
    val filenameSlot = slot<String>()
    every { zipService.zipFiles(any(), capture(filenameSlot)) } answers { ByteArrayFilenameResource("ZIP".toByteArray(StandardCharsets.UTF_8), filenameSlot.captured) }
}