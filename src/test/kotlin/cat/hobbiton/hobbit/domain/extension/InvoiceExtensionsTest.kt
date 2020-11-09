package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.testInvoice
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal

class InvoiceExtensionsTest : DescribeSpec() {

    init {
        describe("Invoice tests") {
            val invoice = testInvoice()

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

        describe("List of Invoices tests")
        {
            val invoice = testInvoice()

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
    }
}
