package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Product
import org.springframework.data.mongodb.repository.MongoRepository

interface ProductRepository : MongoRepository<Product, String>
