package cat.hobbiton.hobbit.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException


@ControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(value = [ApiException::class])
    fun onApiException(ex: ApiException, response: HttpServletResponse) {
        response.sendError(ex.code, ex.message)
    }

    @ExceptionHandler(value = [NotImplementedError::class])
    fun onNotImplemented(ex: NotImplementedError, response: HttpServletResponse) {
        response.sendError(HttpStatus.NOT_IMPLEMENTED.value())
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun onConstraintViolation(ex: ConstraintViolationException, response: HttpServletResponse) {
        response.sendError(
                HttpStatus.BAD_REQUEST.value(),
                ex.constraintViolations.joinToString(", ") { it.message }
        )
    }
}
