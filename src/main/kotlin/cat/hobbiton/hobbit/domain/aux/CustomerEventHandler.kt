package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.*
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler(Customer::class)
class CustomerEventHandler {

    @HandleBeforeSave
    @HandleBeforeCreate
    fun validate(customer: Customer) {
        require(customer.children.isNotEmpty()) { ValidationMessages.ERROR_CUSTOMER_WHITOUT_CHILD.translate() }
        require(customer.adults.isNotEmpty()) { ValidationMessages.ERROR_CUSTOMER_WHITOUT_ADULT.translate() }
        customer.children.forEach { validate(it) }
        customer.adults.forEach { validate(it) }
        validate(customer.invoiceHolder)
    }

    fun validate(child: Child) {
        require(child.name.isNotBlank()) { ValidationMessages.ERROR_CHILD_NAME_BLANK.translate() }
        require(child.surname.isNotBlank()) { ValidationMessages.ERROR_CHILD_SURNAME_BLANK.translate() }
        if (!child.taxId.isNullOrBlank()) require(child.taxId.isValidTaxId()) { ValidationMessages.ERROR_CHILD_TAX_ID_INVALID.translate(child.taxId) }
        require(child.group != GroupType.UNDEFINED) { ValidationMessages.ERROR_CHILD_GROUP_UNDEFINED.translate() }
    }

    fun validate(adult: Adult) {
        require(adult.name.isNotBlank()) { ValidationMessages.ERROR_ADULT_NAME_BLANK.translate() }
        require(adult.surname.isNotBlank()) { ValidationMessages.ERROR_ADULT_SURNAME_BLANK.translate() }
        if (!adult.taxId.isNullOrBlank()) require(adult.taxId.isValidTaxId()) { ValidationMessages.ERROR_ADULT_TAX_ID_INVALID.translate(adult.taxId) }
        if (adult.address != null) validate(adult.address)
        if (adult.email != null) require(adult.email.isValidEmail()) { ValidationMessages.ERROR_ADULT_EMAIL_INVALID.translate(adult.email) }
    }

    fun validate(address: Address) {
        require(address.street.isNotBlank()) { ValidationMessages.ERROR_ADDRESS_STREET_BLANK.translate() }
        require(address.zipCode.isNotBlank()) { ValidationMessages.ERROR_ADDRESS_ZIP_CODE_BLANK.translate() }
        require(address.city.isNotBlank()) { ValidationMessages.ERROR_ADDRESS_CITY_BLANK.translate() }
        require(address.state.isNotBlank()) { ValidationMessages.ERROR_ADDRESS_STATE_BLANK.translate() }
    }

    fun validate(invoiceHolder: InvoiceHolder) {
        require(invoiceHolder.name.isNotBlank()) { ValidationMessages.ERROR_INVOICE_HOLDER_NAME_BLANK.translate() }
        require(invoiceHolder.taxId.isValidTaxId()) { ValidationMessages.ERROR_INVOICE_HOLDER_TAX_ID_INVALID.translate(invoiceHolder.taxId) }
        validate(invoiceHolder.address)
        if (invoiceHolder.email != null) require(invoiceHolder.email.isValidEmail()) { ValidationMessages.ERROR_INVOICE_HOLDER_EMAIL_INVALID.translate(invoiceHolder.email) }
        if (invoiceHolder.bankAccount != null) {
            require(invoiceHolder.bankAccount.isValidBankAccount()) { ValidationMessages.ERROR_INVOICE_HOLDER_BANK_ACCOUNT_INVALID.translate(invoiceHolder.bankAccount) }
        }
    }
}