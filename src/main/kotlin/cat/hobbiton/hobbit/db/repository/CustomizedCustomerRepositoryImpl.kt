package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.domain.Child
import cat.hobbiton.hobbit.domain.Customer
import cat.hobbiton.hobbit.domain.Sequence
import cat.hobbiton.hobbit.domain.SequenceType
import cat.hobbiton.hobbit.util.Logging
import org.springframework.data.mongodb.core.MongoTemplate

class CustomizedCustomerRepositoryImpl(
        private val sequenceRepository: SequenceRepository,
        private val mongoTemplate: MongoTemplate
) : CustomizedCustomerRepository {


    private val logger by Logging()

    override fun save(customer: Customer): Customer {

        val withId = incrementId(customer)
        return completeChildrenCodes(withId)


//        mongoTemplate.insert(customer, "customer")
    }

    private fun incrementId(customer: Customer): Customer {
        if (customer.id != 0) return customer

        val currSequence = sequenceRepository.findById(SequenceType.CUSTOMER).orElse(Sequence(SequenceType.CUSTOMER, 0))
        val newSequence = Sequence(SequenceType.CUSTOMER, currSequence.counter + 1)
        sequenceRepository.save(newSequence)
        logger.debug("Updated customer sequence to ${newSequence.counter}")

        return customer.copy(id = newSequence.counter)
    }

    private fun completeChildrenCodes(customer: Customer): Customer {

        val currChildren = customer.children.sortedBy { it.birthDate }
        val newChildren = mutableListOf<Child>()
        for (i in currChildren.indices) {
            if (currChildren[i].code == 0) {
                newChildren.add(currChildren[i].copy(
                        code = customer.id * 10 + i
                ))
            } else {
                newChildren.add(currChildren[i])
            }
        }
        return customer.copy(children = newChildren)
    }
}