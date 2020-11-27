package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.model.extension.validate
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