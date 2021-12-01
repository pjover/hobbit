package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.testCustomer185
import cat.hobbiton.hobbit.testInvoice185
import cat.hobbiton.hobbit.testInvoices
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class InvoiceExtensionTest : DescribeSpec() {

    init {
        describe("Invoice amount tests") {
            val invoice = testInvoice185

            context("grossAmount()") {

                val actual = invoice.grossAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("83.6")
                }
            }

            context("taxAmount()") {

                val actual = testInvoice185.copy(
                    lines = listOf(
                        InvoiceLine(
                            productId = "YYY",
                            units = 2.toBigDecimal(),
                            productPrice = 10.9.toBigDecimal(),
                            childCode = 1850,
                            taxPercentage = 1.6.toBigDecimal()
                        )
                    )
                ).taxAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("34.88")

                }
            }

            context("totalAmount()") {

                val actual = invoice.totalAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("83.6")
                }
            }
        }

        describe("validate Invoice") {

            context("without children codes") {

                val executor = {
                    testInvoice185.copy(childrenCodes = emptyList()).validate(2500, "€")
                }

                it("throws an error") {
                    assertFailsWith<IllegalArgumentException> { executor.invoke() }
                }
            }

            context("without lines") {

                val executor = {
                    testInvoice185.copy(lines = emptyList()).validate(2500, "€")
                }

                it("throws an error") {
                    assertFailsWith<IllegalArgumentException> { executor.invoke() }
                }
            }

            context("with amount too high") {

                val executor = {
                    testInvoice185.copy(
                        lines = listOf(
                            InvoiceLine(productId = "XXX",
                                units = 2000.toBigDecimal(),
                                productPrice = BigDecimal.ONE,
                                childCode = 1),
                            InvoiceLine(
                                productId = "XXX",
                                units = 500.toBigDecimal(),
                                productPrice = BigDecimal.ONE,
                                childCode = 2)
                        )
                    ).validate(2500, "€")
                }

                it("throws an error") {
                    assertFailsWith<IllegalArgumentException> { executor.invoke() }
                }
            }

            context("with invalid line") {

                val executor = {
                    testInvoice185.copy(
                        lines = listOf(
                            InvoiceLine(productId = "XXX",
                                units = BigDecimal.ZERO,
                                productPrice = BigDecimal.ONE,
                                childCode = 1)
                        )
                    ).validate(2500, "€")
                }

                it("throws an error") {
                    assertFailsWith<IllegalArgumentException> { executor.invoke() }
                }
            }


        }

        describe("List of Invoices") {

            context("getGrossAmount()") {

                val actual = testInvoices.grossAmount()

                it("returns") {
                    actual shouldBe BigDecimal("83.6")
                }
            }

            context("getTaxAmount()") {

                val actual = testInvoices.taxAmount()

                it("returns") {
                    actual shouldBe BigDecimal("0.0")
                }
            }

            context("getTotalAmount()") {

                val actual = testInvoices.totalAmount()

                it("returns the correct amount") {
                    actual shouldBe BigDecimal("83.6")
                }
            }
        }

        describe("childrenNames") {
            val customer = testCustomer185
            val invoice = testInvoice185

            val actual = invoice.childrenNames(customer)

            it("returns the correct children text") {
                actual shouldBe "Laura, Aina"
            }

        }
    }
}
