package cat.hobbiton.hobbit.service.search

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.model.Adult
import cat.hobbiton.hobbit.model.AdultRole
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.testChild1850
import cat.hobbiton.hobbit.testChild1851
import cat.hobbiton.hobbit.testChild1860
import cat.hobbiton.hobbit.testCustomer
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
            every { template.find(any(), Customer::class.java) } returns listOf(
                testCustomer(
                    children = listOf(testChild1850)
                ),
                testCustomer(
                    id = 186,
                    children = listOf(testChild1851),
                    adults = listOf(Adult(name = "Xisca", surname = "Llull", role = AdultRole.MOTHER))
                ),
                testCustomer(
                    id = 187,
                    children = listOf(testChild1860),
                    adults = listOf(Adult(name = "Antònia", surname = "Palmer", role = AdultRole.MOTHER))
                )
            )

            val actual = sut.searchCustomer("search")

            it("returns the correct list") {
                actual shouldBe listOf(
                    CustomerListDTO(185, "Joana Bibiloni",
                        listOf(
                            ChildListDTO(1850, "Laura Llull")
                        )
                    ),
                    CustomerListDTO(186, "Xisca Llull",
                        listOf(
                            ChildListDTO(1851, "Aina Llull")
                        )
                    ),
                    CustomerListDTO(187, "Antònia Palmer",
                        listOf(
                            ChildListDTO(1860, "Laia Llull")
                        )
                    )
                )
            }

        }
    }

}