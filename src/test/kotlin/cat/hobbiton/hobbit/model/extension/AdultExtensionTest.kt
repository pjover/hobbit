package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.testAddress
import cat.hobbiton.hobbit.testAdultMother
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith


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

        describe("validate") {

            context("valid") {

                sut.validate()

                it("should not throw any error") {}
            }

            context("not valid") {

                context("Adult with blank name") {
                    val executor = {
                        sut.copy(name = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Adult name cannot be blank"
                    }
                }

                context("Adult with blank surname") {
                    val executor = {
                        sut.copy(surname = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Adult surname cannot be blank"
                    }
                }

                context("Adult with invalid tax id") {
                    val executor = {
                        sut.copy(taxId = "01960447X").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Adult tax id 01960447X is not valid"
                    }
                }

                context("Adult with invalid address") {

                    val executor = {
                        sut.copy(address = testAddress().copy(street = "")).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Address street cannot be blank"
                    }
                }
            }
        }
    }
}
