package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
        val productRepository = mockk<CachedProductRepository>()
        val sut = BddServiceImpl(bddBuilderService, invoiceRepository, customerRepository, productRepository)

        every { invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(any(), any(), any()) } returns listOf(testInvoice185, testInvoice186)
        every { customerRepository.getCustomer(185) } returns testCustomer185
        every { customerRepository.getCustomer(186) } returns testCustomer186
        every { productRepository.getProduct("TST") } returns testProduct1
        every { productRepository.getProduct("XXX") } returns testProduct2
        every { bddBuilderService.generate(any(), any(), any()) } returns bdd

        describe("generateBDD") {

            val invoicesSlot = slot<List<Invoice>>()
            every { invoiceRepository.saveAll(capture(invoicesSlot)) } answers { invoicesSlot.captured }

            val actual = sut.generateBDD(YEAR_MONTH.toString())

            it("retrieves all the invoices") {
                verify {
                    invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false)
                }
            }

            it("builds the auxiliar maps") {
                verify {
                    customerRepository.getCustomer(185)
                    customerRepository.getCustomer(186)
                    productRepository.getProduct("TST")
                    productRepository.getProduct("XXX")
                }
            }

            it("updates the invoices") {
                verify {
                    invoiceRepository.saveAll(
                        listOf(
                            testInvoice185.copy(sentToBank = true),
                            testInvoice186.copy(sentToBank = true)
                        )
                    )
                }
            }

            it("generates the BDD XML") {
                verify {
                    bddBuilderService.generate(listOf(testInvoice185, testInvoice186), any(), any())
                }
            }

            it("returns the BDD file") {
                actual.filename shouldBe bbdFilename
                val content = actual.inputStream.use { it.readUpToChar() }
                val actualLines = content.lines().map { it.trim() }
                val expectedLines = bdd.lines().map { it.trim() }
                for(i in actualLines.indices) {
                    expectedLines[i] shouldBe actualLines[i]
                }
            }
        }

        describe("simulateBDD") {

            val actual = sut.simulateBDD(YEAR_MONTH.toString())

            it("should be the pending invoices for BANK_DIRECT_DEBIT") {
                actual shouldBe expectedInvoices("??")[0]
            }

            it("call the collaborators") {
                verify {
                    invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false)
                    customerRepository.getCustomer(185)
                    customerRepository.getCustomer(186)
                }
            }
        }
    }

    private fun InputStream.readUpToChar(): String {
        return this.bufferedReader(StandardCharsets.UTF_8).lines().collect(Collectors.joining("\n"))
    }
}