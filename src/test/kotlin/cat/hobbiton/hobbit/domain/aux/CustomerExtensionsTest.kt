package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.domain.AdultRole
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

internal class CustomerExtensionsTest : DescribeSpec() {

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
        }

        describe("Customer formatting tests") {

            context("formattedText()") {

                context("and the customer is active") {

                    val actual = sut.formattedText()

                    it("returns the formatted text describing the customer") {
                        actual shouldBe "[185] Laura, Aina : Joana Bibiloni"
                    }
                }

                context("and the customer is inactive") {

                    val actual = sut.copy(
                            active = false)
                            .formattedText()

                    it("returns the formatted text describing the customer") {
                        actual shouldBe "(185) Laura, Aina : Joana Bibiloni"
                    }
                }
            }

            context("activeChildrenText()") {

                context("and one child is inactive") {

                    val actual = sut.copy(
                            children = testChildren3Inactive())
                            .activeChildrenText()

                    it("returns a text of the active children of the customer") {
                        actual shouldBe "Laura, Aina"
                    }
                }
            }

            context("activeAndInactiveChildrenText()") {

                context("and one child is inactive") {

                    val actual = sut.copy(
                            children = testChildren3Inactive())
                            .activeAndInactiveChildrenText()

                    it("returns a text of the active and inactive children of the customer") {
                        actual shouldBe "Laura, Aina, (Laia)"
                    }
                }
            }

            context("getActiveChildrenNames()") {

                context("and one child is inactive") {

                    val actual = sut.copy(
                            children = testChildren3Inactive())
                            .getActiveChildrenNames()

                    it("returns a text of the active children names list of the customer") {
                        actual shouldBe listOf("Laura", "Aina")
                    }
                }
            }

            context("emailText()") {

                val actual = sut.emailText()

                it("returns the formatted email of the customer") {
                    actual shouldBe "Joana Bibiloni Oliver <jbibiloni@gmail.com>"
                }
            }

        }

        describe("list tests") {

            context("getActiveChildren") {

                val actual = sut.getActiveChildren()

                it("return false") {
                    actual shouldBe testChildren2()
                }
            }

            context("getActiveChildrenNames") {

                val actual = sut.getActiveChildrenNames()

                it("returns the active children list") {
                    actual shouldBe listOf("Laura", "Aina")
                }
            }

        }
    }
}
