package cat.hobbiton.hobbit.service.generate.pdf.itext

import cat.hobbiton.hobbit.init.BusinessProperties
import cat.hobbiton.hobbit.init.FormattingProperties
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.testInvoice
import cat.hobbiton.hobbit.testProducts
import com.itextpdf.text.pdf.PdfReader
import io.kotlintest.matchers.string.shouldStartWith
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk


class ItextPdfBuilderServiceTest : DescribeSpec() {

    init {
        val businessProperties = mockk<BusinessProperties>()
        val formattingProperties = mockk<FormattingProperties>()
        val sut = ItextPdfBuilderService(businessProperties, formattingProperties)

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

            val actual = sut.generate(
                testInvoice(),
                testCustomer(id = 148),
                testProducts().map { it.id to it }.toMap())

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