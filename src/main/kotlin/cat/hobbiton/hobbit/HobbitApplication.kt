package cat.hobbiton.hobbit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HobbitApplication

fun main(args: Array<String>) {
	runApplication<HobbitApplication>(*args)
}
