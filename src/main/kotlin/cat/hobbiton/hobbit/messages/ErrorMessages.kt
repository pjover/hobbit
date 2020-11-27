package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nErrorMessage
import org.springframework.http.HttpStatus

enum class ErrorMessages(
        override val code: String,
        override val httpStatus: HttpStatus
) : I18nErrorMessage {

    ERROR_PRODUCT_NOT_FOUND("error.product.not_found", HttpStatus.NOT_FOUND),
    ERROR_CHILD_NOT_FOUND("error.child.not_found", HttpStatus.NOT_FOUND)
}