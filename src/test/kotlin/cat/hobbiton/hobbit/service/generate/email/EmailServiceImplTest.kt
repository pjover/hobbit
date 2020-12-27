package cat.hobbiton.hobbit.service.generate.email

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.testCustomer185
import cat.hobbiton.hobbit.testCustomer186
import cat.hobbiton.hobbit.util.error.NotFoundException
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import kotlin.test.assertFailsWith

class EmailServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val emailSenderService = mockk<EmailSenderService>()
        val sut = EmailServiceImpl(invoiceRepository, customerRepository, emailSenderService)

        describe("simulateEmails") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository)

                val actual = sut.simulateEmails(YEAR_MONTH.toString())

                it("should be the pending invoices") {
                    actual shouldBe expectedInvoices("??")[0]
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByEmailedAndYearMonth(false, YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository, emailSenderService)
                every { invoiceRepository.findByEmailedAndYearMonth(false, YEAR_MONTH) } returns emptyList()

                val executor = {
                    sut.simulateEmails(YEAR_MONTH.toString())
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.message shouldBe "There are no pending emails to send"
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByEmailedAndYearMonth(false, YEAR_MONTH)
                    }
                }
            }
        }

        describe("generateEmails") {

            context("there are invoices") {
                mockReaders(invoiceRepository, customerRepository)
                mockWriters(invoiceRepository)
                every { emailSenderService.enqueue(invoice1(), testCustomer185) } returns "EmailBatchCode_1"
                every { emailSenderService.enqueue(invoice2(), testCustomer186) } returns "EmailBatchCode_2"
                every { emailSenderService.send(any()) } just runs

                val actual = sut.generateEmails(YEAR_MONTH.toString())

                it("should be the pending invoices") {
                    actual shouldBe expectedInvoices("??")[0]
                }

                it("call the collaborators") {
                    verify {
                        invoiceRepository.findByEmailedAndYearMonth(false, YEAR_MONTH)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                        emailSenderService.enqueue(invoice1(), testCustomer185)
                        emailSenderService.enqueue(invoice2(), testCustomer186)
                        emailSenderService.send(listOf("EmailBatchCode_1", "EmailBatchCode_2"))
                    }
                }

                it("updates the invoices") {
                    verify {
                        invoiceRepository.saveAll(
                            listOf(
                                invoice1().copy(printed = true),
                                invoice2().copy(printed = true)
                            )
                        )
                    }
                }
            }

            context("there are no invoices") {
                clearMocks(invoiceRepository, customerRepository, emailSenderService)
                every { invoiceRepository.findByEmailedAndYearMonth(false, YEAR_MONTH) } returns emptyList()

                val executor = {
                    sut.generateEmails(YEAR_MONTH.toString())
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.message shouldBe "There are no pending emails to send"
                }

                it("calls invoiceRepository") {
                    verify {
                        invoiceRepository.findByEmailedAndYearMonth(false, YEAR_MONTH)
                    }
                }
            }
        }
    }
}

private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
    clearMocks(invoiceRepository, customerRepository)

    every { invoiceRepository.findByEmailedAndYearMonth(false, YEAR_MONTH) } returns listOf(
        invoice1(),
        invoice2()
    )

    every { customerRepository.getCustomer(185) } returns testCustomer185
    every { customerRepository.getCustomer(186) } returns testCustomer186
}

private fun mockWriters(invoiceRepository: InvoiceRepository) {
    val invoicesSlot = slot<List<Invoice>>()
    every { invoiceRepository.saveAll(capture(invoicesSlot)) } answers { invoicesSlot.captured }
}