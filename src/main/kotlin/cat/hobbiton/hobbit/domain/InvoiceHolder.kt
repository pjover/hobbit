package cat.hobbiton.hobbit.domain

import java.math.BigDecimal

data class InvoiceHolder(
        val name: String,
        val taxId: String,
        val address: Address,
        val email: String?,
        val sendEmail: Boolean = false,
        val paymentType: PaymentType,
        val bankAccount: String? = null,
        val subsidizedAmount: BigDecimal = BigDecimal.ZERO,
        val isBusiness: Boolean = false
)
