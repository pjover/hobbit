package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.ErrorMessages.ERROR_DATABASE_CONNECTION
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class AppExceptionTest : DescribeSpec() {

    init {

        describe("without cause") {

            val executor = {
                throw AppException(ERROR_DATABASE_CONNECTION)
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "Cannot connect to database"
                exception.errorMessage shouldBe ERROR_DATABASE_CONNECTION
                exception.params shouldBe arrayOf()
            }
        }

        describe("with cause") {

            val cause = IllegalArgumentException("Root cause")

            val executor = {
                throw AppException(cause, ERROR_DATABASE_CONNECTION)
            }

            it("throws an error") {
                val exception = assertFailsWith<AppException> { executor.invoke() }
                exception.message shouldBe "Cannot connect to database"
                exception.errorMessage shouldBe ERROR_DATABASE_CONNECTION
                exception.params shouldBe arrayOf()
                exception.cause shouldBe cause
            }
        }
    }
}