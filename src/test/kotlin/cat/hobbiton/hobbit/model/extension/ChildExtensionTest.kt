package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.model.GroupType
import cat.hobbiton.hobbit.testChild1850
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.time.LocalDate
import kotlin.test.assertFailsWith


class ChildExtensionTest : DescribeSpec() {

    init {

        val sut = testChild1850

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

        describe("calculateGroup") {
            val thisYear = LocalDate.now().year

            context("EI_1") {
                val birthDate = LocalDate.of(thisYear, 2, 2)

                val actual = birthDate.calculateGroup()

                it("has to be in EI_1 group") {
                    actual shouldBe GroupType.EI_1
                }
            }

            context("EI_2") {
                val birthDate = LocalDate.of(thisYear - 1, 2, 2)

                val actual = birthDate.calculateGroup()

                it("has to be in EI_1 group") {
                    actual shouldBe GroupType.EI_2
                }
            }

            context("EI_3") {
                val birthDate = LocalDate.of(thisYear - 2, 2, 2)

                val actual = birthDate.calculateGroup()

                it("has to be in EI_1 group") {
                    actual shouldBe GroupType.EI_3
                }
            }

            context("UNDEFINED") {
                val birthDate = LocalDate.of(thisYear - 3, 2, 2)

                val actual = birthDate.calculateGroup()

                it("has to be in EI_1 group") {
                    actual shouldBe GroupType.UNDEFINED
                }
            }
        }
    }
}
