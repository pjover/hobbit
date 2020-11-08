package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.domain.Child
import cat.hobbiton.hobbit.domain.Customer
import cat.hobbiton.hobbit.util.Logging
import org.springframework.data.mongodb.core.MongoTemplate

class CustomizedCustomerRepositoryImpl(
        private val mongoTemplate: MongoTemplate
) : CustomizedCustomerRepository {


    private val logger by Logging()

    override fun save(customer: Customer): Customer {

        return completeChildrenCodes(customer)
//        mongoTemplate.insert(customer, "customer")
    }

    private fun completeChildrenCodes(customer: Customer): Customer {

        val currChildren = customer.children.sortedBy { it.birthDate }
        val newChildren = mutableListOf<Child>()
        for (i in currChildren.indices) {
            newChildren.add(currChildren[i].copy(
                    code = customer.id * 10 + i
            ))
        }
        return customer.copy(children = newChildren)
    }
}