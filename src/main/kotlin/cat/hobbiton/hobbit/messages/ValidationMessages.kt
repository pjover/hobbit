package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nMessage

enum class ValidationMessages(
        override val code: String
) : I18nMessage {

    ERROR_CUSTOMER_WHITOUT_CHILD("error.customer.without.child"),
    ERROR_CUSTOMER_WHITOUT_ADULT("error.customer.without.adult"),

    ERROR_CHILD_NAME_BLANK("error.child.name.blank"),
    ERROR_CHILD_SURNAME_BLANK("error.child.surname.blank"),
    ERROR_CHILD_TAX_ID_INVALID("error.child.tax_id.invalid"),
    ERROR_CHILD_GROUP_UNDEFINED("error.child.group.undefined"),

    ERROR_ADULT_NAME_BLANK("error.adult.name.blank"),
    ERROR_ADULT_SURNAME_BLANK("error.adult.surname.blank"),
    ERROR_ADULT_TAX_ID_INVALID("error.adult.tax_id.invalid"),
    ERROR_ADULT_EMAIL_INVALID("error.adult.email.invalid"),

    ERROR_ADDRESS_STREET_BLANK("error.address.street.blank"),
    ERROR_ADDRESS_ZIP_CODE_BLANK("error.address.zip_code.blank"),
    ERROR_ADDRESS_CITY_BLANK("error.address.city.blank"),
    ERROR_ADDRESS_STATE_BLANK("error.address.state.blank"),

    ERROR_INVOICE_HOLDER_NAME_BLANK("error.invoice_holder.name.blank"),
    ERROR_INVOICE_HOLDER_TAX_ID_INVALID("error.invoice_holder.tax_id.invalid"),
    ERROR_INVOICE_HOLDER_EMAIL_INVALID("error.invoice_holder.email.invalid"),
    ERROR_INVOICE_HOLDER_BANK_ACCOUNT_INVALID("error.invoice_holder.bank_account.invalid"),

    ERROR_PRODUCT_ID_BLANK("error.product.id.blank"),
    ERROR_PRODUCT_ID_LENGTH("error.product.id.length"),
    ERROR_PRODUCT_ID_LOWER_CASE("error.product.id.lower_case"),
    ERROR_PRODUCT_NAME_BLANK("error.product.name.blank"),
    ERROR_PRODUCT_NAME_TOO_LONG("error.product.name.too_long"),
    ERROR_PRODUCT_SHORT_NAME_BLANK("error.product.short_name.blank"),
    ERROR_PRODUCT_SHORT_NAME_TOO_LONG("error.product.short_name.too_long"),
    ERROR_PRODUCT_PRICE_ZERO("error.product.price.zero"),
    ERROR_PRODUCT_TAX_PERCENTAGE_ZERO("error.product.tax_percentage.zero"),

    ERROR_SEQUENCE_COUNTER_INVALID("error.sequence.counter.invalid"),

    ERROR_INVOICE_CHILDREN_CODES_EMPTY("error.invoice.children_codes.empty"),
    ERROR_INVOICE_LINES_EMPTY("error.invoice.lines.empty"),
    ERROR_INVOICE_AMOUNT_TOO_HIGH("error.invoice.amount.too_high"),
    ERROR_INVOICE_LINE_PRODUCT_ID_BLANK("error.invoice_line.product_id.blank"),
    ERROR_INVOICE_LINE_PRODUCT_UNITS_ZERO("error.invoice_line.product_units.zero"),

    ERROR_YEAR_MONTH_INVALID("error.year_month.invalid"),

    ERROR_PAYMENT_TYPE_CONFIGURATION_UNDEFINED("error.payment_type.configuration.undefined"),
    ERROR_DB_NAME_CONFIGURATION_UNDEFINED("error.db_name.configuration.undefined"),

    ERROR_LOGO_FILE_NOT_FOUND("error.logo_file.not_found")
}