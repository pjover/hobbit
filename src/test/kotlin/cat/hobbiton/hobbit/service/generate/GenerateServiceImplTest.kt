package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import cat.hobbiton.hobbit.service.generate.email.EmailService
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
        val emailService = mockk<EmailService>()
        val sut = GenerateServiceImpl(bddService, pdfService, emailService)

        val expectedResource = InputStreamResource("Test resource".byteInputStream(StandardCharsets.UTF_8))
        val expectedInvoice = expectedInvoices("??")

        describe("BDD") {
            context("generateBDD") {
                every { bddService.generateBDD(any()) } returns expectedResource

                val actual = sut.generateBDD(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expectedResource
                }
            }


            context("simulateBDD") {
                every { bddService.simulateBDD(any()) } returns expectedInvoice[0]

                val actual = sut.simulateBDD(YEAR_MONTH.toString())

                it("should return the correct invoice") {
                    actual shouldBe expectedInvoice[0]
                }
            }
        }

        describe("PDF") {

            context("simulatePDFs") {
                every { pdfService.simulatePDFs(any()) } returns expectedInvoice

                val actual = sut.simulatePDFs(YEAR_MONTH.toString())

                it("should return the correct invoice") {
                    actual shouldBe expectedInvoice
                }
            }

            context("generatePDFs") {
                every { pdfService.generatePDFs(any()) } returns expectedResource

                val actual = sut.generatePDFs(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expectedResource
                }
            }

            context("generatePDF") {
                every { pdfService.generatePDF(any()) } returns expectedResource

                val actual = sut.generatePDF("F-1")

                it("should return the correct PaymentTypeInvoicesDTO") {
                    actual shouldBe expectedResource
                }
            }
        }

        describe("Email") {

            context("simulateEmails") {
                every { emailService.simulateEmails(any()) } returns expectedInvoice[0]

                val actual = sut.simulateEmails(YEAR_MONTH.toString())

                it("should return the correct invoice") {
                    actual shouldBe expectedInvoice[0]
                }
            }

            context("generateEmails") {
                every { emailService.generateEmails(any()) } returns expectedInvoice[0]

                val actual = sut.generateEmails(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expectedInvoice[0]
                }
            }

        }
    }

}