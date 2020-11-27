package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.ErrorMessages
import io.kotlintest.specs.DescribeSpec
import io.mockk.*
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

class GlobalExceptionHandlerTest : DescribeSpec() {

    init {
        val response = mockk<HttpServletResponse>()
        val sut = GlobalExceptionHandler()

        every { response.sendError(any(), any()) } just runs
        every { response.sendError(any()) } just runs

        describe("onAppException") {

            sut.onAppException(AppException(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, "TST"), response)

            it("returns the correct response") {
                verify(exactly = 1) {
                    response.sendError(
                            ErrorMessages.ERROR_PRODUCT_NOT_FOUND.httpStatus.value(),
                            "Cannot find product with id TST"
                    )
                }
            }
        }

        describe("onNotImplemented") {

            sut.onNotImplemented(NotImplementedError(), response)

            it("returns the correct response") {
                verify(exactly = 1) {
                    response.sendError(
                            HttpStatus.NOT_IMPLEMENTED.value()
                    )
                }
            }
        }

        describe("onIllegalArgumentException") {

            sut.onIllegalArgumentException(IllegalArgumentException("Error message"), response)

            it("returns the correct response") {
                verify(exactly = 1) {
                    response.sendError(
                            HttpStatus.BAD_REQUEST.value(),
                            "Error message"
                    )
                }
            }
        }

        describe("onConstraintViolation") {

            sut.onConstraintViolation(ConstraintViolationException(setOf()), response)

            it("returns the correct response") {
                verify(exactly = 1) {
                    response.sendError(
                            HttpStatus.BAD_REQUEST.value(),
                            "Error message"
                    )
                }
            }
        }
    }
}