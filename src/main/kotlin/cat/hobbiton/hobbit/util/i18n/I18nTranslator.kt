package cat.hobbiton.hobbit.util.i18n

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource

private const val BASENAMES = "messages"
private const val ENCODING = "UTF-8"

object I18nTranslator {

    private var messageSource = ResourceBundleMessageSource()

    init {
        messageSource.setBasenames(BASENAMES)
        messageSource.setDefaultEncoding(ENCODING)
    }

    fun translate(i18nMessage: I18nMessage, vararg params: Any): String {
        return translateWithParamsArray(i18nMessage, params)
    }

    fun translateWithParamsArray(i18nMessage: I18nMessage, params: Array<out Any>): String {
        return messageSource.getMessage(i18nMessage.code, params, LocaleContextHolder.getLocale())
    }
}