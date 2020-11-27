package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.ErrorMessages
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class AppExceptionTest : DescribeSpec() {

    init {

        describe("without cause") {

            val executor = {
                throw AppException(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, "TST")
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "Cannot find product with id TST"
                exception.errorMessage shouldBe ErrorMessages.ERROR_PRODUCT_NOT_FOUND
                exception.params shouldBe arrayOf("TST")
            }
        }

        describe("with cause") {

            val cause = IllegalArgumentException("Root cause")

            val executor = {
                throw AppException(cause, ErrorMessages.ERROR_PRODUCT_NOT_FOUND, "TST")
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "Cannot find product with id TST"
                exception.errorMessage shouldBe ErrorMessages.ERROR_PRODUCT_NOT_FOUND
                exception.params shouldBe arrayOf("TST")
                exception.cause shouldBe cause
            }
        }
    }
}