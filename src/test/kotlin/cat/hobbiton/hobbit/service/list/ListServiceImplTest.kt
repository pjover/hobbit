package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.model.Adult
import cat.hobbiton.hobbit.model.AdultRole
import cat.hobbiton.hobbit.model.GroupType
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
                    testCustomer(children = listOf(testChild3()))
                            .copy(active = false)
            )

            val actual = sut.getChildrenList()

            it("returns the correct list") {
                actual shouldBe listOf(
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
            }
        }

        describe("getCustomersList") {

            every { customerRepository.findAll() } returns listOf(
                    testCustomer(),
                    testCustomer(
                            id = 186,
                            children = listOf(testChild3(), testChild1().copy(active = false)),
                            adults = listOf(Adult(name = "Xisca", surname = "Llull", role = AdultRole.MOTHER))
                    ),
                    testCustomer(id = 187).copy(active = false)
            )

            val actual = sut.getCustomersList()

            it("returns the correct list") {
                actual shouldBe listOf(
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
            }

        }

        describe("getEmailsList") {

            context("all groups") {
                every { customerRepository.findAll() } returns listOf(
                        testCustomer(),
                        testCustomer(
                                children = listOf(testChild3(), testChild1().copy(active = false)),
                                invoiceHolder = testInvoiceHolder().copy(email = "test@gmail.com")
                        ),
                        testCustomer(
                                children = listOf(testChild3())
                        ).copy(active = false)
                )

                val actual = sut.getEmailsList(GroupDTO.ALL)

                it("returns the correct list") {
                    actual shouldBe EmailsGroupDTO(
                            group = GroupDTO.ALL,
                            emails = listOf(
                                    "Joana Bibiloni Oliver <jbibiloni@gmail.com>",
                                    "Joana Bibiloni Oliver <test@gmail.com>"
                            )
                    )
                }
            }

            context("one group") {
                every { customerRepository.findAll() } returns listOf(
                        testCustomer(),
                        testCustomer(
                                children = testChildren3Inactive(),
                                invoiceHolder = testInvoiceHolder().copy(email = "test@gmail.com")
                        ),
                        testCustomer(
                                children = listOf(testChild3()))
                                .copy(active = false)
                )

                val actual = sut.getEmailsList(GroupDTO.EI_1)

                it("returns the correct list") {
                    actual shouldBe EmailsGroupDTO(
                            group = GroupDTO.EI_1,
                            emails = listOf(
                                    "Joana Bibiloni Oliver <jbibiloni@gmail.com>",
                                    "Joana Bibiloni Oliver <test@gmail.com>"
                            )
                    )
                }
            }
        }
    }
}