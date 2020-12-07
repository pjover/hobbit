package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.PaymentType
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

class GenerateServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val sut = GenerateServiceImpl(invoiceRepository, customerRepository)

        describe("generateSepa") { }

        describe("simulateSepa") {
            mockReaders(invoiceRepository, customerRepository)

            val actual = sut.simulateSepa()

            it("should be the pending invoices for BANK_DIRECT_DEBIT") {

                actual shouldBe expectedInvoices("??")[0]
            }

        }
    }

    private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
        every { invoiceRepository.findByPaymentTypeAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, false) } returns listOf(
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