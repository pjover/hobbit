package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.Adult
import cat.hobbiton.hobbit.util.translate

fun Adult.shortName(): String = "$name $surname"

fun Adult.completeName(): String {
    return if (secondSurname.isNullOrBlank()) shortName()
    else "${shortName()} $secondSurname"
}

fun Adult.validate() {

    require(name.isNotBlank()) {
        ValidationMessages.ERROR_ADULT_NAME_BLANK.translate()
    }

    require(surname.isNotBlank()) {
        ValidationMessages.ERROR_ADULT_SURNAME_BLANK.translate()
    }

    if (!taxId.isNullOrBlank()) require(taxId.isValidTaxId()) {
        ValidationMessages.ERROR_ADULT_TAX_ID_INVALID.translate(taxId)
    }

    address?.validate()

    if (email != null) require(email.isValidEmail()) {
        ValidationMessages.ERROR_ADULT_EMAIL_INVALID.translate(email)
    }

}