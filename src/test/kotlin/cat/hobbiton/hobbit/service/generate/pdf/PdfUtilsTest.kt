package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.testInvoice
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class PdfUtilsTest : DescribeSpec() {

    init {
        describe("getPdfName") {
            val invoice = testInvoice()

            val actual = invoice.getPdfName()

            it("should compose the correct PDF name") {
                actual shouldBe "F-103 (148).pdf"
            }
        }
    }

}