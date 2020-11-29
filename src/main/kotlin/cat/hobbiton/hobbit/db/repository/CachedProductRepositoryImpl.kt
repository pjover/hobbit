package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.util.AppException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CachedProductRepositoryImpl(
    private val productRepository: ProductRepository
) : CachedProductRepository {

    @Cacheable("products")
    override fun getProduct(id: String): Product {
        return productRepository.findById(id)
            .orElseThrow { AppException(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, id) }
    }
}