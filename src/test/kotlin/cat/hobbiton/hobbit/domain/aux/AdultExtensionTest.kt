package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.testAdultMother
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec


class AdultExtensionTest : DescribeSpec() {

    init {

        val sut = testAdultMother()

        describe("Adult formatting tests") {

            context("completeName") {

                context("with only one surname") {

                    val actual = sut.copy(secondSurname = "").completeName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni"
                    }
                }

                context("with two surnames") {

                    val actual = sut.completeName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni Oliver"
                    }
                }
            }

            context("shortName") {

                context("with only one surname") {

                    val actual = sut.copy(secondSurname = "").shortName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni"
                    }
                }

                context("with two surnames") {

                    val actual = sut.shortName()

                    it("returns the correct text") {
                        actual shouldBe "Joana Bibiloni"
                    }
                }
            }
        }
    }
}
