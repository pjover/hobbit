package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.AdultRole
import cat.hobbiton.hobbit.util.error.AppException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlin.test.assertFailsWith

internal class CustomerExtensionTest : DescribeSpec() {

    init {

        val sut = testCustomer185

        describe("Extracting info") {

            context("getActiveChildrenCodes") {

                context("and one child is inactive") {

                    val actual = sut.copy(
                        children = listOf(testChild1850, testChild1851, testChild1860.copy(active = false)))
                            .getActiveChildrenCodes()

                    it("returns a text of the active children of the customer") {
                        actual shouldBe listOf(1850, 1851)
                    }
                }
            }

            context("getChild") {

                context("and the code exists") {

                    val actual = sut.getChild(1850)

                    it("returns the child") {
                        actual shouldBe testChild1850
                    }
                }

                context("and the code does not exists") {

                    val executor = {
                        sut.getChild(1852)
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<AppException> { executor.invoke() }
                        exception.errorMessage shouldBe ErrorMessages.ERROR_CHILD_NOT_FOUND
                    }

                }
            }

            context("getAdult") {

                context("and the role exists") {

                    val actual = sut.getAdult(AdultRole.MOTHER)

                    it("returns the adult") {
                        actual shouldBe testAdultMother185
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
                        actual shouldBe testAdultMother185
                    }
                }

                context("has father") {
                    val actual = sut.copy(adults = listOf(testAdultFather185)).getFirstAdult()

                    it("returns the adult") {
                        actual shouldBe testAdultFather185
                    }
                }

                context("has tutor") {
                    val actual = sut.copy(adults = listOf(testAdultTutor186)).getFirstAdult()

                    it("returns the adult") {
                        actual shouldBe testAdultTutor186
                    }
                }

            }
        }

        describe("lists") {

            context("getActiveChildren") {

                val actual = sut.getActiveChildren()

                it("return false") {
                    actual shouldBe testChildren185
                }
            }
        }

        describe("validate") {

            context("valid") {

                sut.validate()

                it("should not throw any error") {}

            }

            context("not valid") {

                context("no children") {

                    val executor = {
                        sut.copy(children = emptyList()).validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("no adults") {

                    val executor = {
                        sut.copy(adults = emptyList()).validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }
        }
    }
}
