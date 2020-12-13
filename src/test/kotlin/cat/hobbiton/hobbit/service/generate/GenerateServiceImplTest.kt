package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import cat.hobbiton.hobbit.service.generate.pdf.PdfService
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.io.InputStreamResource
import java.nio.charset.StandardCharsets

class GenerateServiceImplTest : DescribeSpec() {

    init {
        val bddService = mockk<BddService>()
        val pdfService = mockk<PdfService>()
        val sut = GenerateServiceImpl(bddService, pdfService)

        val expectedResource = InputStreamResource("Test resource".byteInputStream(StandardCharsets.UTF_8))
        val expectedInvoice = expectedInvoices("??")[0]

        describe("BDD") {
            context("generateBDD") {
                every { bddService.generateBDD(any()) } returns expectedResource

                val actual = sut.generateBDD(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expectedResource
                }
            }


            context("simulateBDD") {
                every { bddService.simulateBDD(any()) } returns expectedInvoice

                val actual = sut.simulateBDD(YEAR_MONTH.toString())

                it("should return the correct invoice") {
                    actual shouldBe expectedInvoice
                }
            }
        }

        describe("simulatePDFs") {
            every { pdfService.simulatePDFs(any()) } returns expectedInvoice

            val actual = sut.simulatePDFs(YEAR_MONTH.toString())

            it("should return the correct invoice") {
                actual shouldBe expectedInvoice
            }
        }

        describe("generatePDFs") {
            every { pdfService.generatePDFs(any()) } returns expectedResource

            val actual = sut.generatePDFs(YEAR_MONTH.toString())

            it("should return the correct resource") {
                actual shouldBe expectedResource
            }
        }

        describe("generatePDF") {
            every { pdfService.generatePDF(any()) } returns expectedResource

            val actual = sut.generatePDF("F-1")

            it("should return the correct PaymentTypeInvoicesDTO") {
                actual shouldBe expectedResource
            }
        }
    }

}