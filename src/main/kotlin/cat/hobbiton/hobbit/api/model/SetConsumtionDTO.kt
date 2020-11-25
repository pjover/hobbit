package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Consumption item of a child
 * @param productId
 * @param grossAmount
 * @param note
 */
data class SetConsumtionDTO(

        @JsonProperty("productId") val productId: String? = null,

        @JsonProperty("grossAmount") val grossAmount: Double? = null,

        @JsonProperty("note") val note: String? = null
)

