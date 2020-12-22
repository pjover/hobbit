package cat.hobbiton.hobbit.api.model

/**
 * Type of payment of the customer or the invoice
 * Values: BANK_DIRECT_DEBIT,BANK_TRANSFER,VOUCHER,CASH,RECTIFICATION
 */
enum class PaymentTypeDTO(val value: String) {

    BANK_DIRECT_DEBIT("BANK_DIRECT_DEBIT"),

    BANK_TRANSFER("BANK_TRANSFER"),

    VOUCHER("VOUCHER"),

    CASH("CASH"),

    RECTIFICATION("RECTIFICATION");

}

