package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.messages.ErrorMessages
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import javax.servlet.http.HttpServletRequest

class GlobalExceptionHandlerTest : DescribeSpec() {

    init {
        val sut = GlobalExceptionHandler("5.0.3")

        val url = "/test/url"
        val request = mockk<HttpServletRequest>()
        every { request.requestURL } returns StringBuffer(url)

        describe("onAppException") {

            val actual = sut.onAppException(request, AppException(ErrorMessages.ERROR_SAVING_INVOICE, "TST"))

            it("returns the correct ErrorInfo") {
                actual.status shouldBe 500
                actual.path shouldBe url
                actual.version shouldBe "5.0.3"
            }
        }

        describe("onNotFoundException") {

            val actual = sut.onNotFoundException(request, NotFoundException(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, "TST"))

            it("returns the correct ErrorInfo") {
                actual.status shouldBe 404
                actual.path shouldBe url
            }
        }

        describe("onIllegalArgumentException") {

            val actual = sut.onIllegalArgumentException(request, IllegalArgumentException("Error message"))

            it("returns the correct response") {
                actual.message shouldBe "Error message"
                actual.status shouldBe 400
                actual.path shouldBe url
            }
        }

        describe("onNotImplemented") {

            val actual = sut.onNotImplemented(request, NotImplementedError("Not Implemented Error"))

            it("returns the correct response") {
                actual.message shouldBe "Not Implemented Error"
                actual.status shouldBe 501
                actual.path shouldBe url
            }
        }
    }
}