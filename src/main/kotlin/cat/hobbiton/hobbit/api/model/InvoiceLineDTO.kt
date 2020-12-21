package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * An invoice line
 * @param productId
 * @param units
 * @param totalAmount
 * @param childCode The child code
 */
data class InvoiceLineDTO(

    @get:NotNull
    @JsonProperty("productId") val productId: String,

    @get:NotNull
    @JsonProperty("units") val units: java.math.BigDecimal,

    @get:NotNull
    @JsonProperty("totalAmount") val totalAmount: java.math.BigDecimal,

    @get:NotNull
    @JsonProperty("childCode") val childCode: Int
)

