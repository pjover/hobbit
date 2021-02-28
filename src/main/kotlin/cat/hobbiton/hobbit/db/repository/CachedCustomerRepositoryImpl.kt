package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.extension.getChild
import cat.hobbiton.hobbit.util.error.NotFoundException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
open class CachedCustomerRepositoryImpl(
        private val customerRepository: CustomerRepository
) : CachedCustomerRepository {

    @Cacheable("childById")
    override fun getChild(childCode: Int): Child {
        val customer = getCustomerByChildCode(childCode)
        return customer.getChild(childCode)
    }

    @Cacheable("customerById")
    override fun getCustomer(id: Int): Customer {
        return customerRepository.findById(id)
            .orElseThrow { NotFoundException(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND, id) }
    }

    @Cacheable("customerByChild")
    override fun getCustomerByChildCode(childCode: Int): Customer {
        return customerRepository.findByChildCode(childCode)
            ?: throw NotFoundException(ErrorMessages.ERROR_CHILD_NOT_FOUND, childCode)
    }

    override fun getActiveCustomers(): List<Customer> {
        val customers = customerRepository.findAllByActiveTrue()
        if(customers.isEmpty()) throw NotFoundException(ErrorMessages.ERROR_CUSTOMERS_NOT_FOUND)
        return customers
    }
}