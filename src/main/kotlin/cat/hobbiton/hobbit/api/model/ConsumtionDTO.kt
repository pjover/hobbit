package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Consumption item of a child
 * @param productId
 * @param units
 * @param grossAmount
 * @param note
 */
data class ConsumtionDTO(

        @get:NotNull
        @JsonProperty("productId") val productId: String,

        @get:NotNull
        @JsonProperty("units") val units: Double,

        @get:NotNull
        @JsonProperty("grossAmount") val grossAmount: Double,

        @JsonProperty("note") val note: String? = null
)

