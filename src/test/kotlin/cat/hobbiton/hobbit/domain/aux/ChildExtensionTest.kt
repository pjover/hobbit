package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.testChildren1
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec


class ChildExtensionTest : DescribeSpec() {

    init {

        val sut = testChildren1().first()

        describe("Child formatting tests") {

            context("completeName") {

                context("with only one surname") {

                    val actual = sut.completeName()

                    it("returns the correct text") {
                        actual shouldBe "Laura Llull Bibiloni"
                    }
                }

                context("with two surnames") {

                    val actual = sut.copy(secondSurname = "Valls").completeName()

                    it("returns the correct text") {
                        actual shouldBe "Laura Llull Valls"
                    }
                }
            }

            context("shortName") {

                context("with only one surname") {

                    val actual = sut.shortName()

                    it("returns the correct text") {
                        actual shouldBe "Laura Llull"
                    }
                }

                context("with two surnames") {

                    val actual = sut.copy(secondSurname = "Valls").shortName()

                    it("returns the correct text") {
                        actual shouldBe "Laura Llull"
                    }
                }
            }
        }
    }
}
