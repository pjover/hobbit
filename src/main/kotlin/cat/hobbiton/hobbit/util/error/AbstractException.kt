package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.util.i18n.I18nMessage
import cat.hobbiton.hobbit.util.i18n.translateWithParamsArray

open class AbstractException : RuntimeException {
    val errorMessage: I18nMessage
    val params: Array<out Any>

    constructor(
        errorMessage: I18nMessage,
        vararg params: Any
    ) : super(errorMessage.translateWithParamsArray(params)) {
        this.errorMessage = errorMessage
        this.params = params
    }

    constructor(
        cause: Throwable,
        errorMessage: I18nMessage,
        vararg params: Any
    ) : super(errorMessage.translateWithParamsArray(params), cause) {
        this.errorMessage = errorMessage
        this.params = params
    }
}