package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nMessage

enum class ValidationMessages(
        override val code: String
) : I18nMessage {

    ERROR_PRODUCT_ID_BLANK("error.product.id.blank"),
    ERROR_PRODUCT_ID_LENGTH("error.product.id.length"),
    ERROR_PRODUCT_ID_LOWER_CASE("error.product.id.lower_case"),
    ERROR_PRODUCT_NAME_BLANK("error.product.name.blank"),
    ERROR_PRODUCT_NAME_TOO_LONG("error.product.name.too_long"),
    ERROR_PRODUCT_SHORT_NAME_BLANK("error.product.short_name.blank"),
    ERROR_PRODUCT_SHORT_NAME_TOO_LONG("error.product.short_name.too_long"),
    ERROR_PRODUCT_PRICE_ZERO("error.product.price.zero"),
    ERROR_PRODUCT_TAX_PERCENTAGE_ZERO("error.product.tax_percentage.zero")
}