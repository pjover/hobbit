package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Consumption item of a child
 * @param productId
 * @param units
 * @param note
 */
data class ConsumtionDTO(

        @JsonProperty("productId") val productId: String? = null,

        @JsonProperty("units") val units: Double? = null,

        @JsonProperty("note") val note: String? = null
)

