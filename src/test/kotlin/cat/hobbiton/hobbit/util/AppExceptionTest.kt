package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.TestErrorMessages.TEST_ERROR_MSG_ONE_PARAMS
import cat.hobbiton.hobbit.messages.TestErrorMessages.TEST_ERROR_MSG_ZERO_PARAMS
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class AppExceptionTest : DescribeSpec() {

    init {

        describe("without cause") {

            val executor = {
                throw AppException(TEST_ERROR_MSG_ZERO_PARAMS, 3)
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "TEST TEXT FOR ZERO PARAMS"
                exception.errorMessage shouldBe TEST_ERROR_MSG_ZERO_PARAMS
                exception.params shouldBe arrayOf(3)
            }
        }

        describe("with cause") {

            val cause = IllegalArgumentException("Root cause")

            val executor = {
                throw AppException(cause, TEST_ERROR_MSG_ONE_PARAMS, 3)
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "TEST TEXT FOR ONE PARAMS: 3"
                exception.errorMessage shouldBe TEST_ERROR_MSG_ONE_PARAMS
                exception.params shouldBe arrayOf(3)
                exception.cause shouldBe cause
            }
        }
    }
}