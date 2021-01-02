package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Customer
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface CustomerRepository : MongoRepository<Customer, Int>, CustomizedCustomerRepository {

    @Query(value = "{ 'children.code' : ?0 }")
    fun findByChildCode(code: Int): Customer?

    fun findAllByActiveTrue(): List<Customer>
}
