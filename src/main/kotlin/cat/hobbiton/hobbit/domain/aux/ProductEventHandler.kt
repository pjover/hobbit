package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Product
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.english
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
@RepositoryEventHandler(Product::class)
class ProductEventHandler {

    companion object {
        private const val ID_MAX_LENGTH = 3
        private const val NAME_MAX_LENGTH = 150
        private const val SHORT_NAME_MAX_LENGTH = 12
    }

    @HandleBeforeSave
    @HandleBeforeCreate
    fun validate(p: Product) {

        require(p.id.isNotBlank()) { ValidationMessages.ERROR_PRODUCT_ID_BLANK.english() }
        require(p.id.length == ID_MAX_LENGTH) { ValidationMessages.ERROR_PRODUCT_ID_LENGTH.english(ID_MAX_LENGTH) }

        require(p.id == p.id.toUpperCase()) { ValidationMessages.ERROR_PRODUCT_ID_LOWER_CASE.english() }

        require(p.name.isNotBlank()) { ValidationMessages.ERROR_PRODUCT_NAME_BLANK.english() }
        require(p.name.length <= NAME_MAX_LENGTH) { ValidationMessages.ERROR_PRODUCT_NAME_TOO_LONG.english(NAME_MAX_LENGTH) }

        require(p.shortName.isNotBlank()) { ValidationMessages.ERROR_PRODUCT_SHORT_NAME_BLANK.english() }
        require(p.shortName.length <= SHORT_NAME_MAX_LENGTH) { ValidationMessages.ERROR_PRODUCT_SHORT_NAME_TOO_LONG.english(SHORT_NAME_MAX_LENGTH) }

        require(p.price != BigDecimal.ZERO) { ValidationMessages.ERROR_PRODUCT_PRICE_ZERO.english() }
        require(p.taxPercentage >= BigDecimal.ZERO) { ValidationMessages.ERROR_PRODUCT_TAX_PERCENTAGE_ZERO.english() }
    }
}