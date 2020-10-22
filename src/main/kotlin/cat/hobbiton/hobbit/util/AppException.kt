package cat.hobbiton.hobbit.util

open class AppException : RuntimeException {
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