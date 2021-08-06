package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.util.Logging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener


@SpringBootApplication
class HobbitApplication {
    private val logger by Logging()

    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        logger.info("🟢 Started $appName v$appVersion")
    }
}

fun main(args: Array<String>) {
    runApplication<HobbitApplication>(*args)
}

