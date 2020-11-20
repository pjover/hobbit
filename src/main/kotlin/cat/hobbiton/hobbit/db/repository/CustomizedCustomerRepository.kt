package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Customer

interface CustomizedCustomerRepository {
    fun save(customer: Customer): Customer
}
