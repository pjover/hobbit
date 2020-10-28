package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Sequence
import cat.hobbiton.hobbit.domain.SequenceType
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class SequenceExtensionTest : DescribeSpec() {

    init {
        describe("formattedText") {

            val sequence = Sequence(SequenceType.CUSTOMER, 99)

            val actual = sequence.formattedText()

            it("returns the formatted text describing the sequence") {
                actual shouldBe "C-99"
            }
        }
    }
}
