package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nErrorMessage
import org.springframework.http.HttpStatus

enum class ErrorMessages(
        override val code: String,
        override val httpStatus: HttpStatus
) : I18nErrorMessage {

    ERROR_PRODUCT_NOT_FOUND("error.product.not_found", HttpStatus.NOT_FOUND),
    ERROR_CHILD_NOT_FOUND("error.child.not_found", HttpStatus.NOT_FOUND),
    ERROR_CUSTOMER_NOT_FOUND("error.customer.not_found", HttpStatus.NOT_FOUND),
    ERROR_SEQUENCE_NOT_FOUND("error.sequence.not_found", HttpStatus.NOT_FOUND),
    ERROR_INVOICE_NOT_FOUND("error.invoice.not_found", HttpStatus.NOT_FOUND),
    ERROR_SAVING_INVOICE("error.saving.invoice", HttpStatus.INTERNAL_SERVER_ERROR)
}