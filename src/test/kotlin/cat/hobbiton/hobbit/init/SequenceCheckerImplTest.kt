package cat.hobbiton.hobbit.init

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.db.repository.SequenceRepository
import cat.hobbiton.hobbit.model.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.math.BigDecimal

internal class SequenceCheckerImplTest : DescribeSpec() {

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    init {
        val customerRepository = mockk<CustomerRepository>()
        val invoiceRepository = mockk<InvoiceRepository>()
        val sequenceRepository = mockk<SequenceRepository>()
        val sut = SequenceCheckerImpl(customerRepository, invoiceRepository, sequenceRepository)

        every { sequenceRepository.save(any()) } answers { firstArg() }

        describe("missed sequence") {

            context("missed all sequences") {
                every { sequenceRepository.findAll() } returns emptyList()
                every { customerRepository.findAll() } returns emptyList()
                every { invoiceRepository.findByIdStartingWith("F") } returns emptyList()
                every { invoiceRepository.findByIdStartingWith("X") } returns emptyList()
                every { invoiceRepository.findByIdStartingWith("R") } returns emptyList()

                sut.checkSequences()

                it("creates all sequences") {
                    verify {
                        sequenceRepository.findAll()
                        sequenceRepository.save(Sequence(SequenceType.CUSTOMER, 0))
                        sequenceRepository.save(Sequence(SequenceType.STANDARD_INVOICE, 0))
                        sequenceRepository.save(Sequence(SequenceType.SPECIAL_INVOICE, 0))
                        sequenceRepository.save(Sequence(SequenceType.RECTIFICATION_INVOICE, 0))
                    }
                }
            }

            context("missed C sequence") {
                every { sequenceRepository.findAll() } returns listOf(
                    Sequence(SequenceType.STANDARD_INVOICE, 103),
                    Sequence(SequenceType.SPECIAL_INVOICE, 96),
                    Sequence(SequenceType.RECTIFICATION_INVOICE, 13)
                )
                every { sequenceRepository.save(any()) } answers { firstArg() }
                every { customerRepository.findAll() } returns emptyList()
                every { invoiceRepository.findByIdStartingWith("F") } returns listOf(
                    testInvoice(id = 98),
                    testInvoice(id = 102),
                    testInvoice(id = 103)
                )
                every { invoiceRepository.findByIdStartingWith("X") } returns listOf(
                    testInvoice(id = 92, paymentType = PaymentType.VOUCHER),
                    testInvoice(id = 94, paymentType = PaymentType.VOUCHER),
                    testInvoice(id = 96, paymentType = PaymentType.VOUCHER)
                )
                every { invoiceRepository.findByIdStartingWith("R") } returns listOf(
                    testInvoice(id = 11),
                    testInvoice(id = 12, paymentType = PaymentType.BANK_TRANSFER),
                    testInvoice(id = 13, paymentType = PaymentType.VOUCHER)
                )

                sut.checkSequences()

                it("creates the missed sequence") {
                    verify {
                        sequenceRepository.findAll()
                        sequenceRepository.save(Sequence(SequenceType.CUSTOMER, 0))
                    }
                }
            }
        }

        describe("check sequences with all items synchronized") {
            every { sequenceRepository.findAll() } returns listOf(
                Sequence(SequenceType.CUSTOMER, 188),
                Sequence(SequenceType.STANDARD_INVOICE, 103),
                Sequence(SequenceType.SPECIAL_INVOICE, 96),
                Sequence(SequenceType.RECTIFICATION_INVOICE, 13)
            )
            every { customerRepository.findAll() } returns testCustomers
            every { invoiceRepository.findByIdStartingWith("F") } returns listOf(
                testInvoice(id = 98),
                testInvoice(id = 102),
                testInvoice(id = 103)
            )
            every { invoiceRepository.findByIdStartingWith("X") } returns listOf(
                testInvoice(id = 92, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 94, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 96, paymentType = PaymentType.VOUCHER)
            )
            every { invoiceRepository.findByIdStartingWith("R") } returns listOf(
                testInvoice(id = 11),
                testInvoice(id = 12, paymentType = PaymentType.BANK_TRANSFER),
                testInvoice(id = 13, paymentType = PaymentType.VOUCHER)
            )

            sut.checkSequences()

            it("don't throw any error") {
                verify {
                    sequenceRepository.findAll()
                    customerRepository.findAll()
                    invoiceRepository.findByIdStartingWith("F")
                    invoiceRepository.findByIdStartingWith("X")
                }
            }
        }

        describe("check sequences with clients not synchronized") {
            every { sequenceRepository.findAll() } returns listOf(
                Sequence(SequenceType.CUSTOMER, 186),
                Sequence(SequenceType.STANDARD_INVOICE, 103),
                Sequence(SequenceType.SPECIAL_INVOICE, 96),
                Sequence(SequenceType.RECTIFICATION_INVOICE, 13)
            )
            every { invoiceRepository.findByIdStartingWith("F") } returns listOf(
                testInvoice(id = 98),
                testInvoice(id = 102),
                testInvoice(id = 103)
            )
            every { invoiceRepository.findByIdStartingWith("X") } returns listOf(
                testInvoice(id = 92, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 94, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 96, paymentType = PaymentType.VOUCHER)
            )
            every { invoiceRepository.findByIdStartingWith("R") } returns listOf(
                testInvoice(id = 11),
                testInvoice(id = 12, paymentType = PaymentType.BANK_TRANSFER),
                testInvoice(id = 13, paymentType = PaymentType.VOUCHER)
            )

            context("The client sequence is different than the stored sequence") {
                every { customerRepository.findAll() } returns listOf(
                    testCustomer186,
                    testCustomer187,
                    testCustomer188
                )

                sut.checkSequences()

                it("updates the sequence") {
                    verify {
                        sequenceRepository.findAll()
                        customerRepository.findAll()
                        invoiceRepository.findByIdStartingWith("F")
                        invoiceRepository.findByIdStartingWith("X")
                        sequenceRepository.save(Sequence(SequenceType.CUSTOMER, 188))
                    }
                }
            }
        }

        describe("check sequences with F invoices not synchronized") {
            every { sequenceRepository.findAll() } returns listOf(
                Sequence(SequenceType.CUSTOMER, 226),
                Sequence(SequenceType.STANDARD_INVOICE, 103),
                Sequence(SequenceType.SPECIAL_INVOICE, 96),
                Sequence(SequenceType.RECTIFICATION_INVOICE, 13)
            )
            every { customerRepository.findAll() } returns testCustomers
            every { invoiceRepository.findByIdStartingWith("X") } returns listOf(
                testInvoice(id = 92, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 94, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 96, paymentType = PaymentType.VOUCHER)
            )
            every { invoiceRepository.findByIdStartingWith("R") } returns listOf(
                testInvoice(id = 11),
                testInvoice(id = 12, paymentType = PaymentType.BANK_TRANSFER),
                testInvoice(id = 13, paymentType = PaymentType.VOUCHER)
            )

            context("The invoice sequence is different than the stored sequence") {
                every { invoiceRepository.findByIdStartingWith("F") } returns listOf(
                    testInvoice(id = 98),
                    testInvoice(id = 102),
                    testInvoice(id = 104)
                )

                sut.checkSequences()

                it("updates the sequence") {
                    verify {
                        sequenceRepository.findAll()
                        customerRepository.findAll()
                        invoiceRepository.findByIdStartingWith("F")
                        invoiceRepository.findByIdStartingWith("X")
                        sequenceRepository.save(Sequence(SequenceType.STANDARD_INVOICE, 104))
                    }
                }
            }
        }

        describe("check sequences with X invoices not synchronized") {
            every { sequenceRepository.findAll() } returns listOf(
                Sequence(SequenceType.CUSTOMER, 226),
                Sequence(SequenceType.STANDARD_INVOICE, 103),
                Sequence(SequenceType.SPECIAL_INVOICE, 96),
                Sequence(SequenceType.RECTIFICATION_INVOICE, 13)
            )
            every { customerRepository.findAll() } returns testCustomers
            every { invoiceRepository.findByIdStartingWith("F") } returns listOf(
                testInvoice(id = 98),
                testInvoice(id = 102),
                testInvoice(id = 103)
            )
            every { invoiceRepository.findByIdStartingWith("R") } returns listOf(
                testInvoice(id = 11),
                testInvoice(id = 12, paymentType = PaymentType.BANK_TRANSFER),
                testInvoice(id = 13, paymentType = PaymentType.VOUCHER)
            )

            context("The invoice sequence is different than the stored sequence") {
                every { invoiceRepository.findByIdStartingWith("X") } returns listOf(
                    testInvoice(id = 95, paymentType = PaymentType.VOUCHER),
                    testInvoice(id = 96, paymentType = PaymentType.VOUCHER),
                    testInvoice(id = 97, paymentType = PaymentType.VOUCHER)
                )

                sut.checkSequences()

                it("updates the sequence") {
                    verify {
                        sequenceRepository.findAll()
                        customerRepository.findAll()
                        invoiceRepository.findByIdStartingWith("F")
                        invoiceRepository.findByIdStartingWith("X")
                        sequenceRepository.save(Sequence(SequenceType.SPECIAL_INVOICE, 97))
                    }
                }
            }
        }

        describe("check sequences with R invoices not synchronized") {
            every { sequenceRepository.findAll() } returns listOf(
                Sequence(SequenceType.CUSTOMER, 226),
                Sequence(SequenceType.STANDARD_INVOICE, 103),
                Sequence(SequenceType.SPECIAL_INVOICE, 96),
                Sequence(SequenceType.RECTIFICATION_INVOICE, 13)
            )
            every { customerRepository.findAll() } returns testCustomers
            every { invoiceRepository.findByIdStartingWith("F") } returns listOf(
                testInvoice(id = 98),
                testInvoice(id = 102),
                testInvoice(id = 103)
            )
            every { invoiceRepository.findByIdStartingWith("X") } returns listOf(
                testInvoice(id = 92, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 94, paymentType = PaymentType.VOUCHER),
                testInvoice(id = 96, paymentType = PaymentType.VOUCHER)
            )

            context("The invoice sequence is different than the stored sequence") {
                every { invoiceRepository.findByIdStartingWith("R") } returns listOf(
                    testInvoice(id = 12),
                    testInvoice(id = 13, paymentType = PaymentType.BANK_TRANSFER),
                    testInvoice(id = 14, paymentType = PaymentType.VOUCHER)
                )

                sut.checkSequences()

                it("updates the sequence") {
                    verify {
                        sequenceRepository.findAll()
                        customerRepository.findAll()
                        invoiceRepository.findByIdStartingWith("F")
                        invoiceRepository.findByIdStartingWith("X")
                        sequenceRepository.save(Sequence(SequenceType.RECTIFICATION_INVOICE, 14))
                    }
                }
            }
        }
    }
}

private fun testInvoice(id: Int = 103, paymentType: PaymentType = PaymentType.BANK_DIRECT_DEBIT) = Invoice(
    id = "${paymentType.sequenceType.prefix}-$id",
    date = DATE,
    customerId = 148,
    lines = listOf(
        InvoiceLine(productId = "AAA",
            units = 1.toBigDecimal(),
            productPrice = 11.toBigDecimal(),
            taxPercentage = BigDecimal.ZERO,
            childCode = 1850
        ),
        InvoiceLine(productId = "BBB",
            units = 3.toBigDecimal(),
            productPrice = 5.5.toBigDecimal(),
            taxPercentage = 0.1.toBigDecimal(),
            childCode = 1850
        ),
        InvoiceLine(productId = "CCC",
            units = 1.5.toBigDecimal(),
            productPrice = 5.toBigDecimal(),
            taxPercentage = BigDecimal.ZERO,
            childCode = 1851
        )
    ),
    note = "Invoice note",
    emailed = false,
    printed = false,
    paymentType = paymentType,
    childrenCodes = listOf(1850, 1851)
)

