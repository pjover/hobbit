package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Sequence
import cat.hobbiton.hobbit.domain.SequenceType
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class SequenceEventHandlerTest : DescribeSpec() {

    init {
        val sut = SequenceEventHandler()

        describe("validate") {
            val executor = {
                sut.validate(Sequence(
                        SequenceType.CUSTOMER,
                        -1
                )
                )
            }

            it("throws an error") {
                val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                exception.message shouldBe "Sequence counter has to be greater than zero"
            }
        }
    }
}