package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.ErrorMessages
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class AppExceptionTest : DescribeSpec() {

    init {

        describe("without cause") {

            val executor = {
                throw AppException(ErrorMessages.ERROR_INVALID_CUSTOMER_SEQUENCE, 2, 3)
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "Invalid customer sequence: last customer is 2 and the sequence is 3"
                exception.errorMessage shouldBe ErrorMessages.ERROR_INVALID_CUSTOMER_SEQUENCE
                exception.params shouldBe arrayOf(2, 3)
            }
        }

        describe("with cause") {

            val cause = IllegalArgumentException("Root cause")

            val executor = {
                throw AppException(cause, ErrorMessages.ERROR_INVALID_CUSTOMER_SEQUENCE, 2, 3)
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "Invalid customer sequence: last customer is 2 and the sequence is 3"
                exception.errorMessage shouldBe ErrorMessages.ERROR_INVALID_CUSTOMER_SEQUENCE
                exception.params shouldBe arrayOf(2, 3)
                exception.cause shouldBe cause
            }
        }
    }
}