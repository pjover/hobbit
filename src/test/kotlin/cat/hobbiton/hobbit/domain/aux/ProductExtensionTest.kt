package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.completeTestProduct
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class ProductExtensionTest : DescribeSpec() {

    init {

        describe("formattedText()") {

            val actual = completeTestProduct().formattedText()

            it("returns the formatted text describing the product") {
                actual shouldBe "[TST] Test product (10.90€)"
            }
        }
    }
}
