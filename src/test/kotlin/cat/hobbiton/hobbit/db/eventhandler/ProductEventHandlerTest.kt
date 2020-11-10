package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.domain.Product
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class ProductEventHandlerTest : DescribeSpec() {

    init {
        val sut = ProductEventHandler()

        describe("validate") {

            context("validation fails") {

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
                        exception.message shouldBe "Product id cannot be blank"
                    }
                }

                context("validation suceeds") {

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
                }
            }
        }
    }
}