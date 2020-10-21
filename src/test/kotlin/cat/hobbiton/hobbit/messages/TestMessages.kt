package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nMessage

enum class TestsMessages(
        override val code: String
) : I18nMessage {

    TEST_MSG_ZERO_PARAMS("test.zero.params"),
    TEST_MSG_ONE_PARAMS("test.one.param"),
    TEST_MSG_MANY_PARAMS("test.many.params")
}