package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import io.kotest.core.spec.style.DescribeSpec
import kotlin.test.assertFailsWith

class SequenceEventHandlerTest : DescribeSpec() {

    init {
        val sut = SequenceEventHandler()

        describe("validate") {

            context("validation fails") {
                val executor = {
                    sut.validate(Sequence(
                            SequenceType.CUSTOMER,
                            -1
                    ))
                }

                it("throws an error") {
                    assertFailsWith<IllegalArgumentException> { executor.invoke() }
                }
            }

            context("validation suceeds") {

                sut.validate(Sequence(SequenceType.CUSTOMER, 22))

                it("do not throws any error") {
                }
            }
        }
    }
}