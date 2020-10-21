package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nMessage

enum class TestsMessages(
        override val code: String
) : I18nMessage {

    ERROR_TEST_ZERO_PARAMS("test.zero.params"),
    ERROR_TEST_ONE_PARAMS("test.one.param"),
    ERROR_TEST_MANY_PARAMS("test.many.params")
}