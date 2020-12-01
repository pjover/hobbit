package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.model.SequenceType
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.math.BigDecimal

class InvoiceServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val sequenceService = mockk<SequenceService>()
        val sut = InvoiceServiceImpl(invoiceRepository, sequenceService)

        val slot = slot<Invoice>()

        describe("saveInvoice") {
            every { invoiceRepository.save(capture(slot)) } answers { slot.captured }
            every { sequenceService.increment(any()) } returns cat.hobbiton.hobbit.model.Sequence(SequenceType.STANDARD_INVOICE, 1)

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

            val actual = sut.saveInvoice(invoice)

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
    }

}