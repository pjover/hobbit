package cat.hobbiton.hobbit.init

import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.db.repository.SequenceRepository
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.testInvoice
import io.kotlintest.IsolationMode
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

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
                        sequenceRepository.save(Sequence(SequenceType.CUSTOMER, 1))
                        sequenceRepository.save(Sequence(SequenceType.STANDARD_INVOICE, 1))
                        sequenceRepository.save(Sequence(SequenceType.SPECIAL_INVOICE, 1))
                        sequenceRepository.save(Sequence(SequenceType.RECTIFICATION_INVOICE, 1))
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
                        sequenceRepository.save(Sequence(SequenceType.CUSTOMER, 1))
                    }
                }
            }
        }

        describe("check sequences with all items synchronized") {
            every { sequenceRepository.findAll() } returns listOf(
                Sequence(SequenceType.CUSTOMER, 226),
                Sequence(SequenceType.STANDARD_INVOICE, 103),
                Sequence(SequenceType.SPECIAL_INVOICE, 96),
                Sequence(SequenceType.RECTIFICATION_INVOICE, 13)
            )
            every { customerRepository.findAll() } returns listOf(
                testCustomer(223),
                testCustomer(225),
                testCustomer(226)
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
                Sequence(SequenceType.CUSTOMER, 226),
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
                    testCustomer(223),
                    testCustomer(225),
                    testCustomer(227)
                )

                sut.checkSequences()

                it("updates the sequence") {
                    verify {
                        sequenceRepository.findAll()
                        customerRepository.findAll()
                        invoiceRepository.findByIdStartingWith("F")
                        invoiceRepository.findByIdStartingWith("X")
                        sequenceRepository.save(Sequence(SequenceType.CUSTOMER, 227))
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
            every { customerRepository.findAll() } returns listOf(
                testCustomer(223),
                testCustomer(225),
                testCustomer(226)
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
            every { customerRepository.findAll() } returns listOf(
                testCustomer(223),
                testCustomer(225),
                testCustomer(226)
            )
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
            every { customerRepository.findAll() } returns listOf(
                testCustomer(223),
                testCustomer(225),
                testCustomer(226)
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
