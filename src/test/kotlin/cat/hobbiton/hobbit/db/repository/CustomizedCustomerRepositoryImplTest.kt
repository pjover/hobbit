package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.testChild1
import cat.hobbiton.hobbit.testChild2
import cat.hobbiton.hobbit.testCustomer
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.mockk
import org.springframework.data.mongodb.core.MongoTemplate

class CustomizedCustomerRepositoryImplTest : DescribeSpec() {

    init {
        val mongoTemplate = mockk<MongoTemplate>()
        val sut = CustomizedCustomerRepositoryImpl(mongoTemplate)

        describe("save") {

            context("completes the children codes") {

                val actual = sut.save(testCustomer().copy(
                        children = listOf(
                                testChild1().copy(code = 0),
                                testChild2().copy(code = 0)
                        )
                ))

                it("replaces the children codes") {
                    actual.children[0].code shouldBe 1850
                    actual.children[1].code shouldBe 1851
                }
            }

            context("incrments the customer id") {

            }

            context("saves the customer") {

            }

        }
    }

}