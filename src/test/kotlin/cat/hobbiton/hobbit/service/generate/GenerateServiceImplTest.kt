package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.YEAR
import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.service.billing.expectedInvoices
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import cat.hobbiton.hobbit.service.generate.email.EmailService
import cat.hobbiton.hobbit.service.generate.pdf.PdfService
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheetService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.core.io.InputStreamResource
import java.nio.charset.StandardCharsets

class GenerateServiceImplTest : DescribeSpec() {

    init {
        val bddService = mockk<BddService>()
        val pdfService = mockk<PdfService>()
        val emailService = mockk<EmailService>()
        val spreadsheetService = mockk<SpreadSheetService>()
        val sut = GenerateServiceImpl(bddService, pdfService, emailService, spreadsheetService)

        val expectedResource = InputStreamResource("Test resource".byteInputStream(StandardCharsets.UTF_8))
        val expectedInvoice = expectedInvoices("??")

        describe("BDD") {
            context("generateBDD") {
                every { bddService.generateBDD(any()) } returns expectedResource

                val actual = sut.generateBDD(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expectedResource
                }

                it("call the collaborator") {
                    verify {
                        bddService.generateBDD(YEAR_MONTH.toString())
                    }
                }
            }


            context("simulateBDD") {
                every { bddService.simulateBDD(any()) } returns expectedInvoice[0]

                val actual = sut.simulateBDD(YEAR_MONTH.toString())

                it("should return the correct invoice") {
                    actual shouldBe expectedInvoice[0]
                }

                it("call the collaborator") {
                    verify {
                        bddService.simulateBDD(YEAR_MONTH.toString())
                    }
                }
            }
        }

        describe("PDF") {

            context("generatePDFs") {
                every { pdfService.generatePDFs(any(), any()) } returns expectedResource

                val actual = sut.generatePDFs(YEAR_MONTH.toString(), true)

                it("should return the correct resource") {
                    actual shouldBe expectedResource
                }

                it("call the collaborator") {
                    verify {
                        pdfService.generatePDFs(YEAR_MONTH.toString(), true)
                    }
                }
            }

            context("generatePDF") {
                every { pdfService.generatePDF(any()) } returns expectedResource

                val actual = sut.generatePDF("F-1")

                it("should return the correct PaymentTypeInvoicesDTO") {
                    actual shouldBe expectedResource
                }

                it("call the collaborator") {
                    verify {
                        pdfService.generatePDF("F-1")
                    }
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

                it("call the collaborator") {
                    verify {
                        emailService.simulateEmails(YEAR_MONTH.toString())
                    }
                }
            }

            context("generateEmails") {
                every { emailService.generateEmails(any()) } returns expectedInvoice[0]

                val actual = sut.generateEmails(YEAR_MONTH.toString())

                it("should return the correct resource") {
                    actual shouldBe expectedInvoice[0]
                }

                it("call the collaborator") {
                    verify {
                        emailService.generateEmails(YEAR_MONTH.toString())
                    }
                }
            }
        }

        describe("Month SpreadSheet") {
            every { spreadsheetService.generateMonthSpreadSheet(any()) } returns expectedResource

            val actual = sut.generateMonthSpreadSheet(YEAR_MONTH.toString())

            it("should return the correct resource") {
                actual shouldBe expectedResource
            }

            it("call the collaborator") {
                verify {
                    spreadsheetService.generateMonthSpreadSheet(YEAR_MONTH.toString())
                }
            }
        }

        describe("Year SpreadSheet") {
            every { spreadsheetService.generateYearSpreadSheet(any()) } returns expectedResource

            val actual = sut.generateYearSpreadSheet(YEAR)

            it("should return the correct resource") {
                actual shouldBe expectedResource
            }

            it("call the collaborator") {
                verify {
                    spreadsheetService.generateYearSpreadSheet(YEAR)
                }
            }
        }

        describe("Customers SpreadSheet") {

            context("generateCustomersSpreadSheet") {
                every { spreadsheetService.generateCustomersSpreadSheet() } returns expectedResource

                val actual = sut.generateCustomersSpreadSheet()

                it("should return the correct resource") {
                    actual shouldBe expectedResource
                }

                it("call the collaborator") {
                    verify {
                        spreadsheetService.generateCustomersSpreadSheet()
                    }
                }
            }
        }
    }
}