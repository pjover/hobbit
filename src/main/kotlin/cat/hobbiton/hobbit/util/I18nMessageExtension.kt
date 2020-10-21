package cat.hobbiton.hobbit.util

import java.util.*


fun I18nMessage.english(vararg params: Any): String {
    return I18nTranslator.englishWithParamsArray(this, params)
}

fun I18nMessage.englishWithParamsArray(params: Array<out Any>): String {
    return I18nTranslator.englishWithParamsArray(this, params)
}

fun I18nMessage.translate(locale: Locale, vararg params: Any): String {
    return I18nTranslator.translateWithParamsArray(locale, this, params)
}

fun I18nMessage.translateWithParamsArray(locale: Locale, params: Array<out Any>): String {
    return I18nTranslator.translateWithParamsArray(locale, this, params)
}