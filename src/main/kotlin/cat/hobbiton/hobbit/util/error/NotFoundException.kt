package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.util.i18n.I18nErrorMessage

open class NotFoundException : AbstractException {
    constructor(errorMessage: I18nErrorMessage, vararg params: Any) : super(errorMessage, *params)
    constructor(cause: Throwable, errorMessage: I18nErrorMessage, vararg params: Any) : super(cause, errorMessage, *params)
}