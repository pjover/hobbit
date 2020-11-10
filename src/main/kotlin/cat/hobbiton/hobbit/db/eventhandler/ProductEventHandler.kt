package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.domain.Product
import cat.hobbiton.hobbit.domain.extension.validate
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler(Product::class)
class ProductEventHandler {

    @HandleBeforeSave
    @HandleBeforeCreate
    fun validate(product: Product) = product.validate()
}