package cat.hobbiton.hobbit.service.generate.pdf.itext

import cat.hobbiton.hobbit.service.init.BusinessProperties
import cat.hobbiton.hobbit.service.init.FormattingProperties
import cat.hobbiton.hobbit.testCustomer
import cat.hobbiton.hobbit.testInvoice
import cat.hobbiton.hobbit.testProducts
import com.itextpdf.text.pdf.PdfReader
import io.kotlintest.matchers.file.shouldHaveFileSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk


class ItextPdfBuilderServiceImplTest : DescribeSpec() {

    init {
        val businessProperties = mockk<BusinessProperties>()
        val formattingProperties = mockk<FormattingProperties>()
        val sut = ItextPdfBuilderServiceImpl(businessProperties, formattingProperties)

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

            it("has a correct filename") {
                actual.filename shouldBe "F-103 (148).pdf"
            }

            val file = createTempFile(suffix = ".pdf")
            file.writeBytes(actual.byteArray)

            it("creates the tmp PDF file") {
                file.exists() shouldBe true
            }

            it("has the right size") {
                file shouldHaveFileSize actual.contentLength()
            }

            it("is a valid PDF file") {
                val reader = PdfReader(file.absolutePath)
                reader.close()
            }

            val deleted = file.delete()
            it("deletes the tmp PDF file") {
                deleted shouldBe true
            }
        }
    }
}