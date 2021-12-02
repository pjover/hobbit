package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.testAddress
import cat.hobbiton.hobbit.testAdultMother185
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlin.test.assertFailsWith


class AdultExtensionTest : DescribeSpec() {

    init {

        val sut = testAdultMother185

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
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("Adult with blank surname") {
                    val executor = {
                        sut.copy(surname = "").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("Adult with invalid tax id") {
                    val executor = {
                        sut.copy(taxId = "01960447X").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("Adult with invalid address") {

                    val executor = {
                        sut.copy(address = testAddress().copy(street = "")).validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }
        }
    }
}
