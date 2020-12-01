package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import cat.hobbiton.hobbit.util.Logging
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

@Component
class CustomizedCustomerRepositoryImpl(
    private val sequenceRepository: SequenceRepository,
    private val mongoTemplate: MongoTemplate
) : CustomizedCustomerRepository {

    private val logger by Logging()

    override fun save(customer: Customer): Customer {

        val withId = incrementId(customer)
        val withChildrenCodes = completeChildrenCodes(withId)
        return saveCustomer(withChildrenCodes)
    }

    private fun incrementId(customer: Customer): Customer {
        if(customer.id != 0) return customer

        val currSequence = sequenceRepository.findById(SequenceType.CUSTOMER).orElse(Sequence(SequenceType.CUSTOMER, 0))
        val newSequence = Sequence(SequenceType.CUSTOMER, currSequence.counter + 1)
        sequenceRepository.save(newSequence)
        logger.info("Updated customer sequence to ${newSequence.counter}")

        return customer.copy(id = newSequence.counter)
    }

    private fun completeChildrenCodes(customer: Customer): Customer {

        val currChildren = customer.children.sortedBy { it.birthDate }
        val newChildren = mutableListOf<Child>()
        for(i in currChildren.indices) {
            if(currChildren[i].code == 0) {
                val child = currChildren[i].copy(
                    code = customer.id * 10 + i
                )
                newChildren.add(child)
                logger.info("Updated child code ${child.name}: ${child.code}")
            } else {
                newChildren.add(currChildren[i])
            }
        }
        return customer.copy(children = newChildren)
    }

    private fun saveCustomer(customer: Customer) = mongoTemplate.insert(customer)
}