package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.testAddress
import cat.hobbiton.hobbit.testInvoiceHolder185
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlin.test.assertFailsWith


class InvoiceHolderExtensionTest : DescribeSpec() {

    init {

        val sut = testInvoiceHolder185

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

        describe("validate") {

            context("valid") {

                sut.validate()

                it("should not throw any error") {}

            }

            context("not valid") {

                context("InvoiceHolder with blank name") {
                    val executor = {
                        sut.copy(name = "").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("InvoiceHolder with invalid tax id") {
                    val executor = {
                        sut.copy(taxId = "01960447X").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("InvoiceHolder with invalid address") {

                    context("blank street") {
                        val executor = {
                            sut.copy(address = testAddress().copy(street = "")).validate()
                        }

                        it("throws an error") {
                            assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        }
                    }
                }

                context("InvoiceHolder with invalid email") {
                    val executor = {
                        sut.copy(email = "zzzz@ssss").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("InvoiceHolder with invalid bank account") {
                    val executor = {
                        sut.copy(bankAccount = "1234567890").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }
        }
    }
}
