package cat.hobbiton.hobbit.api.exception

import org.springframework.http.HttpStatus

class NotFoundException(msg: String, code: Int = HttpStatus.NOT_FOUND.value()) : ApiException(msg, code)