package cat.hobbiton.hobbit.model

enum class PaymentType(
    val sequenceType: SequenceType
) {
    BANK_DIRECT_DEBIT(SequenceType.STANDARD_INVOICE),
    BANK_TRANSFER(SequenceType.SPECIAL_INVOICE),
    VOUCHER(SequenceType.SPECIAL_INVOICE),
    CASH(SequenceType.SPECIAL_INVOICE),
    RECTIFICATION(SequenceType.RECTIFICATION_INVOICE);
}
