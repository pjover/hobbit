package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.ChildrenGroupDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.domain.Adult
import cat.hobbiton.hobbit.domain.AdultRole
import cat.hobbiton.hobbit.domain.GroupType
import cat.hobbiton.hobbit.testChild1
import cat.hobbiton.hobbit.testChild3
import cat.hobbiton.hobbit.testCustomer
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
                    testCustomer(children = listOf(testChild3())),
                    testCustomer().copy(children = listOf(testChild3().copy(active = false)))
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
                    ).copy(adults = listOf(Adult(
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
    }

}