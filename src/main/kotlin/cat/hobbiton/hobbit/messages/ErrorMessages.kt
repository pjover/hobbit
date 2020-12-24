package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.i18n.I18nMessage

enum class ErrorMessages(
    override val code: String
) : I18nMessage {

    ERROR_PRODUCT_NOT_FOUND("error.product.not_found"),
    ERROR_CHILD_NOT_FOUND("error.child.not_found"),
    ERROR_CUSTOMER_NOT_FOUND("error.customer.not_found"),
    ERROR_SEQUENCE_NOT_FOUND("error.sequence.not_found"),
    ERROR_PDFS_TO_GENERATE_NOT_FOUND("error.pdfs_to_generate.not_found"),
    ERROR_EMAILS_TO_SEND_NOT_FOUND("error.emails_to_send.not_found"),
    ERROR_INVOICE_NOT_FOUND("error.invoice.not_found"),

    ERROR_SAVING_INVOICE("error.saving.invoice"),
    ERROR_WHILE_BUILDING_PDF("error.while.building_pdf"),

    ERROR_SPREAD_SHEET_TO_GENERATE_NOT_FOUND("error.spread_sheet_to_generate.not_found"),
    ERROR_SPREAD_SHEET_BLANK_FILENAME("error.spread_sheet.blank_filename"),
    ERROR_SPREAD_SHEET_BLANK_TITLE("error.spread_sheet.blank_title"),
    ERROR_SPREAD_SHEET_NO_HEADERS("error.spread_sheet.no_headers"),
    ERROR_SPREAD_SHEET_NO_DATA("error.spread_sheet.no_data"),
    ERROR_SPREAD_SHEET_NO_MATCH_COLUMN_NUMBER("error.spread_sheet.no_match_colum_numbers");
}