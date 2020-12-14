package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.*
import cat.hobbiton.hobbit.util.AppException
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class InvoiceServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val sequenceService = mockk<SequenceService>()
        val consumptionRepository = mockk<ConsumptionRepository>()
        val sut = InvoiceServiceImpl(invoiceRepository, sequenceService, consumptionRepository)

        val invoiceSlot = slot<Invoice>()
        val consumptionsSlot = slot<List<Consumption>>()

        val invoice = Invoice(
            id = "??",
            customerId = 185,
            date = DATE,
            yearMonth = YEAR_MONTH,
            childrenCodes = listOf(1, 2),
            paymentType = PaymentType.BANK_DIRECT_DEBIT,
            lines = listOf(
                InvoiceLine(
                    productId = "TST",
                    productName = "TST product",
                    units = BigDecimal.valueOf(4),
                    productPrice = BigDecimal.valueOf(10.9),
                    childCode = 1
                ),
                InvoiceLine(
                    productId = "TST",
                    productName = "TST product",
                    units = BigDecimal.valueOf(2),
                    productPrice = BigDecimal.valueOf(10.9),
                    childCode = 2
                ),
                InvoiceLine(
                    productId = "XXX",
                    productName = "XXX product",
                    units = BigDecimal.valueOf(2),
                    productPrice = BigDecimal.valueOf(9.1),
                    childCode = 2
                )
            ),
            note = "Note 1, Note 2, Note 3, Note 4"
        )

        val consumptions = listOf(
            Consumption(
                id = "AA1",
                childCode = 1850,
                productId = "TST",
                units = BigDecimal.valueOf(2),
                yearMonth = YEAR_MONTH,
                note = "Note 1"
            ),
            Consumption(
                id = "AA2",
                childCode = 1850,
                productId = "TST",
                units = BigDecimal.valueOf(2),
                yearMonth = YEAR_MONTH,
                note = "Note 2"
            ),
            Consumption(
                id = "AA3",
                childCode = 1850,
                productId = "TST",
                units = BigDecimal.valueOf(2),
                yearMonth = YEAR_MONTH,
                note = "Note 3"
            ),
            Consumption(
                id = "AA4",
                childCode = 1850,
                productId = "XXX",
                units = BigDecimal.valueOf(2),
                yearMonth = YEAR_MONTH,
                note = "Note 4"
            ),
            Consumption(
                id = "AA5",
                childCode = 1850,
                productId = "TST",
                units = BigDecimal.valueOf(2),
                yearMonth = YEAR_MONTH,
                note = "Note 5"
            )
        )

        describe("the sequence is saved") {
            clearMocks(sequenceService, consumptionRepository, invoiceRepository)
            every { sequenceService.increment(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 2)
            every { consumptionRepository.saveAll(capture(consumptionsSlot)) } answers { consumptionsSlot.captured }
            every { invoiceRepository.save(capture(invoiceSlot)) } answers { invoiceSlot.captured }

            val actual = sut.saveInvoice(invoice, consumptions)

            it("Changes the sequence") {
                actual shouldBe invoice.copy(id = "F-2")
            }

            it("Increments the sequence") {
                verify {
                    sequenceService.increment(PaymentType.BANK_DIRECT_DEBIT.sequenceType)
                }
            }

            it("Saves the invoice and the consumptions") {
                verify {
                    consumptionRepository.saveAll(consumptions.map { it.copy(invoiceId = "F-2") })
                    invoiceRepository.save(invoice.copy(id = "F-2"))
                }
            }

        }

        describe("the invoice cannot be saved") {

            context("fails while saving the invoice") {
                clearMocks(sequenceService, consumptionRepository, invoiceRepository)
                every { sequenceService.increment(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 2)
                every { sequenceService.decrement(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 1)
                every { consumptionRepository.saveAll(capture(consumptionsSlot)) } answers { consumptionsSlot.captured }
                every { invoiceRepository.save(invoice.copy(id = "F-2")) } throws Exception("Any message")

                val executor = {
                    sut.saveInvoice(invoice, consumptions)
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "Error while saving invoice: STANDARD_INVOICE"
                }

                it("Increments and decrements the sequence") {
                    verify {
                        sequenceService.increment(PaymentType.BANK_DIRECT_DEBIT.sequenceType)
                        sequenceService.decrement(PaymentType.BANK_DIRECT_DEBIT.sequenceType)
                    }
                }

                it("Saves and restores the consumptions") {
                    verify {
                        consumptionRepository.saveAll(consumptions.map { it.copy(invoiceId = "F-2") })
                        consumptionRepository.saveAll(consumptions)
                    }
                }
            }

            context("fails while incrementing the sequence") {
                clearMocks(sequenceService, consumptionRepository, invoiceRepository)
                every { sequenceService.increment(any()) } throws Exception("Any message")

                val executor = {
                    sut.saveInvoice(invoice, consumptions)
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "Error while saving invoice: STANDARD_INVOICE"
                }

                it("Increments the sequence") {
                    verify {
                        sequenceService.increment(PaymentType.BANK_DIRECT_DEBIT.sequenceType)
                    }
                }

                it("Do not call the others collaborators") {
                    verify(exactly = 0) {
                        invoiceRepository.save(any())
                        sequenceService.decrement(any())
                        consumptionRepository.saveAll(any())
                    }
                }
            }

            context("fails while saving the consumptions") {
                clearMocks(sequenceService, consumptionRepository, invoiceRepository)
                every { sequenceService.increment(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 2)
                every { sequenceService.decrement(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 1)
                every { consumptionRepository.saveAll(capture(consumptionsSlot)) } throws Exception("Any message")

                val executor = {
                    sut.saveInvoice(invoice, consumptions)
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "Error while saving invoice: STANDARD_INVOICE"
                }

                it("increments the sequence and saves the consumptions") {
                    verify {
                        sequenceService.increment(PaymentType.BANK_DIRECT_DEBIT.sequenceType)
                        consumptionRepository.saveAll(consumptions.map { it.copy(invoiceId = "F-2") })
                    }
                }

                it("decrements the sequence") {
                    verify {
                        sequenceService.decrement(any())
                    }
                }

                it("do not save the invoice") {
                    verify(exactly = 0) {
                        invoiceRepository.save(any())
                    }
                }
            }
        }
    }

}