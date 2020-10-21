package cat.hobbiton.hobbit.util

@Suppress("unused")
open class AppException : RuntimeException {
    val errorMessage: I18nErrorMessage
    val params: Array<out Any>

    constructor(
            errorMessage: I18nErrorMessage,
            vararg params: Any
    ) : super(errorMessage.englishWithParamsArray(params)) {
        this.errorMessage = errorMessage
        this.params = params
    }

    constructor(
            cause: Throwable,
            errorMessage: I18nErrorMessage,
            vararg params: Any
    ) : super(errorMessage.englishWithParamsArray(params), cause) {
        this.errorMessage = errorMessage
        this.params = params
    }
}