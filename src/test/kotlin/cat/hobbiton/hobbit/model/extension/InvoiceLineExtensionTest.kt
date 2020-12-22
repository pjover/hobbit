package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.testInvoiceLines
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import kotlin.test.assertFailsWith

internal class InvoiceLineExtensionTest : DescribeSpec() {

    init {

        describe("Invoice lines amounts") {

            context("grossAmount()") {

                val actual = testInvoiceLines().first().grossAmount()

                it("return the correct amount") {
                    actual shouldBe 11.toBigDecimal()
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
                    actual shouldBe 11.toBigDecimal()
                }
            }
        }

        describe("validate Invoice Line") {
            context("with blank id") {

                val executor = {
                    InvoiceLine(productId = "",
                        units = 2000.toBigDecimal(),
                        productPrice = BigDecimal.ONE,
                        childCode = 1)
                        .validate()
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice line must inform a product id"
                }
            }

            context("with zero units") {

                val executor = {
                    InvoiceLine(productId = "XXX",
                        units = BigDecimal.ZERO,
                        productPrice = BigDecimal.ONE,
                        childCode = 1)
                        .validate()
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice line product units cannot be zero"
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
