package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.domain.Customer
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerRepository : MongoRepository<Customer, Int>, CustomizedCustomerRepository
