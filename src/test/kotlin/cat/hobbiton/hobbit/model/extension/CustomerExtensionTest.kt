package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.model.AdultRole
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

internal class CustomerExtensionTest : DescribeSpec() {

    init {

        val sut = testCustomer()

        describe("Extracting info") {

            context("getActiveChildrenCodes") {

                context("and one child is inactive") {

                    val actual = sut.copy(
                            children = testChildren3Inactive())
                            .getActiveChildrenCodes()

                    it("returns a text of the active children of the customer") {
                        actual shouldBe listOf(1, 2)
                    }
                }
            }

            context("getChild") {

                context("and the code exists") {

                    val actual = sut.getChild(1)

                    it("returns the child") {
                        actual shouldBe testChild1()
                    }
                }

                context("and the code does not exists") {

                    val actual = sut.getChild(3)

                    it("returns null") {
                        actual shouldBe null
                    }
                }
            }

            context("getAdult") {

                context("and the role exists") {

                    val actual = sut.getAdult(AdultRole.MOTHER)

                    it("returns the adult") {
                        actual shouldBe testAdultMother()
                    }
                }

                context("and the code does not exists") {

                    val actual = sut.getAdult(AdultRole.TUTOR)

                    it("returns null") {
                        actual shouldBe null
                    }
                }
            }

            context("getCustomersList") {

                context("has mother") {
                    val actual = sut.getFirstAdult()

                    it("returns the adult") {
                        actual shouldBe testAdultMother()
                    }
                }

                context("has father") {
                    val actual = sut.copy(adults = listOf(testAdultFather())).getFirstAdult()

                    it("returns the adult") {
                        actual shouldBe testAdultFather()
                    }
                }

                context("has tutor") {
                    val actual = sut.copy(adults = listOf(testAdultTutor())).getFirstAdult()

                    it("returns the adult") {
                        actual shouldBe testAdultTutor()
                    }
                }

            }
        }

        describe("lists") {

            context("getActiveChildren") {

                val actual = sut.getActiveChildren()

                it("return false") {
                    actual shouldBe testChildren2()
                }
            }
        }

        describe("validate") {

            context("valid") {

                sut.validate()

                it("should not throw any error") {}

            }

            context("not valid") {

                val executor = {
                    sut.copy(children = emptyList()).validate()
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Customer must have al least one child"
                }
            }
        }
    }
}
