package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.i18n.I18nMessage

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
    PDF_INVOICE_TOTALS_TOTAL("pdf.invoice.totals.total"),

    MONTH_REPORT_TITLE("month_report.title"),
    MONTH_REPORT_ID("month_report.id"),
    MONTH_REPORT_CUSTOMER("month_report.customer"),
    MONTH_REPORT_CHILDREN("month_report.children"),
    MONTH_REPORT_DATE("month_report.date"),
    MONTH_REPORT_YEAR_MONTH("month_report.year_month"),
    MONTH_REPORT_INVOICE("month_report.invoice"),
    MONTH_REPORT_TOTAL("month_report.total"),
    MONTH_REPORT_PRODUCTS("month_report.products"),
    MONTH_REPORT_PAYMENT("month_report.payment"),
    MONTH_REPORT_NOTE("month_report.note"),

    YEAR_REPORT_TITLE("year_report.title"),
    YEAR_REPORT_ID("year_report.id"),
    YEAR_REPORT_FIRST_ADULT_TAX_ID("year_report.first_adult.tax_id"),
    YEAR_REPORT_FIRST_ADULT_NAME("year_report.first_adult.name"),
    YEAR_REPORT_FIRST_ADULT_SURNAME("year_report.first_adult.surname"),
    YEAR_REPORT_FIRST_ADULT_SECOND_SURNAME("year_report.first_adult.second_surname"),
    YEAR_REPORT_SECOND_ADULT_TAX_ID("year_report.second_adult.tax_id"),
    YEAR_REPORT_SECOND_ADULT_NAME("year_report.second_adult.name"),
    YEAR_REPORT_SECOND_ADULT_SURNAME("year_report.second_adult.surname"),
    YEAR_REPORT_SECOND_ADULT_SECOND_SURNAME("year_report.second_adult.second_surname"),
    YEAR_REPORT_CHILD_TAX_ID("year_report.child.tax_id"),
    YEAR_REPORT_CHILD_NAME("year_report.child.name"),
    YEAR_REPORT_CHILD_SURNAME("year_report.child.surname"),
    YEAR_REPORT_CHILD_SECOND_SURNAME("year_report.child.second_surname"),
    YEAR_REPORT_CHILD_BIRTH_DATE("year_report.child.birth_date"),
    YEAR_REPORT_JANUARY("year.january"),
    YEAR_REPORT_FEBRUARY("year.february"),
    YEAR_REPORT_MARCH("year.march"),
    YEAR_REPORT_APRIL("year.april"),
    YEAR_REPORT_MAY("year.may"),
    YEAR_REPORT_JUNE("year.june"),
    YEAR_REPORT_JULY("year.july"),
    YEAR_REPORT_AUGUST("year.august"),
    YEAR_REPORT_SEPTEMBER("year.september"),
    YEAR_REPORT_OCTOBER("year.october"),
    YEAR_REPORT_NOVEMBER("year.november"),
    YEAR_REPORT_DECEMBER("year.december"),
    YEAR_REPORT_TOTAL("year.total"),

    PAYMENT_TYPE_BANK_DIRECT_DEBIT("payment_type.bank_direct_debit"),
    PAYMENT_TYPE_BANK_TRANSFER("payment_type.bank_transfer"),
    PAYMENT_TYPE_VOUCHER("payment_type.voucher"),
    PAYMENT_TYPE_CASH("payment_type.cash"),
    PAYMENT_TYPE_RECTIFICATION("payment_type.rectification")

}