package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.service.generate.bdd.string.StringBddBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BddConfiguration {

    @Bean
    fun bddBuilder(): BddBuilder {
        return StringBddBuilder()
    }
}
