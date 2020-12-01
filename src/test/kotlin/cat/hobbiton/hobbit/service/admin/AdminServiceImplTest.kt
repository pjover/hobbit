package cat.hobbiton.hobbit.service.admin

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.testInvoice
import cat.hobbiton.hobbit.testInvoices
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.math.BigDecimal
import java.time.LocalDate

class AdminServiceImplTest : DescribeSpec() {

    init {
        val repository = mockk<InvoiceRepository>()
        val sut = AdminServiceImpl(repository)

        describe("modifyEntity") {

            val testInvoiceLines = listOf(
                InvoiceLine(productId = "AAA",
                    productName = "AAA product long name",
                    units = BigDecimal.valueOf(1),
                    productPrice = BigDecimal.valueOf(11),
                    taxPercentage = BigDecimal.ZERO,
                    childCode = null
                ),
                InvoiceLine(productId = "BBB",
                    productName = "BBB product long name",
                    units = BigDecimal.valueOf(3),
                    productPrice = BigDecimal.valueOf(5.5),
                    taxPercentage = BigDecimal.valueOf(0.1),
                    childCode = null
                ),
                InvoiceLine(productId = "CCC",
                    productName = "CCC product long name",
                    units = BigDecimal.valueOf(1.5),
                    productPrice = BigDecimal.valueOf(5),
                    taxPercentage = BigDecimal.ZERO,
                    childCode = null
                )
            )

            val testInvoices = listOf(
                testInvoice(100, lines = testInvoiceLines, childrenCodes = listOf(1851)),
                testInvoice(101, lines = testInvoiceLines, childrenCodes = listOf(1850, 1852)),
                testInvoice(102, lines = testInvoiceLines, childrenCodes = listOf(1853, 1851, 1852)),
                testInvoice(103, lines = testInvoiceLines, childrenCodes = listOf(1852))
            )

            every { repository.findAll() } returns testInvoices
            val slot = slot<Invoice>()
            every { repository.save(capture(slot)) } answers { slot.captured }

            val actual = sut.modifyEntity("Invoice")

            it("should return the number of modified elements") {
                actual shouldBe 4
            }

            it("should save every invoice") {
                verify(exactly = 1) {
                    repository.save(Invoice(
                        id = "F-100",
                        customerId = 148,
                        date = DATE,
                        yearMonth = YEAR_MONTH,
                        childrenCodes = listOf(1851),
                        lines = listOf(
                            InvoiceLine(productId = "AAA", productName = null, units = BigDecimal.valueOf(1), productPrice = BigDecimal.valueOf(11), taxPercentage = BigDecimal.ZERO, childCode = 1851),
                            InvoiceLine(productId = "BBB", productName = null, units = BigDecimal.valueOf(3), productPrice = BigDecimal.valueOf(5.5), taxPercentage = BigDecimal.valueOf(0.1), childCode = 1851),
                            InvoiceLine(productId = "CCC", productName = null, units = BigDecimal.valueOf(1.5), productPrice = BigDecimal.valueOf(5), taxPercentage = BigDecimal.ZERO, childCode = 1851)),
                        paymentType = PaymentType.BANK_DIRECT_DEBIT, subsidizedAmount = BigDecimal.ZERO, note = "Invoice note", emailed = false, printed = false))
                    repository.save(Invoice(
                        id = "F-101",
                        customerId = 148,
                        date = DATE,
                        yearMonth = YEAR_MONTH,
                        childrenCodes = listOf(1850, 1852),
                        lines = listOf(
                            InvoiceLine(productId = "AAA", productName = null, units = BigDecimal.valueOf(1), productPrice = BigDecimal.valueOf(11), taxPercentage = BigDecimal.ZERO, childCode = 1850),
                            InvoiceLine(productId = "BBB", productName = null, units = BigDecimal.valueOf(3), productPrice = BigDecimal.valueOf(5.5), taxPercentage = BigDecimal.valueOf(0.1), childCode = 1850),
                            InvoiceLine(productId = "CCC", productName = null, units = BigDecimal.valueOf(1.5), productPrice = BigDecimal.valueOf(5), taxPercentage = BigDecimal.ZERO, childCode = 1850)),
                        paymentType = PaymentType.BANK_DIRECT_DEBIT, subsidizedAmount = BigDecimal.ZERO, note = "Invoice note", emailed = false, printed = false))
                    repository.save(Invoice(
                        id = "F-102",
                        customerId = 148,
                        date = DATE,
                        yearMonth = YEAR_MONTH,
                        childrenCodes = listOf(1853, 1851, 1852),
                        lines = listOf(
                            InvoiceLine(productId = "AAA", productName = null, units = BigDecimal.valueOf(1), productPrice = BigDecimal.valueOf(11), taxPercentage = BigDecimal.ZERO, childCode = 1853),
                            InvoiceLine(productId = "BBB", productName = null, units = BigDecimal.valueOf(3), productPrice = BigDecimal.valueOf(5.5), taxPercentage = BigDecimal.valueOf(0.1), childCode = 1853),
                            InvoiceLine(productId = "CCC", productName = null, units = BigDecimal.valueOf(1.5), productPrice = BigDecimal.valueOf(5), taxPercentage = BigDecimal.ZERO, childCode = 1853)),
                        paymentType = PaymentType.BANK_DIRECT_DEBIT, subsidizedAmount = BigDecimal.ZERO, note = "Invoice note", emailed = false, printed = false))
                    repository.save(Invoice(
                        id = "F-103",
                        customerId = 148,
                        date = DATE,
                        yearMonth = YEAR_MONTH,
                        childrenCodes = listOf(1852),
                        lines = listOf(
                            InvoiceLine(productId = "AAA", productName = null, units = BigDecimal.valueOf(1), productPrice = BigDecimal.valueOf(11), taxPercentage = BigDecimal.ZERO, childCode = 1852),
                            InvoiceLine(productId = "BBB", productName = null, units = BigDecimal.valueOf(3), productPrice = BigDecimal.valueOf(5.5), taxPercentage = BigDecimal.valueOf(0.1), childCode = 1852),
                            InvoiceLine(productId = "CCC", productName = null, units = BigDecimal.valueOf(1.5), productPrice = BigDecimal.valueOf(5), taxPercentage = BigDecimal.ZERO, childCode = 1852)),
                        paymentType = PaymentType.BANK_DIRECT_DEBIT, subsidizedAmount = BigDecimal.ZERO, note = "Invoice note", emailed = false, printed = false))
                }
            }
        }
    }

}