package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.domain.Product
import cat.hobbiton.hobbit.domain.aux.validate
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class ProductEventHandlerTest : DescribeSpec() {

    init {
        val sut = ProductEventHandler()

        describe("validate") {

            context("validation fails") {

                mockkStatic("cat.hobbiton.hobbit.domain.aux.ProductExtensionKt")
                every { any<Product>().validate() } throws (IllegalArgumentException("Fake error"))

                context("and the id is blank") {
                    val executor = {
                        sut.validate(
                                Product(
                                        "",
                                        "Test",
                                        "Test product",
                                        BigDecimal.TEN
                                )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Fake error"
                    }

                    unmockkStatic("cat.hobbiton.hobbit.domain.aux.ProductExtensionKt")
                }

                context("validation suceeds") {

                    mockkStatic("cat.hobbiton.hobbit.domain.aux.ProductExtensionKt")
                    every { any<Product>().validate() } just runs

                    sut.validate(
                            Product(
                                    "AAA",
                                    "Test",
                                    "Test product",
                                    BigDecimal.TEN
                            )
                    )

                    it("do not throws any error") {
                    }

                    unmockkStatic("cat.hobbiton.hobbit.domain.aux.ProductExtensionKt")
                }
            }
        }

    }
}