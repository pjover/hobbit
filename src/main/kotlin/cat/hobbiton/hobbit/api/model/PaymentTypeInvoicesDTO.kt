package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Invoices grouped by payment type
 * @param paymentType
 * @param totalAmount
 * @param customers
 */
data class PaymentTypeInvoicesDTO(

    @get:NotNull
    @JsonProperty("paymentType") val paymentType: PaymentTypeDTO,

    @get:NotNull
    @JsonProperty("totalAmount") val totalAmount: java.math.BigDecimal,

    @get:NotNull
    @JsonProperty("customers") val customers: List<CustomerInvoicesDTO>
)

