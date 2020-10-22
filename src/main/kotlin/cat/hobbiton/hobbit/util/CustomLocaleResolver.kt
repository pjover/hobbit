package cat.hobbiton.hobbit.util

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.*
import javax.servlet.http.HttpServletRequest

@Configuration
class CustomLocaleResolver : AcceptHeaderLocaleResolver(), WebMvcConfigurer {

    companion object {
        private val LOCALES = listOf(Locale.ENGLISH, Locale("ca"))
    }

    override fun resolveLocale(request: HttpServletRequest): Locale {
        val lang = request.getHeader("Accept-Language") ?: ""
        return if (lang.isEmpty()) LOCALES[0]
        else Locale.lookup(Locale.LanguageRange.parse(lang), LOCALES)
    }
}