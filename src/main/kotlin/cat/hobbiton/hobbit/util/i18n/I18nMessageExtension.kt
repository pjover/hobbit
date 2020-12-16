package cat.hobbiton.hobbit.util.i18n


fun I18nMessage.translate(vararg params: Any): String {
    return I18nTranslator.translateWithParamsArray(this, params)
}

fun I18nMessage.translateWithParamsArray(params: Array<out Any>): String {
    return I18nTranslator.translateWithParamsArray(this, params)
}