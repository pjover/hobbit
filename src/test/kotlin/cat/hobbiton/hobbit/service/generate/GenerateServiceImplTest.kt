package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.testAdultMother
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class GenerateServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val timeService = mockk<TimeService>()
        val sut = GenerateServiceImpl(invoiceRepository, customerRepository, timeService)

        every { timeService.currentYearMonth } returns YEAR_MONTH

        describe("generateBDD") { }

        describe("simulateBDD") {
            mockReaders(invoiceRepository, customerRepository)

            val actual = sut.simulateBDD(null)

            it("should be the pending invoices for BANK_DIRECT_DEBIT") {
                actual shouldBe expectedInvoices("??")[0]
            }

            it("call the collaborators") {
                verify(exactly = 1) {
                    timeService.currentYearMonth
                    invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false)
                    customerRepository.getCustomer(185)
                    customerRepository.getCustomer(186)
                }
            }
        }
    }

    private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
        every { invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false) } returns listOf(
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