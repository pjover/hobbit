package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.io.InputStreamResource
import java.nio.charset.StandardCharsets

class GenerateServiceImplTest : DescribeSpec() {

    init {
        val bddService = mockk<BddService>()
        val sut = GenerateServiceImpl(bddService)

        describe("BDD") {
            context("generateBDD") {
                val expected = InputStreamResource("test".byteInputStream(StandardCharsets.UTF_8))
                every { bddService.generateBDD(any()) } returns expected

                val actual = sut.generateBDD(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expected
                }
            }


            context("simulateBDD") {
                val expected = expectedInvoices("??")[0]
                every { bddService.simulateBDD(any()) } returns expected

                val actual = sut.simulateBDD(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expected
                }
            }
        }

        describe("simulatePDFs") { }

        describe("generatePDFs") { }

        describe("generatePDF") { }
    }

}