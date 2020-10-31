package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.PaymentType
import cat.hobbiton.hobbit.testInvoiceFtype
import cat.hobbiton.hobbit.testInvoices
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal

class InvoiceExtensionsTest : DescribeSpec() {

    init {
        describe("Invoice tests") {
            val invoice = testInvoiceFtype()

            context("grossAmount()") {

                val actual = invoice.grossAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("35.0")
                }
            }

            context("taxAmount()") {

                val actual = invoice.taxAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("1.65")

                }
            }

            context("totalAmount()") {

                val actual = invoice.totalAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("36.65")
                }
            }
        }

        describe("Invoice formatting tests") {
            val invoice = testInvoiceFtype()

            context("formattedTextWithoutKey()") {

                val actual = invoice.formattedTextWithoutId()

                it("returns the formatted text describing the invoice") {
                    actual shouldBe "2019-05 (36.65€) 1xAAA, 3xBBB, 1.5xCCC [148: 1800, 1801]"
                }
            }

            context("formattedTextWithKey()") {

                val actual = invoice.formattedTextWithId()

                it("returns the formatted text describing the invoice with the invoice key code") {
                    actual shouldBe "[F-103] 2019-05 (36.65€) 1xAAA, 3xBBB, 1.5xCCC [148: 1800, 1801]"
                }
            }

            context("getLinesText()") {

                val actual = invoice.getLinesText()

                it("returns the concatenated line's text") {
                    actual shouldBe "1xAAA, 3xBBB, 1.5xCCC"
                }
            }

            context("getChildrenText()") {

                context("with an invoice with only child") {
                    val invoice1 = testInvoiceFtype()
                    invoice1.childrenCodes = listOf(1800)

                    val actual = invoice1.getChildrenText()

                    it("returns the child code") {
                        actual shouldBe "1800"

                    }
                }

                context("with an invoice with two children") {

                    val actual = invoice.getChildrenText()

                    it("return the concatenated children code") {
                        actual shouldBe "1800, 1801"
                    }
                }
            }

            context("getChildrenTextWithLabel()") {

                context("with an invoice with on child") {
                    invoice.childrenCodes = listOf(1800)

                    val actual = invoice.getChildrenTextWithLabel()

                    it("returns ") {
                        actual shouldBe "Infant: 1800"

                    }
                }

                context("with an invoice with many children") {
                    invoice.childrenCodes = listOf(1800, 1801, 1802, 1803)

                    val actual = invoice.getChildrenTextWithLabel()

                    it("returns ") {
                        actual shouldBe "Infants: 1800, 1801, 1802, 1803"
                    }
                }
            }
        }

        describe("Invoice sequence extract") {

            context("idPrefix") {

                val actual = testInvoiceFtype().idPrefix()

                it("should extract the prefix from the id") {
                    actual shouldBe "F"
                }
            }

            context("idSequence") {

                val actual = testInvoiceFtype().idSequence()

                it("should extract the id sequence from the id") {
                    actual shouldBe 103
                }
            }

        }

        describe("List of Invoices tests")
        {
            val invoice = testInvoiceFtype()

            context("getGrossAmount()") {

                val actual = invoice.grossAmount()

                it("returns") {
                    actual shouldBe BigDecimal("35.0")
                }
            }

            context("getTaxAmount()") {

                val actual = invoice.taxAmount()

                it("returns") {
                    actual shouldBe BigDecimal("1.65")
                }
            }

            context("getTotalAmount()") {

                val actual = invoice.totalAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("36.65")
                }
            }
        }

        describe("List of Invoices formatting tests")
        {

            context("getSummary()") {
                val invoices = testInvoices()

                context("with all payment types") {
                    invoices[0].paymentType = PaymentType.CASH
                    invoices[1].paymentType = PaymentType.BANK_TRANSFER
                    invoices[2].paymentType = PaymentType.VOUCHER

                    val actual = invoices.getSummary("Carregades 4 factures")

                    it("returns the summary with sum of every PaymentType and a total amount") {
                        actual shouldBe "Carregades 4 factures\n" +
                                "  - 1 rebut (149): 36.65€\n" +
                                "  - 1 transferència (148): 36.65€\n" +
                                "  - 1 xec escoleta (149): 36.65€\n" +
                                "  - 1 en metàlic (148): 36.65€\n" +
                                "Total: 146.60€"
                    }

                }

                context("with different payment types except bank transfer") {
                    invoices[0].paymentType = PaymentType.CASH
                    invoices[2].paymentType = PaymentType.VOUCHER

                    val actual = invoices.getSummary("Carregades 4 factures")

                    it("return the summary, skipping the non existent payment types") {
                        actual shouldBe "Carregades 4 factures\n" +
                                "  - 1 rebut (149): 36.65€\n" +
                                "  - 1 transferència (148): 36.65€\n" +
                                "  - 1 xec escoleta (149): 36.65€\n" +
                                "  - 1 en metàlic (148): 36.65€\n" +
                                "Total: 146.60€"
                    }
                }
            }
        }
    }
}
