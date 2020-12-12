package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.*
import cat.hobbiton.hobbit.util.AppException
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class InvoiceServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val sequenceService = mockk<SequenceService>()
        val consumptionRepository = mockk<ConsumptionRepository>()
        val sut = InvoiceServiceImpl(invoiceRepository, sequenceService, consumptionRepository)

        val invoiceSlot = slot<Invoice>()

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

        describe("saveInvoice") {

            context("the sequence can be saved") {
                every { invoiceRepository.save(capture(invoiceSlot)) } answers { invoiceSlot.captured }
                every { sequenceService.increment(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 1)

                val actual = sut.saveInvoice(invoice, consumptions)

                it("Changes the sequence") {
                    actual shouldBe invoice.copy(id = "F-1")
                }

                it("Saves the invoice") {
                    verify(exactly = 1) {
                        sequenceService.increment(any())
                        invoiceRepository.save(invoice.copy(id = "F-1"))
                    }
                }
            }

            context("the sequence cannot be saved") {

                every { sequenceService.increment(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 2)
                every { sequenceService.decrement(any()) } returns Sequence(SequenceType.STANDARD_INVOICE, 1)

                every { invoiceRepository.save(any()) } throws Exception("Any message")

                val executor = {
                    sut.saveInvoice(invoice, consumptions)
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "Error while saving invoice: F-2"
                }
            }
        }
    }

}