package cat.hobbiton.hobbit.service.search

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.domain.Adult
import cat.hobbiton.hobbit.domain.AdultRole
import cat.hobbiton.hobbit.domain.Customer
import cat.hobbiton.hobbit.testChild1
import cat.hobbiton.hobbit.testChild2
import cat.hobbiton.hobbit.testChild3
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
                            children = listOf(testChild1())
                    ),
                    testCustomer(
                            id = 186,
                            children = listOf(testChild2()),
                            adults = listOf(Adult(name = "Xisca", surname = "Llull", role = AdultRole.MOTHER))
                    ),
                    testCustomer(
                            id = 187,
                            children = listOf(testChild3()),
                            adults = listOf(Adult(name = "Antònia", surname = "Palmer", role = AdultRole.MOTHER))
                    )
            )

            val expected = listOf(
                    CustomerListDTO(185, "Joana Bibiloni",
                            listOf(
                                    ChildListDTO(1, "Laura Llull")
                            )
                    ),
                    CustomerListDTO(186, "Xisca Llull",
                            listOf(
                                    ChildListDTO(2, "Aina Llull")
                            )
                    ),
                    CustomerListDTO(187, "Antònia Palmer",
                            listOf(
                                    ChildListDTO(3, "Laia Llull")
                            )
                    )
            )

            val actual = sut.searchCustomer("search")

            it("returns the correct list") {
                actual shouldBe expected
            }

        }
    }

}