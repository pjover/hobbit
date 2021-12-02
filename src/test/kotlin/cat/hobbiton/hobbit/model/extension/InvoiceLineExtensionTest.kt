package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.model.InvoiceLine
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import kotlin.test.assertFailsWith

internal class InvoiceLineExtensionTest : DescribeSpec() {

    init {
        val sut = listOf(
            InvoiceLine(productId = "AAA",
                units = 1.toBigDecimal(),
                productPrice = 11.toBigDecimal(),
                taxPercentage = BigDecimal.ZERO,
                childCode = 1850
            ),
            InvoiceLine(productId = "BBB",
                units = 3.toBigDecimal(),
                productPrice = 5.5.toBigDecimal(),
                taxPercentage = 0.1.toBigDecimal(),
                childCode = 1850
            ),
            InvoiceLine(productId = "CCC",
                units = 1.5.toBigDecimal(),
                productPrice = 5.toBigDecimal(),
                taxPercentage = BigDecimal.ZERO,
                childCode = 1851
            )
        )

        describe("Invoice lines amounts") {

            context("grossAmount()") {

                val actual = sut.first().grossAmount()

                it("return the correct amount") {
                    actual shouldBe 11.toBigDecimal()
                }
            }

            context("taxAmount()") {

                val actual = sut.first().taxAmount()

                it("return the correct amount") {
                    actual shouldBe BigDecimal.ZERO
                }
            }

            context("totalAmount()") {

                val actual = sut.first().totalAmount()

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
                    assertFailsWith<IllegalArgumentException> { executor.invoke() }
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
                    assertFailsWith<IllegalArgumentException> { executor.invoke() }
                }
            }
        }

        describe("List of Invoices lines tests") {

            context("getGrossAmount()") {

                val actual = sut.grossAmount()

                it("returns  the correct amount") {
                    actual shouldBe BigDecimal("35.0")
                }
            }

            context("getTaxAmount()") {

                val actual = sut.taxAmount()

                it("returns  the correct amount") {
                    actual shouldBe BigDecimal("1.65")
                }
            }

            context("totalAmount()") {

                val actual = sut.totalAmount()

                it("returns  the correct amount") {
                    actual shouldBe BigDecimal("36.65")
                }
            }
        }
    }
}


