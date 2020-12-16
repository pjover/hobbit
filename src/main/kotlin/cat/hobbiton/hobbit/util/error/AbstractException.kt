package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.util.i18n.I18nErrorMessage
import cat.hobbiton.hobbit.util.i18n.translateWithParamsArray

open class AbstractException : RuntimeException {
    val errorMessage: I18nErrorMessage
    val params: Array<out Any>

    constructor(
        errorMessage: I18nErrorMessage,
        vararg params: Any
    ) : super(errorMessage.translateWithParamsArray(params)) {
        this.errorMessage = errorMessage
        this.params = params
    }

    constructor(
        cause: Throwable,
        errorMessage: I18nErrorMessage,
        vararg params: Any
    ) : super(errorMessage.translateWithParamsArray(params), cause) {
        this.errorMessage = errorMessage
        this.params = params
    }
}