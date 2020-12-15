package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.util.i18n.I18nErrorMessage

open class AppException : AbstractException {
    constructor(errorMessage: I18nErrorMessage, vararg params: Any) : super(errorMessage, *params)
    constructor(cause: Throwable, errorMessage: I18nErrorMessage, vararg params: Any) : super(cause, errorMessage, *params)
}