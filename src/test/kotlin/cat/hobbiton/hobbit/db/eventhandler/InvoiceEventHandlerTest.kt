package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.testInvoice185
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class InvoiceEventHandlerTest : DescribeSpec() {

    init {
        val maxAmount = 2500
        val currency = "€"
        val sut = InvoiceEventHandler(maxAmount, currency)

        describe("validate") {

            context("validation fails") {

                val executor = {
                    sut.validate(
                        testInvoice185.copy(
                            childrenCodes = emptyList()
                        )
                    )
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Invoice children codes cannot be blank"
                }
            }

            context("validation suceeds") {

                sut.validate(testInvoice185)

                it("do not throws any error") {
                }
            }
        }
    }
}