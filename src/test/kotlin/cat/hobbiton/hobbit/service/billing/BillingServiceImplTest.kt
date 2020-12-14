package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.service.aux.TimeService
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.math.BigDecimal

class BillingServiceImplTest : DescribeSpec() {

    init {
        val consumptionRepository = mockk<ConsumptionRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val productRepository = mockk<CachedProductRepository>()
        val timeService = mockk<TimeService>()
        val invoiceService = mockk<InvoiceService>()
        val sut = BillingServiceImpl(consumptionRepository, customerRepository, productRepository, timeService, invoiceService)

        every { timeService.currentLocalDate } returns DATE

        describe("getInvoices") {
            mockReaders(customerRepository, productRepository)
            mockConsumptionsReader(consumptionRepository)

            val actual = sut.getInvoices()

            it("returns the list of invoices from pending consumptions") {
                actual shouldBe expectedInvoices(code = "??")
            }
        }

        describe("setInvoices") {
            mockReaders(customerRepository, productRepository)
            mockConsumptionsReader(consumptionRepository)
            val slot = slot<Invoice>()
            every { invoiceService.saveInvoice(capture(slot), any()) } answers { slot.captured.copy(id = "F-1") }

            val actual = sut.setInvoices()

            it("returns the list of invoices from pending consumptions") {
                actual shouldBe expectedInvoices(code = "F-1")
            }

            it("saves the invoices") {
                verify {
                    invoiceService.saveInvoice(invoice1(), any())
                    invoiceService.saveInvoice(invoice2(), any())
                }
            }
        }
    }

    private fun mockReaders(customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
        every { productRepository.getProduct("TST") } returns Product(
            id = "TST",
            name = "TST product",
            shortName = "TST product",
            price = BigDecimal.valueOf(10.9)
        )
        every { productRepository.getProduct("XXX") } returns Product(
            id = "XXX",
            name = "XXX product",
            shortName = "XXX product",
            price = BigDecimal.valueOf(9.1)
        )
        every { customerRepository.getCustomerByChildCode(1850) } returns testCustomer(children = listOf(testChild1(), testChild2()))
        every { customerRepository.getCustomerByChildCode(1851) } returns testCustomer(children = listOf(testChild1(), testChild2()))
        every { customerRepository.getCustomerByChildCode(1852) } returns testCustomer(
            id = 186,
            children = listOf(testChild3()),
            adults = listOf(testAdultTutor())
        )

        every { customerRepository.getChild(1850) } returns testChild1()
        every { customerRepository.getChild(1851) } returns testChild2()
        every { customerRepository.getChild(1852) } returns testChild3()
    }

}

fun expectedInvoices(code: String) = listOf(
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
                        code = code,
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Laura", "Aina"),
                        totalAmount = 83.6,
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 4.0,
                                totalAmount = 43.6,
                                childCode = 1850
                            ),
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 2.0,
                                totalAmount = 21.8,
                                childCode = 1851
                            ),
                            InvoiceLineDTO(
                                productId = "XXX",
                                units = 2.0,
                                totalAmount = 18.2,
                                childCode = 1851
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
                        code = code,
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Laia"),
                        totalAmount = 21.8,
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 2.0,
                                totalAmount = 21.8,
                                childCode = 1852
                            )
                        ),
                        note = "Note 5"
                    )
                )
            )
        )
    )
)

fun invoice1() = Invoice(
    id = "??",
    customerId = 185,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1850, 1851),
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            productName = "TST product",
            units = BigDecimal.valueOf(4),
            productPrice = BigDecimal.valueOf(10.9),
            childCode = 1850
        ),
        InvoiceLine(
            productId = "TST",
            productName = "TST product",
            units = BigDecimal.valueOf(2),
            productPrice = BigDecimal.valueOf(10.9),
            childCode = 1851
        ),
        InvoiceLine(
            productId = "XXX",
            productName = "XXX product",
            units = BigDecimal.valueOf(2),
            productPrice = BigDecimal.valueOf(9.1),
            childCode = 1851
        )
    ),
    note = "Note 1, Note 2, Note 3, Note 4"
)

fun invoice2() = Invoice(
    id = "??",
    customerId = 186,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1852),
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            productName = "TST product",
            units = BigDecimal.valueOf(2),
            productPrice = BigDecimal.valueOf(10.9),
            childCode = 1852
        )
    ),
    note = "Note 5"
)



