package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.domain.Customer
import cat.hobbiton.hobbit.domain.Sequence
import cat.hobbiton.hobbit.domain.SequenceType
import cat.hobbiton.hobbit.testChild1
import cat.hobbiton.hobbit.testChild2
import cat.hobbiton.hobbit.testCustomer
import io.kotlintest.IsolationMode
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.mongodb.core.MongoTemplate
import java.util.*

class CustomizedCustomerRepositoryImplTest : DescribeSpec() {

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    init {
        val sequenceRepository = mockk<SequenceRepository>()
        val mongoTemplate = mockk<MongoTemplate>()
        val sut = CustomizedCustomerRepositoryImpl(sequenceRepository, mongoTemplate)

        describe("save") {

            every { sequenceRepository.save(any()) } answers { firstArg() }
            every { mongoTemplate.insert(any<Customer>()) } answers { firstArg() }
            every { sequenceRepository.findById(SequenceType.CUSTOMER) } returns Optional.of(Sequence(SequenceType.CUSTOMER, 999))

            context("completes the children codes") {

                context("updates the new children codes") {

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

                context("preserves the old children codes") {

                    val actual = sut.save(testCustomer())

                    it("replaces the children codes") {
                        actual.children[0].code shouldBe 1
                        actual.children[1].code shouldBe 2
                    }
                }

            }

            context("increments the customer id") {

                val actual = sut.save(testCustomer(id = 0))

                it("updates the sequence") {
                    actual.id shouldBe 1000
                }

                it("call collaborators") {
                    verify(exactly = 1) {
                        sequenceRepository.findById(SequenceType.CUSTOMER)
                        sequenceRepository.save(Sequence(SequenceType.CUSTOMER, 1000))
                    }
                }
            }

            context("saves the customer") {

                sut.save(testCustomer())

                it("call collaborators") {
                    verify(exactly = 1) {
                        mongoTemplate.insert(any<Customer>())
                    }
                }
            }
        }
    }
}