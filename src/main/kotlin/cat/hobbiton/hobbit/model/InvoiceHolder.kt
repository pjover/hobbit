package cat.hobbiton.hobbit.model

data class InvoiceHolder(
    val name: String,
    val taxId: String,
    val address: Address,
    val email: String?,
    val sendEmail: Boolean = false,
    val paymentType: PaymentType,
    val bankAccount: String? = null,
    val isBusiness: Boolean = false
)
