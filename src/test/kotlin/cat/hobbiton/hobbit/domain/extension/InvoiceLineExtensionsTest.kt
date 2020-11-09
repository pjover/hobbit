package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.testInvoiceLines
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal

internal class InvoiceLineExtensionsTest : DescribeSpec() {

    init {

        describe("Invoice lines tests") {

            context("grossAmount()") {

                val actual = testInvoiceLines().first().grossAmount()

                it("return the correct amount") {
                    actual shouldBe BigDecimal.valueOf(11)
                }
            }

            context("taxAmount()") {

                val actual = testInvoiceLines().first().taxAmount()

                it("return the correct amount") {
                    actual shouldBe BigDecimal.ZERO
                }
            }

            context("totalAmount()") {

                val actual = testInvoiceLines().first().totalAmount()

                it("return the correct amount") {
                    actual shouldBe BigDecimal.valueOf(11)
                }
            }
        }

        describe("List of Invoices lines tests") {

            context("getGrossAmount()") {

                val actual = testInvoiceLines().grossAmount()

                it("returns  the correct amount") {
                    actual shouldBe BigDecimal("35.0")
                }
            }

            context("getTaxAmount()") {

                val actual = testInvoiceLines().taxAmount()

                it("returns  the correct amount") {
                    actual shouldBe BigDecimal("1.65")
                }
            }

            context("totalAmount()") {

                val actual = testInvoiceLines().totalAmount()

                it("returns  the correct amount") {
                    actual shouldBe BigDecimal("36.65")
                }
            }
        }
    }
}
