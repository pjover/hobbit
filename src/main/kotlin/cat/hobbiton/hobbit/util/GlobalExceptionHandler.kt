package cat.hobbiton.hobbit.util

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [AppException::class])
    fun onAppException(ex: AppException, response: HttpServletResponse) {
        response.sendError(ex.errorMessage.httpStatus.value(), ex.message)
    }

    @ExceptionHandler(value = [NotImplementedError::class])
    fun onNotImplemented(ex: NotImplementedError, response: HttpServletResponse) {
        response.sendError(HttpStatus.NOT_IMPLEMENTED.value())
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun onIllegalArgumentException(ex: IllegalArgumentException, response: HttpServletResponse) {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.message)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun onConstraintViolation(ex: ConstraintViolationException, response: HttpServletResponse) {
        response.sendError(
                HttpStatus.BAD_REQUEST.value(),
                ex.constraintViolations.joinToString(", ") { it.message }
        )
    }
}