package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.service.aux.TimeService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
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
        val sut = BillingServiceImpl(
            consumptionRepository,
            customerRepository,
            productRepository,
            timeService,
            invoiceService
        )

        every { timeService.currentLocalDate } returns DATE

        describe("getInvoices") {

            context("with rectification") {
                mockReaders(customerRepository, productRepository)
                every { consumptionRepository.findByInvoiceId() } returns consumptions

                val actual = sut.getInvoices()

                it("returns the list of invoices from pending consumptions") {
                    actual shouldBe expectedInvoices(code = "??")
                }
            }

            context("without rectification") {
                mockReaders(customerRepository, productRepository)
                every { consumptionRepository.findByInvoiceId() } returns consumptions.filter { !it.isRectification } + listOf(
                    Consumption(
                        id = "AA7",
                        childCode = 1860,
                        productId = "YYY",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "",
                        isRectification = false
                    ),
                    Consumption(
                        id = "AA8",
                        childCode = 1860,
                        productId = "YYY",
                        units = (-2).toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "",
                        isRectification = false
                    )
                )

                val actual = sut.getInvoices()

                it("returns the list of invoices from pending consumptions") {
                    actual shouldBe listOf(expectedInvoices(code = "??")[0])
                }
            }

            context("only rectification") {
                mockReaders(customerRepository, productRepository)
                every { consumptionRepository.findByInvoiceId() } returns consumptions.filter { it.isRectification }

                val actual = sut.getInvoices()

                it("returns the list of invoices from pending consumptions") {
                    actual shouldBe listOf(expectedInvoices(code = "??")[1])
                }
            }
        }

        describe("setInvoices") {
            mockReaders(customerRepository, productRepository)
            every { consumptionRepository.findByInvoiceId() } returns consumptions
            val slot = slot<Invoice>()
            every { invoiceService.saveInvoice(capture(slot), any()) } answers { slot.captured.copy(id = "F-1") }

            val actual = sut.setInvoices()

            it("returns the list of invoices from pending consumptions") {
                actual shouldBe expectedInvoices(code = "F-1")
            }

            it("saves the invoices") {
                verify {
                    invoiceService.saveInvoice(testInvoice185, any())
                    invoiceService.saveInvoice(testInvoice186, any())
                    invoiceService.saveInvoice(testInvoice187, any())
                }
            }
        }
    }

    private fun mockReaders(customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
        every { productRepository.getProduct("TST") } returns testProduct1
        every { productRepository.getProduct("XXX") } returns testProduct2
        every { productRepository.getProduct("YYY") } returns testProduct3

        every { customerRepository.getCustomerByChildCode(1850) } returns testCustomer185
        every { customerRepository.getCustomerByChildCode(1851) } returns testCustomer185
        every { customerRepository.getCustomerByChildCode(1860) } returns testCustomer186
        every { customerRepository.getCustomerByChildCode(1870) } returns testCustomer187

        every { customerRepository.getChild(1850) } returns testChild1850
        every { customerRepository.getChild(1851) } returns testChild1851
        every { customerRepository.getChild(1860) } returns testChild1860
        every { customerRepository.getChild(1870) } returns testChild1870
    }

}

fun expectedInvoices(code: String) = listOf(
    PaymentTypeInvoicesDTO(
        paymentType = PaymentTypeDTO.BANK_DIRECT_DEBIT,
        totalAmount = 105.4.toBigDecimal(),
        numberOfInvoices = 2,
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
                                childCode = 1860
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
        totalAmount = -(21.8).toBigDecimal(),
        numberOfInvoices = 1,
        customers = listOf(
            CustomerInvoicesDTO(
                code = 187,
                shortName = "Cara Santamaria",
                totalAmount = -(21.8).toBigDecimal(),
                invoices = listOf(
                    InvoiceDTO(
                        code = code,
                        yearMonth = YEAR_MONTH.toString(),
                        children = listOf("Ona"),
                        totalAmount = (-21.8).toBigDecimal(),
                        lines = listOf(
                            InvoiceLineDTO(
                                productId = "TST",
                                units = (-2).toBigDecimal(),
                                totalAmount = (-21.8).toBigDecimal(),
                                childCode = 1870
                            )
                        ),
                        note = "Note 6"
                    )
                )
            )
        )
    )
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
        childCode = 1860,
        productId = "TST",
        units = 2.toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 5"
    ),
    Consumption(
        id = "AA6",
        childCode = 1870,
        productId = "TST",
        units = (-2).toBigDecimal(),
        yearMonth = YEAR_MONTH,
        note = "Note 6",
        isRectification = true
    ),
)


