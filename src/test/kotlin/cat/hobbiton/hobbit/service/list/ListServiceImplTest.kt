package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.ChildrenListDTO
import cat.hobbiton.hobbit.db.repository.CustomerRepository
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
                    testCustomer(children = listOf(testChild3()))
            )

            val expected = ChildrenListDTO(
                    children = listOf(
                            ChildListDTO(1, "Laura Llull"),
                            ChildListDTO(2, "Aina Llull"),
                            ChildListDTO(3, "Laia Llull")
                    )
            )

            val actual = sut.getChildrenList()

            it("returns the correct list") {
                actual shouldBe expected
            }

        }
    }

}