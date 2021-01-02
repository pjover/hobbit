package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.Customer

interface CachedCustomerRepository {
    fun getChild(childCode: Int): Child
    fun getCustomer(id: Int): Customer
    fun getCustomerByChildCode(childCode: Int): Customer
    fun getActiveCustomers(): List<Customer>
}