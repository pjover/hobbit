package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Invoices grouped by customer
 * @param code Customer code
 * @param shortName Customer short name
 * @param totalAmount
 * @param invoices
 */
data class CustomerInvoicesDTO(

    @get:NotNull
    @JsonProperty("code") val code: Int,

    @get:NotNull
    @JsonProperty("shortName") val shortName: String,

    @get:NotNull
    @JsonProperty("totalAmount") val totalAmount: Double,

    @get:NotNull
    @JsonProperty("invoices") val invoices: List<InvoicesDTO>
)

