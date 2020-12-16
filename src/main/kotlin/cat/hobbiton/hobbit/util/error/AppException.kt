package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.util.i18n.I18nMessage

open class AppException : AbstractException {
    constructor(errorMessage: I18nMessage, vararg params: Any) : super(errorMessage, *params)
    constructor(cause: Throwable, errorMessage: I18nMessage, vararg params: Any) : super(cause, errorMessage, *params)
}