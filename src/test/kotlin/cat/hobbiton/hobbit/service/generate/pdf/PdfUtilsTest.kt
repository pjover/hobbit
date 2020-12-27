package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.testInvoice185
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class PdfUtilsTest : DescribeSpec() {

    init {
        describe("getPdfName") {
            val invoice = testInvoice185.copy(id = "F-103")

            val actual = invoice.getPdfName()

            it("should compose the correct PDF name") {
                actual shouldBe "F-103 (185).pdf"
            }
        }
    }

}