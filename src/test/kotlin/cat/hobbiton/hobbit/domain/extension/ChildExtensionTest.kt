package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.GroupType
import cat.hobbiton.hobbit.testChildren1
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith


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

        describe("validate") {

            context("valid") {

                sut.validate()

                it("should not throw any error") {}

            }

            context("not valid") {


                context("Child with blank name") {
                    val executor = {
                        sut.copy(name = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child name cannot be blank"
                    }
                }

                context("Child with blank surname") {
                    val executor = {
                        sut.copy(surname = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child surname cannot be blank"
                    }
                }

                context("Child with invalid tax id") {
                    val executor = {
                        sut.copy(taxId = "01960447X").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child tax id 01960447X is not valid"
                    }
                }

                context("Child with invalid group") {
                    val executor = {
                        sut.copy(group = GroupType.UNDEFINED).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child group must be defined"
                    }
                }
            }
        }
    }
}
