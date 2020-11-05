package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.InvoiceLine
import cat.hobbiton.hobbit.testInvoice
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class InvoiceEventHandlerTest : DescribeSpec() {

    init {
        val maxAmount = 2500
        val currency = "€"
        val sut = InvoiceEventHandler(maxAmount, currency)

        describe("Invoice validate") {

            context("without children codes") {

                val executor = {
                    sut.validate(
                            testInvoice().copy(
                                    childrenCodes = listOf()
                            )
                    )
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice children codes cannot be blank"
                }
            }

            context("without lines") {

                val executor = {
                    sut.validate(
                            testInvoice().copy(
                                    lines = listOf()
                            )
                    )
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice lines cannot be blank"
                }
            }

            context("with amount too high") {

                val executor = {
                    sut.validate(
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
                            )
                    )
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice amount has bo be lesser than 2,500 €"
                }
            }

            context("with invalid lines") {
                context("with blank id") {

                    val executor = {
                        sut.validate(
                                testInvoice().copy(
                                        lines = listOf(
                                                InvoiceLine(productId = "",
                                                        productName = "XXX name",
                                                        units = BigDecimal.valueOf(2000),
                                                        productPrice = BigDecimal.ONE)
                                        )
                                )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice line must inform a product id"
                    }
                }

                context("with zero units") {

                    val executor = {
                        sut.validate(
                                testInvoice().copy(
                                        lines = listOf(
                                                InvoiceLine(productId = "XXX",
                                                        productName = "XXX name",
                                                        units = BigDecimal.ZERO,
                                                        productPrice = BigDecimal.ONE)
                                        )
                                )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice line product units cannot be zero"
                    }
                }
            }
        }
    }
}