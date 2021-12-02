package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.testCustomer188
import io.kotest.core.spec.style.DescribeSpec
import kotlin.test.assertFailsWith

class CustomerEventHandlerTest : DescribeSpec() {

    init {
        val sut = CustomerEventHandler()

        describe("validate") {

            context("validation fails") {

                context("and the id is blank") {
                    val executor = {
                        sut.validate(testCustomer188.copy(children = emptyList()))
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("validation suceeds") {

                    sut.validate(testCustomer188)

                    it("do not throws any error") {
                    }
                }
            }
        }
    }
}