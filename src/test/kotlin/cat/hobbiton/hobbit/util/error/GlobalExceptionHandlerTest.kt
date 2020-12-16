package cat.hobbiton.hobbit.util.error

import cat.hobbiton.hobbit.messages.ErrorMessages
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import javax.servlet.http.HttpServletRequest

class GlobalExceptionHandlerTest : DescribeSpec() {

    init {
        val sut = GlobalExceptionHandler()

        val url = "/test/url"
        val request = mockk<HttpServletRequest>()
        every { request.requestURL } returns StringBuffer(url)

        describe("onAppException") {

            val actual = sut.onAppException(request, AppException(ErrorMessages.ERROR_SAVING_INVOICE, "TST"))

            it("returns the correct ErrorInfo") {
                actual.message shouldBe "Error while saving invoice: TST"
                actual.status shouldBe 500
                actual.path shouldBe url
            }
        }

        describe("onNotFoundException") {

            val actual = sut.onNotFoundException(request, NotFoundException(ErrorMessages.ERROR_PRODUCT_NOT_FOUND, "TST"))

            it("returns the correct ErrorInfo") {
                actual.message shouldBe "Cannot find product with id TST"
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