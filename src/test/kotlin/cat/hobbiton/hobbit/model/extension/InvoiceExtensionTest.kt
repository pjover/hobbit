package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.testInvoice
import cat.hobbiton.hobbit.testInvoices
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
                    testInvoice().copy(childrenCodes = emptyList()).validate(2500, "€")
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice children codes cannot be blank"
                }
            }

            context("without lines") {

                val executor = {
                    testInvoice().copy(lines = emptyList()).validate(2500, "€")
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
                                productName = "XXX product",
                                units = 2000.toBigDecimal(),
                                productPrice = BigDecimal.ONE,
                                childCode = 1),
                            InvoiceLine(
                                productId = "XXX",
                                productName = "XXX product",
                                units = 500.toBigDecimal(),
                                productPrice = BigDecimal.ONE,
                                childCode = 2)
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
                                productName = "XXX product",
                                units = BigDecimal.ZERO,
                                productPrice = BigDecimal.ONE,
                                childCode = 1)
                        )
                    ).validate(2500, "€")
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice line product units cannot be zero"
                }
            }


        }

        describe("List of Invoices") {
            val invoices = testInvoices()

            context("getGrossAmount()") {

                val actual = invoices.grossAmount()

                it("returns") {
                    actual shouldBe BigDecimal("140.0")
                }
            }

            context("getTaxAmount()") {

                val actual = invoices.taxAmount()

                it("returns") {
                    actual shouldBe BigDecimal("6.60")
                }
            }

            context("getTotalAmount()") {

                val actual = invoices.totalAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("146.60")
                }
            }
        }

        describe("childrenNames") {
            val customer = testCustomer(id = 185)
            val invoice = testInvoice()

            val actual = invoice.childrenNames(customer)

            it("returns the correct children text") {
                actual shouldBe "Laura, Aina"
            }

        }
    }
}
