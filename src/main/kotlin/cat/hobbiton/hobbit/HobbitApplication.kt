package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.util.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener


@SpringBootApplication
class HobbitApplication(
    @Value("\${appVersion}") private val appVersion: String,
    @Value("\${spring.profiles.active}") private val activeProfile: String
    ) {
    private val logger by Logging()

    @EventListener(ApplicationReadyEvent::class)
    fun afterStartup() {
        logger.info("🟢 Started $appName v$appVersion (${activeProfile.toUpperCase()})")
    }
}

fun main(args: Array<String>) {
    runApplication<HobbitApplication>(*args)
}

