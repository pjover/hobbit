package cat.hobbiton.hobbit.domain

enum class PaymentType(
        val sequenceType: SequenceType,
        val text: String
) {
    BANK_DIRECT_DEBIT(SequenceType.STANDARD_INVOICE, "rebut"),
    BANK_TRANSFER(SequenceType.SPECIAL_INVOICE, "transferència"),
    VOUCHER(SequenceType.SPECIAL_INVOICE, "xec escoleta"),
    CASH(SequenceType.SPECIAL_INVOICE, "en metàlic");
}
