package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * An invoice line
 * @param productId
 * @param units
 * @param totalAmount
 */
data class InvoicesLinesDTO(

    @get:NotNull
    @JsonProperty("productId") val productId: String,

    @get:NotNull
    @JsonProperty("units") val units: Double,

    @get:NotNull
    @JsonProperty("totalAmount") val totalAmount: Double
)

