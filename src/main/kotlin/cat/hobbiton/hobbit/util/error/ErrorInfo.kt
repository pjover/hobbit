package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.appName
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorInfo(
    val message: String?,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val path: String?,
    val status: Int,
    val error: String = HttpStatus.valueOf(status).reasonPhrase,
    val service: String = appName,
    val version: String
)
