package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.testChild1
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.util.error.NotFoundException
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*
import kotlin.test.assertFailsWith

class CachedCustomerRepositoryImplTest : DescribeSpec() {

    init {
        val customerRepository = mockk<CustomerRepository>()
        val sut = CachedCustomerRepositoryImpl(customerRepository)

        describe("getChild") {
            clearMocks(customerRepository)

            context("existing customer") {
                every { customerRepository.findByChildCode(any()) } returns testCustomer()

                val actual = sut.getChild(1850)

                it("returns the customer") {
                    actual shouldBe testChild1()
                }

                it("calls the repository once") {
                    verify {
                        customerRepository.findByChildCode(1850)
                    }
                }
            }

            context("non existing customer") {
                every { customerRepository.findByChildCode(any()) } returns null

                val executor = {
                    sut.getChild(1850)
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.message shouldBe "Cannot find child with id 1,850"
                }
            }
        }

        describe("getCustomer") {
            clearMocks(customerRepository)

            context("existing customer") {
                every { customerRepository.findById(any()) } returns Optional.of(testCustomer())

                val actual = sut.getCustomer(11)

                it("returns the customer") {
                    actual shouldBe testCustomer()
                }

                it("calls the repository once") {
                    verify {
                        customerRepository.findById(11)
                    }
                }
            }

            context("non existing customer") {
                every { customerRepository.findById(any()) } returns Optional.empty()

                val executor = {
                    sut.getCustomer(11)
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.message shouldBe "Cannot find customer with id 11"
                }
            }
        }

        describe("getCustomerByChildCode") {
            clearMocks(customerRepository)

            context("existing customer") {
                every { customerRepository.findByChildCode(any()) } returns testCustomer()

                val actual = sut.getCustomerByChildCode(1)

                it("returns the customer") {
                    actual shouldBe testCustomer()
                }

                it("calls the repository once") {
                    verify {
                        customerRepository.findByChildCode(1)
                    }
                }
            }

            context("non existing customer") {
                every { customerRepository.findByChildCode(any()) } returns null

                val executor = {
                    sut.getCustomerByChildCode(1)
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.message shouldBe "Cannot find child with id 1"
                }
            }
        }
    }
}