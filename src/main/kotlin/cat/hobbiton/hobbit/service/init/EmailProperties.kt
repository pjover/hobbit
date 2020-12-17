package cat.hobbiton.hobbit.service.init

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "email")
class EmailProperties {
    lateinit var username: String
    lateinit var password: String
}
