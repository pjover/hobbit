package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nErrorMessage
import org.springframework.http.HttpStatus

enum class ErrorMessages(
        override val code: String,
        override val httpStatus: HttpStatus
) : I18nErrorMessage {

    ERROR_INVALID_CUSTOMER_SEQUENCE("error.invalid.customer.sequence", HttpStatus.INTERNAL_SERVER_ERROR) // TODO remove
}