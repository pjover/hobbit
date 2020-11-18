package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.domain.Adult
import cat.hobbiton.hobbit.domain.AdultRole
import cat.hobbiton.hobbit.domain.GroupType
import cat.hobbiton.hobbit.testChild1
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.testInvoiceHolder
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class ListServiceImplTest : DescribeSpec() {

    init {
        val customerRepository = mockk<CustomerRepository>()
        val sut = ListServiceImpl(customerRepository)

        describe("getChildrenList") {
            every { customerRepository.findAll() } returns listOf(
                    testCustomer(),
                    testCustomer(
                            children = listOf(
                                    testChild3(),
                                    testChild1().copy(active = false)
                            )
                    ),
                    testCustomer().copy(
                            active = false,
                            children = listOf(testChild3()))
            )

            val expected = listOf(
                    ChildrenGroupDTO(
                            GroupType.EI_1.text,
                            listOf(
                                    ChildListDTO(1, "Laura Llull"),
                                    ChildListDTO(2, "Aina Llull")
                            )
                    ),
                    ChildrenGroupDTO(
                            GroupType.EI_2.text,
                            listOf(
                                    ChildListDTO(3, "Laia Llull")
                            )
                    )
            )

            val actual = sut.getChildrenList()

            it("returns the correct list") {
                actual shouldBe expected
            }
        }

        describe("getCustomersList") {

            every { customerRepository.findAll() } returns listOf(
                    testCustomer(),
                    testCustomer(
                            id = 186,
                            children = listOf(
                                    testChild3(),
                                    testChild1().copy(active = false)
                            ),
                    ).copy(
                            adults = listOf(Adult(
                                    name = "Xisca",
                                    surname = "Llull",
                                    role = AdultRole.MOTHER
                            ))),
                    testCustomer(id = 187).copy(active = false)
            )

            val expected = listOf(
                    CustomerListDTO(185, "Joana Bibiloni",
                            listOf(
                                    ChildListDTO(1, "Laura Llull"),
                                    ChildListDTO(2, "Aina Llull")
                            )
                    ),
                    CustomerListDTO(186, "Xisca Llull",
                            listOf(
                                    ChildListDTO(3, "Laia Llull")
                            )
                    )
            )

            val actual = sut.getCustomersList()

            it("returns the correct list") {
                actual shouldBe expected
            }

        }

        describe("getEmailsList") {
            every { customerRepository.findAll() } returns listOf(
                    testCustomer(),
                    testCustomer(
                            children = listOf(testChild3(), testChild1().copy(active = false)),
                            invoiceHolder = testInvoiceHolder().copy(email = "test@gmail.com")
                    ),
                    testCustomer().copy(active = false, children = listOf(testChild3()))
            )

            context("all groups") {

                val expected = EmailsGroupDTO(
                        group = GroupDTO.ALL,
                        emails = listOf(
                                "Joana Bibiloni Oliver <jbibiloni@gmail.com>",
                                "Joana Bibiloni Oliver <test@gmail.com>"
                        )
                )

                val actual = sut.getEmailsList(GroupDTO.ALL)

                it("returns the correct list") {
                    actual shouldBe expected
                }
            }

            context("one group") {
                assert(false)
            }
        }
    }
}