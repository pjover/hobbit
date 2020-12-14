package cat.hobbiton.hobbit.service.generate.pdf.itext

import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.totalAmount
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.BarcodePDF417
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter

class Footer(invoice: Invoice) : PdfPageEventHelper() {

    override fun onEndPage(writer: PdfWriter, document: Document) {
        val pdf417 = BarcodePDF417()
        pdf417.setText(barcodeText)
        val img = pdf417.image
        val x = (document.left() + (document.right() - document.left()) / 2 - img.width / 2).toInt()
        val y = (document.bottom() - 20).toInt()
        img.setAbsolutePosition(x.toFloat(), y.toFloat())
        document.add(img)
    }

    private val barcodeText = "[seq=${invoice.id}, client=${invoice.customerId}, date=${invoice.date}, total=${invoice.totalAmount()}]"
}