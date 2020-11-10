package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Child
import cat.hobbiton.hobbit.domain.GroupType
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate

fun Child.shortName(): String = "$name $surname"

fun Child.completeName(): String {
    return if (secondSurname.isNullOrBlank()) shortName()
    else "${shortName()} $secondSurname"
}

fun Child.validate() {

    require(name.isNotBlank()) {
        ValidationMessages.ERROR_CHILD_NAME_BLANK.translate()
    }

    require(surname.isNotBlank()) {
        ValidationMessages.ERROR_CHILD_SURNAME_BLANK.translate()
    }

    if (!taxId.isNullOrBlank()) require(taxId.isValidTaxId()) {
        ValidationMessages.ERROR_CHILD_TAX_ID_INVALID.translate(taxId)
    }

    require(group != GroupType.UNDEFINED) {
        ValidationMessages.ERROR_CHILD_GROUP_UNDEFINED.translate()
    }
}