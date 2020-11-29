package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Product

interface CachedProductRepository {
    fun getProduct(id: String): Product
}