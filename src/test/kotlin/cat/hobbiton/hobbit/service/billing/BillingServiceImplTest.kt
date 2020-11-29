package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.*
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.consumptions.mockAuxReaders
import cat.hobbiton.hobbit.service.consumptions.mockConsumptionsReader
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import java.math.BigDecimal

class BillingServiceImplTest : DescribeSpec() {

    init {
        val consumptionRepository = mockk<ConsumptionRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val productRepository = mockk<CachedProductRepository>()
        val invoiceRepository = mockk<InvoiceRepository>()
        val timeService = mockk<TimeService>()
        val sequenceService = mockk<SequenceService>()
        val sut = BillingServiceImpl(consumptionRepository, customerRepository, productRepository, invoiceRepository, timeService, sequenceService)

        val slot = slot<Invoice>()

        every { timeService.currentLocalDate } returns DATE

        describe("getInvoices") {
            mockReaders(customerRepository, productRepository)
            mockConsumptionsReader(consumptionRepository)

            val actual = sut.getInvoices()

            it("returns the list of invoices from pending consumptions") {
                actual shouldBe expectedInvoices()
            }
        }

        describe("setInvoices") {
            mockAuxReaders(customerRepository, productRepository)
            mockConsumptionsReader(consumptionRepository)
            mockWriters(invoiceRepository, sequenceService, slot)

            val actual = sut.setInvoices()

            it("saves the invoices") {
                verify(exactly = 2) {
                    sequenceService.increment(any())
                }
                verify(exactly = 1) {
                    invoiceRepository.save(
                        Invoice(
                            id = "F-1",
                            customerId = 185,
                            date = DATE,
                            yearMonth = YEAR_MONTH,
                            childrenCodes = listOf(1, 2),
                            paymentType = PaymentType.BANK_DIRECT_DEBIT,
                            lines = listOf(
                                InvoiceLine(
                                    productId = "TST",
                                    units = BigDecimal.valueOf(4),
                                    productPrice = BigDecimal.valueOf(10.9),
                                    childCode = 1
                                ),
                                InvoiceLine(
                                    productId = "TST",
                                    units = BigDecimal.valueOf(2),
                                    productPrice = BigDecimal.valueOf(10.9),
                                    childCode = 2
                                ),
                                InvoiceLine(
                                    productId = "STS",
                                    units = BigDecimal.valueOf(2),
                                    productPrice = BigDecimal.valueOf(9.1),
                                    childCode = 2
                                )
                            ),
                            note = "Note 1, Note 2, Note 3, Note 4"
                        )
                    )
                    invoiceRepository.save(
                        Invoice(
                            id = "F-1",
                            customerId = 186,
                            date = DATE,
                            yearMonth = YEAR_MONTH,
                            childrenCodes = listOf(3),
                            paymentType = PaymentType.BANK_DIRECT_DEBIT,
                            lines = listOf(
                                InvoiceLine(
                                    productId = "TST",
                                    units = BigDecimal.valueOf(2),
                                    productPrice = BigDecimal.valueOf(10.9),
                                    childCode = 3
                                )
                            ),
                            note = "Note 5"
                        )
                    )
                }
            }
        }
    }

    private fun mockReaders(customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
        every { productRepository.getProduct("TST") } returns Product(
            id = "TST",
            name = "Test product",
            shortName = "Test",
            price = BigDecimal.valueOf(10.9)
        )
        every { productRepository.getProduct("STS") } returns Product(
            id = "STS",
            name = "Test product",
            shortName = "Test",
            price = BigDecimal.valueOf(9.1)
        )
        every { customerRepository.getCustomerByChildCode(1) } returns testCustomer(children = listOf(testChild1()))
        every { customerRepository.getCustomerByChildCode(2) } returns testCustomer(children = listOf(testChild2()))
        every { customerRepository.getCustomerByChildCode(3) } returns testCustomer(
            id = 186,
            children = listOf(testChild3()),
            adults = listOf(testAdultTutor())
        )

        every { customerRepository.getChild(1) } returns testChild1()
        every { customerRepository.getChild(2) } returns testChild2()
        every { customerRepository.getChild(3) } returns testChild3()
    }

    private fun mockWriters(invoiceRepository: InvoiceRepository, sequenceService: SequenceService, slot: CapturingSlot<Invoice>) {
        every { invoiceRepository.save(capture(slot)) } answers { slot.captured }
        every { sequenceService.increment(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 1)
    }

    private fun expectedInvoices() = listOf(
        PaymentTypeInvoicesDTO(
            paymentType = PaymentTypeDTO.BANK_DIRECT_DEBIT,
            totalAmount = 105.4,
            customers = listOf(
                CustomerInvoicesDTO(
                    code = 185,
                    shortName = "Joana Bibiloni",
                    totalAmount = 83.6,
                    invoices = listOf(
                        InvoiceDTO(
                            code = "??",
                            yearMonth = YEAR_MONTH.toString(),
                            children = listOf("Laura", "Aina"),
                            totalAmount = 83.6,
                            lines = listOf(
                                InvoiceLineDTO(
                                    productId = "TST",
                                    units = 4.0,
                                    totalAmount = 43.6,
                                    childCode = 1
                                ),
                                InvoiceLineDTO(
                                    productId = "TST",
                                    units = 2.0,
                                    totalAmount = 21.8,
                                    childCode = 2
                                ),
                                InvoiceLineDTO(
                                    productId = "STS",
                                    units = 2.0,
                                    totalAmount = 18.2,
                                    childCode = 2
                                )
                            ),
                            note = "Note 1, Note 2, Note 3, Note 4"
                        )
                    )
                ),
                CustomerInvoicesDTO(
                    code = 186,
                    shortName = "Silvia Mayol",
                    totalAmount = 21.8,
                    invoices = listOf(
                        InvoiceDTO(
                            code = "??",
                            yearMonth = YEAR_MONTH.toString(),
                            children = listOf("Laia"),
                            totalAmount = 21.8,
                            lines = listOf(
                                InvoiceLineDTO(
                                    productId = "TST",
                                    units = 2.0,
                                    totalAmount = 21.8,
                                    childCode = 3
                                )
                            ),
                            note = "Note 5"
                        )
                    )
                )
            )
        )
    )
}

