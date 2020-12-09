package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.billing.invoice1
import cat.hobbiton.hobbit.service.billing.invoice2
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import cat.hobbiton.hobbit.testAdultMother
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class GenerateServiceImplTest : DescribeSpec() {

    init {
        val bddService = mockk<BddService>()
        val invoiceRepository = mockk<InvoiceRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val timeService = mockk<TimeService>()
        val sut = GenerateServiceImpl(bddService, invoiceRepository, customerRepository, timeService)

        describe("generateBDD") {
            mockReaders(invoiceRepository, customerRepository)
            every { timeService.currentYearMonth } returns YEAR_MONTH

            val bdd = this::class.java.getResource("/Test_bdd.q1x")
                .readText(charset(StandardCharsets.UTF_8.name()))
            every { bddService.generate(any()) } returns bdd

            val actual = sut.generateBDD(YEAR_MONTH.toString())

            it("return the BDD") {
                val content = actual.inputStream.use { it.readUpToChar() }
                val actualLines = content.lines().map { it.trim() }
                val expectedLines = bdd.lines().map { it.trim() }
                for(i in actualLines.indices) {
                    expectedLines[i] shouldBe actualLines[i]
                }
            }

            it("call collaborators") {
                verify(exactly = 1) {
                    invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false)
                    bddService.generate(listOf(invoice1(), invoice2()))
                }
            }
        }

        describe("simulateBDD") {
            mockReaders(invoiceRepository, customerRepository)
            every { timeService.currentYearMonth } returns YEAR_MONTH

            val actual = sut.simulateBDD(null)

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

    private fun mockReaders(invoiceRepository: InvoiceRepository, customerRepository: CachedCustomerRepository) {
        clearAllMocks()

        every { invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, YEAR_MONTH, false) } returns listOf(
            invoice1(),
            invoice2()
        )

        every { customerRepository.getCustomer(185) } returns testCustomer()
        every { customerRepository.getCustomer(186) } returns testCustomer(
            id = 186,
            adults = listOf(testAdultMother().copy(name = "Silvia", surname = "Mayol")),
            children = listOf(testChild3()))
    }

    private fun InputStream.readUpToChar(): String {
        return this.bufferedReader(StandardCharsets.UTF_8).lines().collect(Collectors.joining("\n"))
    }

}