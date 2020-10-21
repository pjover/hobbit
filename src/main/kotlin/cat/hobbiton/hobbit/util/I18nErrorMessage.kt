package cat.hobbiton.hobbit.util

import org.springframework.http.HttpStatus

interface I18nErrorMessage : I18nMessage {
    val httpStatus: HttpStatus
}