package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.domain.Customer

interface CustomizedCustomerRepository {
    fun save(customer: Customer): Customer
}
