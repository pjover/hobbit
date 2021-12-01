package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.model.GroupType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ListServiceImplTest : DescribeSpec() {

    init {
        val customerRepository = mockk<CustomerRepository>()
        val sut = ListServiceImpl(customerRepository)

        every { customerRepository.findAll() } returns listOf(
            testCustomer185,
            testCustomer186.copy(children = listOf(testChild1860, testChild1850.copy(active = false))),
            testCustomer187.copy(active = false),
            testCustomer188
        )

        describe("getChildrenList") {

            val actual = sut.getChildrenList()

            it("returns the correct list") {
                actual shouldBe listOf(
                    ChildrenGroupDTO(
                        GroupType.EI_1.text,
                        listOf(
                            ChildListDTO(1850, "Laura Llull"),
                            ChildListDTO(1851, "Aina Llull"),
                            ChildListDTO(1880, "Nil Brown")
                        )
                    ),
                    ChildrenGroupDTO(
                        GroupType.EI_2.text,
                        listOf(
                            ChildListDTO(1860, "Laia Mayol")
                        )
                    )
                )
            }
        }

        describe("getCustomersList") {

            val actual = sut.getCustomersList()

            it("returns the correct list") {
                actual shouldBe listOf(
                    CustomerListDTO(
                        185, "Joana Bibiloni",
                        listOf(
                            ChildListDTO(1850, "Laura Llull"),
                            ChildListDTO(1851, "Aina Llull")
                        )
                    ),
                    CustomerListDTO(
                        186, "Silvia Mayol",
                        listOf(
                            ChildListDTO(1860, "Laia Mayol")
                        )
                    ),
                    CustomerListDTO(
                        188, "Andrew Brown",
                        listOf(
                            ChildListDTO(1880, "Nil Brown")
                        )
                    )
                )
            }
        }

        describe("getEmailsList") {

            context("all groups") {

                val actual = sut.getEmailsList(GroupDTO.ALL)

                it("returns the correct list") {
                    actual shouldBe EmailsGroupDTO(
                        group = GroupDTO.ALL,
                        emails = "Andrew Brown <abrown@gmail.com>, Joana Bibiloni Oliver <jbibiloni@gmail.com>, Silvia Mayol <silvia@gmail.com>"
                    )
                }
            }

            context("one group") {

                val actual = sut.getEmailsList(GroupDTO.EI_1)

                it("returns the correct list") {
                    actual shouldBe EmailsGroupDTO(
                        group = GroupDTO.EI_1,
                        emails = "Andrew Brown <abrown@gmail.com>, Joana Bibiloni Oliver <jbibiloni@gmail.com>, Silvia Mayol <silvia@gmail.com>"
                    )
                }
            }
        }
    }
}