package cat.hobbiton.hobbit.service.init

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "formatting")
class FormattingProperties {
    lateinit var locale: String
    lateinit var longDateFormat: String
}
