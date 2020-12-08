package cat.hobbiton.hobbit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class HobbitApplication

fun main(args: Array<String>) {
    runApplication<HobbitApplication>(*args)
}
