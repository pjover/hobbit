package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * A single invoice
 * @param code
 * @param yearMonth
 * @param children
 * @param totalAmount
 * @param lines
 * @param note
 */
data class InvoiceDTO(

    @get:NotNull
    @JsonProperty("code") val code: String,

    @get:NotNull
    @JsonProperty("yearMonth") val yearMonth: String,

    @get:NotNull
    @JsonProperty("children") val children: List<String>,

    @get:NotNull
    @JsonProperty("totalAmount") val totalAmount: java.math.BigDecimal,

    @get:NotNull
    @JsonProperty("lines") val lines: List<InvoiceLineDTO>,

    @JsonProperty("note") val note: String? = null
)

