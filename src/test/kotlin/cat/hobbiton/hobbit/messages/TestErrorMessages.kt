package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nErrorMessage
import org.springframework.http.HttpStatus

enum class TestErrorMessages(
        override val code: String,
        override val httpStatus: HttpStatus
) : I18nErrorMessage {

    TEST_ERROR_MSG_ZERO_PARAMS("test.zero.params", HttpStatus.BAD_REQUEST),
    TEST_ERROR_MSG_ONE_PARAMS("test.one.param", HttpStatus.CONFLICT)
}