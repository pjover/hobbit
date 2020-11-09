package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Product
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate
import java.math.BigDecimal

private const val ID_MAX_LENGTH = 3
private const val NAME_MAX_LENGTH = 150
private const val SHORT_NAME_MAX_LENGTH = 12

fun Product.validate() {

    require(id.isNotBlank()) { ValidationMessages.ERROR_PRODUCT_ID_BLANK.translate() }
    require(id.length == ID_MAX_LENGTH) { ValidationMessages.ERROR_PRODUCT_ID_LENGTH.translate(ID_MAX_LENGTH) }

    require(id == id.toUpperCase()) { ValidationMessages.ERROR_PRODUCT_ID_LOWER_CASE.translate() }

    require(name.isNotBlank()) { ValidationMessages.ERROR_PRODUCT_NAME_BLANK.translate() }
    require(name.length <= NAME_MAX_LENGTH) { ValidationMessages.ERROR_PRODUCT_NAME_TOO_LONG.translate(NAME_MAX_LENGTH) }

    require(shortName.isNotBlank()) { ValidationMessages.ERROR_PRODUCT_SHORT_NAME_BLANK.translate() }
    require(shortName.length <= SHORT_NAME_MAX_LENGTH) { ValidationMessages.ERROR_PRODUCT_SHORT_NAME_TOO_LONG.translate(SHORT_NAME_MAX_LENGTH) }

    require(price != BigDecimal.ZERO) { ValidationMessages.ERROR_PRODUCT_PRICE_ZERO.translate() }
    require(taxPercentage >= BigDecimal.ZERO) { ValidationMessages.ERROR_PRODUCT_TAX_PERCENTAGE_ZERO.translate() }

}