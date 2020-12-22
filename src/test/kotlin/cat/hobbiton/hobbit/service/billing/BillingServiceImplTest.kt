package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.model.*
import cat.hobbiton.hobbit.service.aux.TimeService
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify

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
            every { consumptionRepository.findByInvoiceIdNull() } returns consumptions

            val actual = sut.getInvoices()

            it("returns the list of invoices from pending consumptions") {
                actual shouldBe expectedInvoices(code = "??")
            }
        }

        describe("setInvoices") {
            mockReaders(customerRepository, productRepository)
            every { consumptionRepository.findByInvoiceIdNull() } returns consumptions
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
                    invoiceService.saveInvoice(invoice3(), any())
                }
            }
        }
    }

    private fun mockReaders(customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
        every { productRepository.getProduct("TST") } returns Product(
            id = "TST",
            name = "TST product",
            shortName = "TST product",
            price = 10.9.toBigDecimal()
        )
        every { productRepository.getProduct("XXX") } returns Product(
            id = "XXX",
            name = "XXX product",
            shortName = "XXX product",
            price = 9.1.toBigDecimal()
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
        totalAmount = 105.4.toBigDecimal(),
        customers = listOf(
            CustomerInvoicesDTO(
                code = 185,
                shortName = "Joana Bibiloni",
                totalAmount = 83.6.toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = code,
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Laura", "Aina"),
                        totalAmount = 83.6.toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 4.toBigDecimal(),
                                totalAmount = 43.6.toBigDecimal(),
                                childCode = 1850
                            ),
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 2.toBigDecimal(),
                                totalAmount = 21.8.toBigDecimal(),
                                childCode = 1851
                            ),
                            InvoiceLineDTO(
                                productId = "XXX",
                                units = 2.toBigDecimal(),
                                totalAmount = 18.2.toBigDecimal(),
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
                totalAmount = 21.8.toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = code,
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Laia"),
                        totalAmount = 21.8.toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = 2.toBigDecimal(),
                                totalAmount = 21.8.toBigDecimal(),
                                childCode = 1852
                            )
                        ),
                        note = "Note 5"
                    )
                )
            )
        )
    ),
    PaymentTypeInvoicesDTO(
        paymentType = PaymentTypeDTO.RECTIFICATION,
        totalAmount = -21.8.toBigDecimal(),
        customers = listOf(
            CustomerInvoicesDTO(
                code = 186,
                shortName = "Silvia Mayol",
                totalAmount = -21.8.toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = code,
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Laia"),
                        totalAmount = -21.8.toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = -2.toBigDecimal(),
                                totalAmount = -21.8.toBigDecimal(),
                                childCode = 1852
                            )
                        ),
                        note = "Note 6"
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
            units = 4.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1850
        ),
        InvoiceLine(
            productId = "TST",
            units = 2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1851
        ),
        InvoiceLine(
            productId = "XXX",
            units = 2.toBigDecimal(),
            productPrice = 9.1.toBigDecimal(),
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
            units = 2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1852
        )
    ),
    note = "Note 5"
)

fun invoice3() = Invoice(
    id = "??",
    customerId = 186,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1852),
    paymentType = PaymentType.RECTIFICATION,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = -2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1852
        )
    ),
    note = "Note 6"
)

val consumptions = listOf(
    Consumption(
        id = "AA1",
        childCode = 1850,
        productId = "TST",
        units = 2.toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 1"
    ),
    Consumption(
        id = "AA2",
        childCode = 1850,
        productId = "TST",
        units = 2.toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 2"
    ),
    Consumption(
        id = "AA3",
        childCode = 1851,
        productId = "TST",
        units = 2.toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 3"
    ),
    Consumption(
        id = "AA4",
        childCode = 1851,
        productId = "XXX",
        units = 2.toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 4"
    ),
    Consumption(
        id = "AA5",
        childCode = 1852,
        productId = "TST",
        units = 2.toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 5"
    ),
    Consumption(
        id = "AA6",
        childCode = 1852,
        productId = "TST",
        units = -2.toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 6",
        isRectification = true
    )
)


