package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.Address
import cat.hobbiton.hobbit.util.i18n.translate

fun Address.validate() {

    require(street.isNotBlank()) {
        ValidationMessages.ERROR_ADDRESS_STREET_BLANK.translate()
    }

    require(zipCode.isNotBlank()) {
        ValidationMessages.ERROR_ADDRESS_ZIP_CODE_BLANK.translate()
    }

    require(city.isNotBlank()) {
        ValidationMessages.ERROR_ADDRESS_CITY_BLANK.translate()
    }

    require(state.isNotBlank()) {
        ValidationMessages.ERROR_ADDRESS_STATE_BLANK.translate()
    }
}