package cat.hobbiton.hobbit.domain.extension

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

        describe("extractCounter") {

            it("extracts the correct counter") {
                "C-0".extractCounter() shouldBe 0
                "F-33".extractCounter() shouldBe 33
                "X-666".extractCounter() shouldBe 666
            }
        }
    }
}
