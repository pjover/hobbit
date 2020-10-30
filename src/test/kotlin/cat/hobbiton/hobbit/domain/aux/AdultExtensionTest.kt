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

            context("formattedText") {

                context("with only surname") {

                    val actual = sut.copy(secondSurname = "").formattedText()

                    it("returns the correct text") {
                        actual shouldBe "[M] Joana Bibiloni"
                    }
                }

                context("with email") {

                    val actual = sut.copy(email = "jbibiloni@gmail.com").formattedText()

                    it("returns the correct text") {
                        actual shouldBe "[M] Joana Bibiloni <jbibiloni@gmail.com>"
                    }
                }

                context("with mobile phone") {

                    val actual = sut.copy(mobilePhone = "644 424 578").formattedText()

                    it("returns the correct text") {
                        actual shouldBe "[M] Joana Bibiloni (644 424 578)"
                    }
                }
                context("with mobile phone and email") {

                    val actual = sut.copy(mobilePhone = "644 424 578", email = "jbibiloni@gmail.com").formattedText()

                    it("returns the correct text") {
                        actual shouldBe "[M] Joana Bibiloni (644 424 578) <jbibiloni@gmail.com>"
                    }
                }
            }
        }
    }
}
