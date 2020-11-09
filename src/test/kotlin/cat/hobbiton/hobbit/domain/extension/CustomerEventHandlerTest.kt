package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.domain.Child
import cat.hobbiton.hobbit.domain.GroupType
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class CustomerEventHandlerTest : DescribeSpec() {

    init {
        val sut = CustomerEventHandler()

        describe("validate") {

            context("Customer") {

                context("Valid customer") {
                    sut.validate(testCustomer())

                    it("does not throw any error") {}
                }

                context("Customer without children") {
                    val executor = {
                        sut.validate(testCustomer().copy(children = listOf()))
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Customer must have al least one child"
                    }
                }

                context("Customer without adult") {
                    val executor = {
                        sut.validate(testCustomer().copy(adults = listOf()))
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Customer must have al least one adult"
                    }
                }
            }

            context("Child") {

                context("Child with blank name") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                children = listOf(
                                        Child(
                                                code = 1,
                                                name = "",
                                                surname = "Llull Bibiloni",
                                                birthDate = DATE
                                        )
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child name cannot be blank"
                    }
                }

                context("Child with blank surname") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                children = listOf(
                                        Child(
                                                code = 1,
                                                name = "Aina",
                                                surname = "",
                                                birthDate = DATE
                                        )
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child surname cannot be blank"
                    }
                }

                context("Child with invalid tax id") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                children = listOf(
                                        Child(
                                                code = 1,
                                                name = "Aina",
                                                surname = "Llull Bibiloni",
                                                birthDate = DATE,
                                                taxId = "01960447X"
                                        )
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child tax id 01960447X is not valid"
                    }
                }

                context("Child with invalid group") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                children = listOf(
                                        Child(
                                                code = 1,
                                                name = "Aina",
                                                surname = "Llull Bibiloni",
                                                birthDate = DATE,
                                                group = GroupType.UNDEFINED
                                        )
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Child group must be defined"
                    }
                }
            }

            context("Adult") {

                context("Adult with blank name") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                adults = listOf(
                                        testAdultFather().copy(
                                                name = ""
                                        )
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Adult name cannot be blank"
                    }
                }

                context("Adult with blank surname") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                adults = listOf(
                                        testAdultFather().copy(
                                                surname = ""
                                        )
                                )
                        ))
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Adult surname cannot be blank"
                    }
                }

                context("Adult with invalid tax id") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                adults = listOf(
                                        testAdultFather().copy(
                                                taxId = "01960447X"
                                        )
                                )
                        ))
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Adult tax id 01960447X is not valid"
                    }
                }

                context("Adult with invalid address") {

                    context("blank street") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    adults = listOf(
                                            testAdultFather().copy(
                                                    address = testAddress().copy(street = "")
                                            )
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address street cannot be blank"
                        }
                    }

                    context("blank zip code") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    adults = listOf(
                                            testAdultFather().copy(
                                                    address = testAddress().copy(zipCode = "")
                                            )
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address zip code cannot be blank"
                        }
                    }

                    context("blank city") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    adults = listOf(
                                            testAdultFather().copy(
                                                    address = testAddress().copy(city = "")
                                            )
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address city cannot be blank"
                        }
                    }

                    context("blank state") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    adults = listOf(
                                            testAdultFather().copy(
                                                    address = testAddress().copy(state = "")
                                            )
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address state cannot be blank"
                        }
                    }
                }
            }

            context("InvoiceHolder") {

                context("InvoiceHolder with blank name") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                invoiceHolder = testInvoiceHolder().copy(
                                        name = ""
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice holder name cannot be blank"
                    }
                }

                context("InvoiceHolder with invalid tax id") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                invoiceHolder = testInvoiceHolder().copy(
                                        taxId = "01960447X"
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice holder tax id 01960447X is not valid"
                    }
                }

                context("InvoiceHolder with invalid address") {

                    context("blank street") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    invoiceHolder = testInvoiceHolder().copy(
                                            address = testAddress().copy(street = "")
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address street cannot be blank"
                        }
                    }

                    context("blank zip code") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    invoiceHolder = testInvoiceHolder().copy(
                                            address = testAddress().copy(zipCode = "")
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address zip code cannot be blank"
                        }
                    }

                    context("blank city") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    invoiceHolder = testInvoiceHolder().copy(
                                            address = testAddress().copy(city = "")
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address city cannot be blank"
                        }
                    }

                    context("blank state") {
                        val executor = {
                            sut.validate(testCustomer().copy(
                                    adults = listOf(
                                            testAdultFather().copy(
                                                    address = testAddress().copy(state = "")
                                            )
                                    )
                            ))
                        }

                        it("throws an error") {
                            val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                            exception.message shouldBe "Address state cannot be blank"
                        }
                    }
                }

                context("InvoiceHolder with invalid email") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                invoiceHolder = testInvoiceHolder().copy(
                                        email = "zzzz@ssss"
                                )
                        )
                        )
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Invoice holder email zzzz@ssss is not valid"
                    }
                }

                context("InvoiceHolder with invalid bank account") {
                    val executor = {
                        sut.validate(testCustomer().copy(
                                invoiceHolder = testInvoiceHolder().copy(
                                        bankAccount = "1234567890"
                                )
                        )
                        )
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