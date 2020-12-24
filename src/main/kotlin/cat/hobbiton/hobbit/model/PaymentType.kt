package cat.hobbiton.hobbit.model

import cat.hobbiton.hobbit.messages.TextMessages
import cat.hobbiton.hobbit.util.i18n.I18nMessage

enum class PaymentType(
    val sequenceType: SequenceType,
    val message: I18nMessage
) {
    BANK_DIRECT_DEBIT(SequenceType.STANDARD_INVOICE, TextMessages.PAYMENT_TYPE_BANK_DIRECT_DEBIT),
    BANK_TRANSFER(SequenceType.SPECIAL_INVOICE, TextMessages.PAYMENT_TYPE_BANK_TRANSFER),
    VOUCHER(SequenceType.SPECIAL_INVOICE, TextMessages.PAYMENT_TYPE_VOUCHER),
    CASH(SequenceType.SPECIAL_INVOICE, TextMessages.PAYMENT_TYPE_CASH),
    RECTIFICATION(SequenceType.RECTIFICATION_INVOICE, TextMessages.PAYMENT_TYPE_RECTIFICATION);
}
