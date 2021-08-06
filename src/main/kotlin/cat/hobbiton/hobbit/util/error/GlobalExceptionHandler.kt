package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.util.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@ResponseBody
@ControllerAdvice
class GlobalExceptionHandler(@Value("\${appVersion}") private val appVersion: String) {

    private val logger by Logging()

    @ExceptionHandler(value = [AppException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun onAppException(req: HttpServletRequest, ex: AppException): ErrorInfo {
        return errorInfo(req, HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onNotFoundException(req: HttpServletRequest, ex: NotFoundException): ErrorInfo {
        return errorInfo(req, HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onIllegalArgumentException(req: HttpServletRequest, ex: IllegalArgumentException): ErrorInfo {
        return errorInfo(req, HttpStatus.BAD_REQUEST, ex.message)
    }

    @ExceptionHandler(value = [NotImplementedError::class])
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    fun onNotImplemented(req: HttpServletRequest, ex: NotImplementedError): ErrorInfo {
        return errorInfo(req, HttpStatus.NOT_IMPLEMENTED, ex.message)
    }

    fun errorInfo(request: HttpServletRequest, status: HttpStatus, message: String?): ErrorInfo {
        return ErrorInfo(
            message = message,
            path = request.requestURL.toString(),
            status = status.value(),
            version = appVersion
        )
    }
}
