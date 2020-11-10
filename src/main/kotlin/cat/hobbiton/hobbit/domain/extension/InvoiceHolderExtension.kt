package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.InvoiceHolder
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate

fun InvoiceHolder.emailText() = "$name <$email>"

fun InvoiceHolder.isEmailConfigured(): Boolean = email?.isValidEmail() ?: false

fun InvoiceHolder.validate() {

    require(name.isNotBlank()) {
        ValidationMessages.ERROR_INVOICE_HOLDER_NAME_BLANK.translate()
    }

    require(taxId.isValidTaxId()) {
        ValidationMessages.ERROR_INVOICE_HOLDER_TAX_ID_INVALID.translate(taxId)
    }

    address.validate()

    if (email != null) require(email.isValidEmail()) {
        ValidationMessages.ERROR_INVOICE_HOLDER_EMAIL_INVALID.translate(email)
    }

    if (bankAccount != null) {
        require(bankAccount.isValidBankAccount()) {
            ValidationMessages.ERROR_INVOICE_HOLDER_BANK_ACCOUNT_INVALID.translate(bankAccount)
        }
    }
}