package cat.hobbiton.hobbit.service.generate.pdf.itext

import cat.hobbiton.hobbit.init.BusinessProperties
import cat.hobbiton.hobbit.init.FormattingProperties
import cat.hobbiton.hobbit.logoResourcePath
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.messages.TextMessages
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.model.extension.childrenNames
import cat.hobbiton.hobbit.model.extension.grossAmount
import cat.hobbiton.hobbit.model.extension.taxAmount
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.generate.pdf.PdfBuilderService
import cat.hobbiton.hobbit.service.generate.pdf.getPdfName
import cat.hobbiton.hobbit.util.error.AppException
import cat.hobbiton.hobbit.util.i18n.translate
import cat.hobbiton.hobbit.util.resource.FileResource
import com.itextpdf.text.*
import com.itextpdf.text.Font.FontFamily
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import org.apache.commons.lang3.LocaleUtils
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*
import javax.annotation.PostConstruct

@Service
class ItextPdfBuilderService(
    private val businessProperties: BusinessProperties,
    private val formattingProperties: FormattingProperties
) : PdfBuilderService {

    companion object {
        private val header1: Font = Font(FontFamily.HELVETICA, 14f, Font.BOLD)
        private val header2: Font = Font(FontFamily.HELVETICA, 12f, Font.NORMAL)
        private val header3: Font = Font(FontFamily.HELVETICA, 11f, Font.BOLD)
        private val header4: Font = Font(FontFamily.HELVETICA, 9f, Font.BOLD)
        private val normal: Font = Font(FontFamily.HELVETICA, 10f, Font.NORMAL)
        private val italic: Font = Font(FontFamily.HELVETICA, 10f, Font.ITALIC)
        private val small: Font = Font(FontFamily.HELVETICA, 9f, Font.NORMAL)
        private lateinit var locale: Locale
        private lateinit var oneDecimalFormat: NumberFormat
        private lateinit var twoDecimalFormat: NumberFormat
        private lateinit var longDateFormat: DateTimeFormatter
        private lateinit var logo: Image
    }

    @PostConstruct
    fun init() {
        locale = LocaleUtils.toLocale(formattingProperties.locale)
        longDateFormat = DateTimeFormatter.ofPattern(formattingProperties.longDateFormat, locale)
        oneDecimalFormat = NumberFormat.getNumberInstance(locale)
        oneDecimalFormat.minimumFractionDigits = 0
        oneDecimalFormat.maximumFractionDigits = 1
        twoDecimalFormat = NumberFormat.getNumberInstance(locale)
        twoDecimalFormat.minimumFractionDigits = 2
        twoDecimalFormat.maximumFractionDigits = 2
        logo = getLogoImage()
    }

    private fun getLogoImage(): Image {
        val logo = this::class.java.getResource(logoResourcePath)
            ?: throw IllegalArgumentException(ValidationMessages.ERROR_LOGO_FILE_NOT_FOUND.translate(logoResourcePath))
        val file = File(logo.file)
        if(!file.exists()) {
            throw IllegalArgumentException(ValidationMessages.ERROR_LOGO_FILE_NOT_FOUND.translate(logoResourcePath))
        }
        return try {
            Image.getInstance(file.absolutePath)
        } catch(e: Exception) {
            throw IllegalArgumentException(ValidationMessages.ERROR_LOGO_FILE_NOT_FOUND.translate(logoResourcePath), e)
        }
    }

    override fun generate(invoice: Invoice, customer: Customer, products: Map<String, Product>): FileResource {
        try {
            ByteArrayOutputStream().use {
                generate(it, invoice, customer, products)
                return FileResource(it.toByteArray(), invoice.getPdfName())
            }
        } catch(e: Exception) {
            throw AppException(e, ErrorMessages.ERROR_WHILE_BUILDING_PDF, e.message!!)
        }
    }

    private fun generate(outputStream: OutputStream, invoice: Invoice, customer: Customer, products: Map<String, Product>) {
        val doc = Document(PageSize.A4)
        try {
            val writer = PdfWriter.getInstance(doc, outputStream)
            writer.pageEvent = Footer(invoice)
            doc.open()
            fillDocument(doc, invoice, customer, products)
        } finally {
            doc.close()
        }
    }

    private fun fillDocument(doc: Document, invoice: Invoice, customer: Customer, products: Map<String, Product>) {
        doc.newPage()
        logo(doc)
        headerGroup(doc, invoice)
        businessGroup(doc)
        clientGroup(doc, invoice, customer)
        detailsTable(doc, invoice, products)
        totalsTable(doc, invoice)
        notesGroup(doc, invoice, customer)
    }

    private fun headerGroup(doc: Document, invoice: Invoice) {
        val p = headerGroupParagraph(TextMessages.PDF_INVOICE_HEADER_LABEL.translate(), header1)

        p.add(LineSeparator(1f, 100f, null, Element.ALIGN_CENTER, -2f))
        doc.add(p)

        val number = TextMessages.PDF_INVOICE_HEADER_NUMBER.translate() + ": " + invoice.id
        doc.add(headerGroupParagraph(number, header2))

        val date = TextMessages.PDF_INVOICE_HEADER_DATE.translate() + ": " + longDateFormat.format(invoice.date)
        doc.add(headerGroupParagraph(date, header2))
    }

    private fun headerGroupParagraph(text: String, font: Font): Paragraph {
        val p = Paragraph(text, font)
        p.indentationLeft = 330f
        return p
    }

    private fun logo(doc: Document) {
        logo.scalePercent(50f)
        logo.setAbsolutePosition(40f, PageSize.A4.height - logo.scaledHeight - 50)
        doc.add(logo)
    }

    private fun businessGroup(doc: Document) {
        val p = Paragraph(businessProperties.businessName, header3)
        p.spacingBefore = 50f
        doc.add(p)
        doc.add(Paragraph(businessProperties.addressLine1, normal))
        doc.add(Paragraph(businessProperties.addressLine2, normal))
        doc.add(Paragraph(businessProperties.addressLine3, normal))
        doc.add(Paragraph(businessProperties.addressLine4, normal))
        doc.add(Paragraph(businessProperties.taxIdLine, header3))
    }

    private fun clientGroup(doc: Document, invoice: Invoice, customer: Customer) {
        doc.add(clientGroupParagraph(customer.invoiceHolder.name, header3))
        doc.add(clientGroupParagraph(customer.invoiceHolder.address.street, normal))
        doc.add(clientGroupParagraph("${customer.invoiceHolder.address.zipCode} ${customer.invoiceHolder.address.city}, ${customer.invoiceHolder.address.state}", normal))
        if(customer.invoiceHolder.email != null) {
            doc.add(clientGroupParagraph(customer.invoiceHolder.email, normal))
        }

        val children = TextMessages.PDF_INVOICE_CLIENT_CHILDREN.translate() + ": " + invoice.childrenNames(customer)
        doc.add(clientGroupParagraph(children, normal))

        val taxId = TextMessages.PDF_INVOICE_CLIENT_TAX_ID.translate() + ": " + customer.invoiceHolder.taxId
        doc.add(clientGroupParagraph(taxId, header3))

        val customerId = TextMessages.PDF_INVOICE_CLIENT_CUSTOMER_ID.translate() + ": " + customer.id
        doc.add(clientGroupParagraph(customerId, italic))
    }

    private fun clientGroupParagraph(text: String?, font: Font): Paragraph {
        val p = Paragraph(text, font)
        p.indentationLeft = 280f
        return p
    }

    private fun detailsTable(doc: Document, invoice: Invoice, products: Map<String, Product>) {
        var num: String
        val table = buildPdfPTable()
        var cell: PdfPCell

        // write table row data
        for(line in invoice.lines) {

            // units
            num = oneDecimalFormat.format(line.units)
            cell = PdfPCell(Phrase(num, small))
            cell.setPadding(4f)
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)

            // concept
            cell = PdfPCell(Phrase(products[line.productId]?.name, small))
            table.addCell(cell)

            // price
            num = twoDecimalFormat.format(line.productPrice)
            table.addCell(Phrase(num, small))

            // amount
            num = twoDecimalFormat.format(line.grossAmount())
            table.addCell(Phrase(num, small))

            // tax
            num = oneDecimalFormat.format(line.taxPercentage)
            cell = PdfPCell(Phrase("$num%", small))
            cell.setPadding(4f)
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)

            num = twoDecimalFormat.format(line.taxAmount())
            table.addCell(Phrase(num, small))

            // total
            num = twoDecimalFormat.format(line.totalAmount())
            table.addCell(Phrase(num, small))
        }
        doc.add(table)
    }


    private fun buildPdfPTable(): PdfPTable {
        val table = PdfPTable(7)
        table.widthPercentage = 100.0f
        table.setWidths(intArrayOf(4, 20, 5, 5, 3, 5, 5))
        table.spacingBefore = 30f
        table.spacingAfter = 0f
        table.horizontalAlignment = Element.ALIGN_CENTER
        table.defaultCell.horizontalAlignment = Element.ALIGN_RIGHT
        table.defaultCell.verticalAlignment = Element.ALIGN_MIDDLE

        // define table header cell
        val cell = PdfPCell()
        cell.backgroundColor = BaseColor.LIGHT_GRAY
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.setPadding(4f)

        // write table header
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TABLE_UNITS.translate(), header4)
        table.addCell(cell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TABLE_ITEM.translate(), header4)
        table.addCell(cell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TABLE_PRICE.translate(), header4)
        table.addCell(cell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TABLE_AMOUNT.translate(), header4)
        table.addCell(cell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TABLE_TAX_TYPE.translate(), header4)
        table.addCell(cell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TABLE_TAX_AMOUNT.translate(), header4)
        table.addCell(cell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TABLE_TOTAL.translate(), header4)
        table.addCell(cell)
        return table
    }


    private fun totalsTable(doc: Document, invoice: Invoice) {
        val table = PdfPTable(7)
        table.widthPercentage = 100.0f
        table.setWidths(intArrayOf(4, 20, 5, 5, 3, 5, 5))
        table.spacingBefore = 10f
        table.spacingAfter = 30f
        table.horizontalAlignment = Element.ALIGN_CENTER
        table.defaultCell.verticalAlignment = Element.ALIGN_MIDDLE

        // define table header cell
        val cell = PdfPCell()
        cell.setPadding(4f)

        val emptyCell = PdfPCell()
        emptyCell.phrase = Phrase(" ", header4)
        emptyCell.disableBorderSide(Rectangle.BOTTOM)
        emptyCell.disableBorderSide(Rectangle.LEFT)
        emptyCell.disableBorderSide(Rectangle.RIGHT)
        emptyCell.disableBorderSide(Rectangle.TOP)

        for(i in 0..4) table.addCell(emptyCell)

        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TOTALS_SUM.translate(), header4)
        cell.horizontalAlignment = Element.ALIGN_LEFT
        table.addCell(cell)

        var num: String? = twoDecimalFormat.format(invoice.grossAmount())
        cell.phrase = Phrase(num, small)
        cell.horizontalAlignment = Element.ALIGN_RIGHT
        table.addCell(cell)

        for(i in 0..4) table.addCell(emptyCell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TOTALS_TAX.translate(), header4)
        cell.horizontalAlignment = Element.ALIGN_LEFT
        table.addCell(cell)

        num = twoDecimalFormat.format(invoice.taxAmount())
        cell.phrase = Phrase(num, small)
        cell.horizontalAlignment = Element.ALIGN_RIGHT
        table.addCell(cell)

        for(i in 0..4) table.addCell(emptyCell)
        cell.phrase = Phrase(TextMessages.PDF_INVOICE_TOTALS_TOTAL.translate(), header4)
        cell.horizontalAlignment = Element.ALIGN_LEFT
        table.addCell(cell)

        num = twoDecimalFormat.format(invoice.totalAmount())
        cell.phrase = Phrase(num, small)
        cell.horizontalAlignment = Element.ALIGN_RIGHT
        table.addCell(cell)

        doc.add(table)
    }


    private fun notesGroup(doc: Document, invoice: Invoice, customer: Customer) {

        addNote(doc, invoice.note, normal)
        addNote(doc, customer.note, normal)

        for(child in customer.children) {
            addNote(doc, child.note, normal)
        }

        val p = Paragraph(" ", italic)
        p.spacingBefore = 30f
        p.add(LineSeparator(0.5f, 100f, null, Element.ALIGN_CENTER, -2f))
        doc.add(p)
        addNote(doc, businessProperties.getPaymentTypeNotes(invoice.paymentType), italic)
    }

    private fun addNote(doc: Document, nota: String?, font: Font) {
        if(nota != null && nota.isNotEmpty()) {
            val p = Paragraph(nota, font)
            p.indentationLeft = 10f
            doc.add(p)
        }
    }
}