package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.testAdultMother
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

val bdd = BddServiceImplTest::class.java.getResource("/Test_bdd.q1x")
    .readText(charset(StandardCharsets.UTF_8.name()))

class BddServiceImplTest : DescribeSpec() {

    init {
        val bddBuilderService = mockk<BddBuilderService>()
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val timeService = mockk<TimeService>()
        val sut = BddServiceImpl(bddBuilderService, invoiceRepository, customerRepository, timeService)

        describe("generateBDD") {

            context("with yearMonth") {
                mockReaders(invoiceRepository, customerRepository, timeService, bddBuilderService)
                mockWriters(invoiceRepository)

                val actual = sut.generateBDD(YEAR_MONTH.toString())

                it("doesn't retrieve the current yearMonth") {
                    verify(exactly = 0) {
                        timeService.currentYearMonth
                    }
                }

                it("retrieves all the invoices") {
                    verify(exactly = 1) {
                        invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false)
                    }
                }

                it("updates the invoices") {
                    verify {
                        invoiceRepository.saveAll(
                            listOf(
                                invoice1().copy(sentToBank = true),
                                invoice2().copy(sentToBank = true)
                            )
                        )
                    }
                }

                it("generates the BDD XML") {
                    verify(exactly = 1) {
                        bddBuilderService.generate(listOf(invoice1(), invoice2()))
                    }
                }

                it("returns the BDD file") {
                    val content = actual.inputStream.use { it.readUpToChar() }
                    val actualLines = content.lines().map { it.trim() }
                    val expectedLines = bdd.lines().map { it.trim() }
                    for(i in actualLines.indices) {
                        expectedLines[i] shouldBe actualLines[i]
                    }
                }
            }
        }

        describe("simulateBDD") {
            every { timeService.currentYearMonth } returns YEAR_MONTH

            context("with yearMonth") {
                mockReaders(invoiceRepository, customerRepository, timeService, bddBuilderService)

                val actual = sut.simulateBDD(YEAR_MONTH.toString())

                it("should be the pending invoices for BANK_DIRECT_DEBIT") {
                    actual shouldBe expectedInvoices("??")[0]
                }

                it("call the collaborators") {
                    verify(exactly = 1) {
                        invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                    }

                    verify(exactly = 0) {
                        timeService.currentYearMonth
                    }
                }
            }

            context("without yearMonth") {
                mockReaders(invoiceRepository, customerRepository, timeService, bddBuilderService)

                val actual = sut.simulateBDD(null)

                it("retrieves the current yearMonth") {
                    verify(exactly = 1) {
                        timeService.currentYearMonth
                    }
                }

                it("should be the pending invoices for BANK_DIRECT_DEBIT") {
                    actual shouldBe expectedInvoices("??")[0]
                }

                it("call the collaborators") {
                    verify(exactly = 1) {
                        timeService.currentYearMonth
                        invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false)
                        customerRepository.getCustomer(185)
                        customerRepository.getCustomer(186)
                    }
                }
            }
        }
    }

    private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository, timeService: TimeService,
                            bddBuilderService: BddBuilderService) {
        clearMocks(invoiceRepository, customerRepository, timeService, bddBuilderService)

        every { invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false) } returns listOf(
            invoice1(),
            invoice2()
        )

        every { customerRepository.getCustomer(185) } returns testCustomer()
        every { customerRepository.getCustomer(186) } returns testCustomer(
            id = 186,
            adults = listOf(testAdultMother().copy(name = "Silvia", surname = "Mayol")),
            children = listOf(testChild3()))

        every { timeService.currentYearMonth } returns YEAR_MONTH

        every { bddBuilderService.generate(any()) } returns bdd
    }

    private fun mockWriters(invoiceRepository: InvoiceRepository) {
        val invoicesSlot = slot<List<Invoice>>()
        every { invoiceRepository.saveAll(capture(invoicesSlot)) } answers { invoicesSlot.captured }
    }

    private fun InputStream.readUpToChar(): String {
        return this.bufferedReader(StandardCharsets.UTF_8).lines().collect(Collectors.joining("\n"))
    }

}