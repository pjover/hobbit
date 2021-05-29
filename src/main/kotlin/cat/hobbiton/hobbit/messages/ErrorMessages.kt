package cat.hobbiton.hobbit.messages

import cat.hobbiton.hobbit.util.i18n.I18nMessage

enum class ErrorMessages(
    override val code: String
) : I18nMessage {

    ERROR_PRODUCT_NOT_FOUND("error.product.not_found"),
    ERROR_CHILD_NOT_FOUND("error.child.not_found"),
    ERROR_CHILD_INACTIVE("error.child.inactive"),
    ERROR_CUSTOMER_NOT_FOUND("error.customer.not_found"),
    ERROR_CUSTOMER_INACTIVE("error.customer.inactive"),
    ERROR_CUSTOMERS_NOT_FOUND("error.customers.not_found"),
    ERROR_SEQUENCE_NOT_FOUND("error.sequence.not_found"),
    ERROR_PDFS_TO_GENERATE_NOT_FOUND("error.pdfs_to_generate.not_found"),
    ERROR_EMAILS_TO_SEND_NOT_FOUND("error.emails_to_send.not_found"),
    ERROR_INVOICE_NOT_FOUND("error.invoice.not_found"),

    ERROR_SAVING_INVOICE("error.saving.invoice"),
    ERROR_WHILE_BUILDING_PDF("error.while.building_pdf"),

    ERROR_SPREADSHEET_INVOICES_NOT_FOUND("error.spreadsheet.invoices.not_found"),
    ERROR_SPREADSHEET_BLANK_FILENAME("error.spreadsheet.blank_filename"),
    ERROR_SPREADSHEET_BLANK_TITLE("error.spreadsheet.blank_title"),
    ERROR_SPREADSHEET_NO_HEADERS("error.spreadsheet.no_headers"),
    ERROR_SPREADSHEET_NO_DATA("error.spreadsheet.no_data"),
    ERROR_SPREADSHEET_NO_MATCH_COLUMN_NUMBER("error.spreadsheet.no_match_colum_numbers");
}