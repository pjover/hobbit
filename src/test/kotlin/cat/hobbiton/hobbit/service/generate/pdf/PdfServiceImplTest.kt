package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.testAdultMother
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class PdfServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val pdfBuilderService = mockk<PdfBuilderService>()
        val sut = PdfServiceImpl(invoiceRepository, customerRepository, pdfBuilderService)

        describe("simulatePDFs") {
            mockReaders(invoiceRepository, customerRepository)

            val actual = sut.simulatePDFs(YEAR_MONTH.toString())

            it("should be the pending invoices") {
                actual shouldBe expectedInvoices("??")
            }

            it("call the collaborators") {
                verify(exactly = 1) {
                    invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH)
                    customerRepository.getCustomer(185)
                    customerRepository.getCustomer(186)
                }
            }
        }

        describe("generatePDFs") { }

        describe("generatePDF") { }
    }

    private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
        clearMocks(invoiceRepository, customerRepository)

        every { invoiceRepository.findByPrintedAndYearMonth(false, YEAR_MONTH) } returns listOf(
            invoice1(),
            invoice2()
        )

        every { customerRepository.getCustomer(185) } returns testCustomer()
        every { customerRepository.getCustomer(186) } returns testCustomer(
            id = 186,
            adults = listOf(testAdultMother().copy(name = "Silvia", surname = "Mayol")),
            children = listOf(testChild3()))
    }

}