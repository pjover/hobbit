package cat.hobbiton.hobbit.util

import org.springframework.context.support.ResourceBundleMessageSource
import java.util.*
import java.util.Locale.ENGLISH

private const val BASENAMES = "messages"
private const val ENCODING = "UTF-8"

object I18nTranslator {

    private var messageSource = ResourceBundleMessageSource()

    init {
        messageSource.setBasenames(BASENAMES)
        messageSource.setDefaultEncoding(ENCODING)
    }

    fun english(i18nMessage: I18nMessage, vararg params: Any): String {
        return this.translateWithParamsArray(ENGLISH, i18nMessage, params)
    }

    fun englishWithParamsArray(i18nMessage: I18nMessage, params: Array<out Any>): String {
        return this.translateWithParamsArray(ENGLISH, i18nMessage, params)
    }

    fun translate(locale: Locale, i18nMessage: I18nMessage, vararg params: Any): String {
        return this.translateWithParamsArray(locale, i18nMessage, params)
    }

    fun translateWithParamsArray(locale: Locale, i18nMessage: I18nMessage, params: Array<out Any>): String {
        return messageSource.getMessage(i18nMessage.code, params, locale)
    }
}