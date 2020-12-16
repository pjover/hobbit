package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.db.repository.SequenceRepository
import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import io.kotlintest.IsolationMode
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.*

class SequenceServiceImplTest : DescribeSpec() {

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    init {
        val sequenceRepository = mockk<SequenceRepository>()
        val sut = SequenceServiceImpl(sequenceRepository)

        val slot = slot<Sequence>()

        every { sequenceRepository.findById(SequenceType.CUSTOMER) } returns
            Optional.of(Sequence(SequenceType.CUSTOMER, 2))
        every { sequenceRepository.save(capture(slot)) } answers { slot.captured }

        describe("increment()") {
            val actual = sut.increment(SequenceType.CUSTOMER)

            it("increments the sequence") {
                actual shouldBe Sequence(SequenceType.CUSTOMER, 3)
            }

            it("calls the repository") {
                verify {
                    sequenceRepository.findById(SequenceType.CUSTOMER)
                    sequenceRepository.save(eq(Sequence(SequenceType.CUSTOMER, 3)))
                }
            }
        }

        describe("decrement()") {
            val actual = sut.decrement(SequenceType.CUSTOMER)

            it("decrements the sequence") {
                actual shouldBe Sequence(SequenceType.CUSTOMER, 1)
            }

            it("calls the repository") {
                verify {
                    sequenceRepository.findById(SequenceType.CUSTOMER)
                    sequenceRepository.save(eq(Sequence(SequenceType.CUSTOMER, 1)))
                }
            }
        }
    }
}