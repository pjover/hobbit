package cat.hobbiton.hobbit.service.generate.pdf.itext

import cat.hobbiton.hobbit.init.BusinessProperties
import cat.hobbiton.hobbit.init.FormattingProperties
import cat.hobbiton.hobbit.testCustomer185
import cat.hobbiton.hobbit.testInvoice185
import cat.hobbiton.hobbit.testProductsMap
import com.itextpdf.text.pdf.PdfReader
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import io.mockk.every
import io.mockk.mockk


class ItextPdfBuilderServiceTest : DescribeSpec() {

    init {
        val businessProperties = mockk<BusinessProperties>()
        val formattingProperties = mockk<FormattingProperties>()
        val sut = ItextPdfBuilderService(businessProperties, formattingProperties, "src/main/resources/logo.png")

        every { businessProperties.businessName } returns "BusinessName"
        every { businessProperties.addressLine1 } returns "AddressLine1"
        every { businessProperties.addressLine2 } returns "AddressLine2"
        every { businessProperties.addressLine3 } returns "AddressLine3"
        every { businessProperties.addressLine4 } returns "AddressLine4"
        every { businessProperties.taxIdLine } returns "TaxIdLine"
        every { businessProperties.getPaymentTypeNotes(any()) } returns "PaymentNote"

        every { formattingProperties.locale } returns "en_US"
        every { formattingProperties.longDateFormat } returns "MMMM d, yyyy"

        sut.init()

        describe("generate") {

            val actual = sut.generate(testInvoice185, testCustomer185, testProductsMap)

            val reader = PdfReader(actual.byteArray)
            val pages = reader.numberOfPages
            val info = reader.info
            reader.close()

            it("is a valid PDF file") {
                pages shouldBe 1
                info shouldNotBe null
                info["Producer"] shouldStartWith "iText"
            }
        }
    }
}