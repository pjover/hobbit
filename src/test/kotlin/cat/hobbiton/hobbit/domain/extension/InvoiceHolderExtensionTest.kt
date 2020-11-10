package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.testAddress
import cat.hobbiton.hobbit.testInvoiceHolder
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith


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
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice holder name cannot be blank"
                    }
                }

                context("InvoiceHolder with invalid tax id") {
                    val executor = {
                        sut.copy(taxId = "01960447X").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice holder tax id 01960447X is not valid"
                    }
                }

                context("InvoiceHolder with invalid address") {

                    context("blank street") {
                        val executor = {
                            sut.copy(address = testAddress().copy(street = "")).validate()
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address street cannot be blank"
                        }
                    }
                }

                context("InvoiceHolder with invalid email") {
                    val executor = {
                        sut.copy(email = "zzzz@ssss").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice holder email zzzz@ssss is not valid"
                    }
                }

                context("InvoiceHolder with invalid bank account") {
                    val executor = {
                        sut.copy(bankAccount = "1234567890").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice holder bank account 1234567890 is not valid"
                    }
                }
            }
        }
    }
}
