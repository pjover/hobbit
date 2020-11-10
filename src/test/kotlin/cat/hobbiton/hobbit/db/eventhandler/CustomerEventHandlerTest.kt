package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.testCustomer
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class CustomerEventHandlerTest : DescribeSpec() {

    init {
        val sut = CustomerEventHandler()

        describe("validate") {

            context("validation fails") {

                context("and the id is blank") {
                    val executor = {
                        sut.validate(testCustomer().copy(children = listOf()))
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Customer must have al least one child"
                    }
                }

                context("validation suceeds") {

                    sut.validate(testCustomer())

                    it("do not throws any error") {
                    }
                }
            }
        }
    }
}