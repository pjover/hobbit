package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nErrorMessage
import org.springframework.http.HttpStatus

enum class ErrorMessages(
        override val code: String,
        override val httpStatus: HttpStatus
) : I18nErrorMessage {

    ERROR_DATABASE_CONNECTION("error.database.connection", HttpStatus.INTERNAL_SERVER_ERROR)
}