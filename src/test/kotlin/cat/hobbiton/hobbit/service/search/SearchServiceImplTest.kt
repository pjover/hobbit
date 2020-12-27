package cat.hobbiton.hobbit.service.search

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.testCustomers
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.mongodb.core.MongoTemplate

class SearchServiceImplTest : DescribeSpec() {

    init {
        val template = mockk<MongoTemplate>()
        val sut = SearchServiceImpl(template)

        describe("searchCustomer") {
            every { template.find(any(), Customer::class.java) } returns testCustomers

            val actual = sut.searchCustomer("search")

            it("returns the correct list") {
                actual shouldBe listOf(
                    CustomerListDTO(185, "Joana Bibiloni",
                        listOf(
                            ChildListDTO(1850, "Laura Llull"),
                            ChildListDTO(1851, "Aina Llull")
                        )
                    ),
                    CustomerListDTO(186, "Silvia Mayol",
                        listOf(
                            ChildListDTO(1860, "Laia Mayol")
                        )
                    ),
                    CustomerListDTO(187, "Cara Santamaria",
                        listOf(
                            ChildListDTO(1870, "Ona Santamaria")
                        )
                    ),
                    CustomerListDTO(188, "Andrew Brown",
                        listOf(
                            ChildListDTO(1880, "Nil Brown")
                        )
                    )
                )
            }
        }
    }
}