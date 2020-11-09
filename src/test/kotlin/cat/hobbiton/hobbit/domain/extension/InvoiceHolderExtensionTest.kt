package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.testInvoiceHolder
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec


class InvoiceHolderExtensionTest : DescribeSpec() {

    init {

        val sut = testInvoiceHolder()

        describe("email") {

            context("emailText") {

                it("return the correct email format") {
                    sut.emailText() shouldBe "Joana Bibiloni Oliver <jbibiloni@gmail.com>"
                }
            }

            context("isEmailConfigured") {

                context("with a valid email") {

                    it("returns true") {
                        sut.isEmailConfigured() shouldBe true

                    }
                }

                context("with an ivalid email") {

                    it("returns false") {
                        sut.copy(email = "").isEmailConfigured() shouldBe false
                        sut.copy(email = null).isEmailConfigured() shouldBe false
                    }
                }
            }
        }
    }
}
