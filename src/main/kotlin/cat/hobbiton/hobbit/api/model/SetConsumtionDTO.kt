package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Consumption item of a child
 * @param productId
 * @param units
 * @param note
 */
data class SetConsumtionDTO(

    @get:NotNull
    @JsonProperty("productId") val productId: String,

    @get:NotNull
    @JsonProperty("units") val units: Double,

    @JsonProperty("note") val note: String? = null
)

