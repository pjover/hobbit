package cat.hobbiton.hobbit.service.generate.bdd.string

import cat.hobbiton.hobbit.testCustomersMap
import cat.hobbiton.hobbit.testProductsMap
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.nio.charset.StandardCharsets

class StringBddBuilderServiceTest : DescribeSpec() {

    init {
        val invoicesToBddMapper = mockk<InvoicesToBddMapper>()
        val sut = StringBddBuilderService(invoicesToBddMapper)

        describe("build()") {
            val expected = this::class.java.getResource("/Test_bdd.q1x")
                .readText(charset(StandardCharsets.UTF_8.name()))
            val invoices = bddTestInvoices()
            every { invoicesToBddMapper.map(any(), testCustomersMap, testProductsMap) } returns Bdd(
                messageIdentification = "HOBB-20180707204338029-50",
                creationDateTime = "2018-07-07T20:43:38",
                numberOfTransactions = 4,
                controlSum = "146.60",
                name = "Centre d'Educació Infantil Hobbiton, S.L.",
                identification = "ES92000B57398000",
                requestedCollectionDate = "2018-07-08",
                country = "ES",
                addressLine1 = "Carrer de Bisbe Rafael Josep Verger, 4",
                addressLine2 = "07010 Palma, Illes Balears",
                iban = "ES8004872157762000009714",
                bic = "GBMNESMMXXX",
                details = buildTestBddDetailList(
                    "HOBB-20180707204338029-50",
                    invoices))

            val actual: String = sut.generate(invoices, testCustomersMap, testProductsMap)

            it("return the correct q1x") {
                val actualLines = actual.lines().map { it.trim() }
                val expectedLines = expected.lines().map { it.trim() }

                actualLines.size shouldBe expectedLines.size

                for(i in 2 until actualLines.size)
                    actualLines[i] shouldBe expectedLines[i]
            }
        }
    }
}
