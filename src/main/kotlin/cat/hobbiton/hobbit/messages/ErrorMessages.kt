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
    ERROR_WHILE_BUILDING_PDF("error.while.building_pdf");
}