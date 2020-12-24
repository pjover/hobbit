package cat.hobbiton.hobbit.init

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "formatting")
class FormattingProperties {
    lateinit var locale: String
    lateinit var longDateFormat: String
    lateinit var spreadSheetDateFormat: String
    lateinit var spreadSheetCurrencyFormat: String
}
