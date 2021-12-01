package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.model.Product
import io.kotest.core.spec.style.DescribeSpec
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
                                "TST product",
                                BigDecimal.TEN
                            )
                        )
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("validation suceeds") {

                    sut.validate(
                        Product(
                            "AAA",
                            "Test",
                            "TST product",
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