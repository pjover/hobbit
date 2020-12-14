package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.I18nMessage

enum class TextMessages(
    override val code: String
) : I18nMessage {

    PDF_INVOICE_HEADER_LABEL("pdf.invoice.header.label"),
    PDF_INVOICE_HEADER_NUMBER("pdf.invoice.header.number"),
    PDF_INVOICE_HEADER_DATE("pdf.invoice.header.date"),
    PDF_INVOICE_CLIENT_CHILDREN("pdf.invoice.client.children"),
    PDF_INVOICE_CLIENT_TAX_ID("pdf.invoice.client.tax_id"),
    PDF_INVOICE_CLIENT_CUSTOMER_ID("pdf.invoice.client.customer_id"),
    PDF_INVOICE_TABLE_UNITS("pdf.invoice.table.units"),
    PDF_INVOICE_TABLE_ITEM("pdf.invoice.table.item"),
    PDF_INVOICE_TABLE_PRICE("pdf.invoice.table.price"),
    PDF_INVOICE_TABLE_AMOUNT("pdf.invoice.table.amount"),
    PDF_INVOICE_TABLE_TAX_TYPE("pdf.invoice.table.type"),
    PDF_INVOICE_TABLE_TAX_AMOUNT("pdf.invoice.table.tax_amount"),
    PDF_INVOICE_TABLE_TOTAL("pdf.invoice.table.total"),
    PDF_INVOICE_TOTALS_SUM("pdf.invoice.totals.sum"),
    PDF_INVOICE_TOTALS_TAX("pdf.invoice.totals.tax"),
    PDF_INVOICE_TOTALS_TOTAL("pdf.invoice.totals.total")

}