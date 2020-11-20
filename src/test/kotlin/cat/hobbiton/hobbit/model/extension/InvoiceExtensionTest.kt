package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.testInvoice
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class InvoiceExtensionTest : DescribeSpec() {

    init {
        describe("Invoice amount tests") {
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

        describe("validate Invoice") {

            context("without children codes") {

                val executor = {
                    testInvoice().copy(childrenCodes = listOf()).validate(2500, "€")
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice children codes cannot be blank"
                }
            }

            context("without lines") {

                val executor = {
                    testInvoice().copy(lines = listOf()).validate(2500, "€")
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice lines cannot be blank"
                }
            }

            context("with amount too high") {

                val executor = {
                    testInvoice().copy(
                            lines = listOf(
                                    InvoiceLine(productId = "XXX",
                                            productName = "XXX name",
                                            units = BigDecimal.valueOf(2000),
                                            productPrice = BigDecimal.ONE),
                                    InvoiceLine(
                                            productId = "TSS",
                                            productName = "XXX name",
                                            units = BigDecimal.valueOf(500),
                                            productPrice = BigDecimal.ONE)
                            )
                    ).validate(2500, "€")
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice amount has bo be lesser than 2,500 €"
                }
            }

            context("with invalid line") {

                val executor = {
                    testInvoice().copy(
                            lines = listOf(
                                    InvoiceLine(productId = "XXX",
                                            productName = "XXX name",
                                            units = BigDecimal.ZERO,
                                            productPrice = BigDecimal.ONE)
                            )
                    ).validate(2500, "€")
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice line product units cannot be zero"
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
