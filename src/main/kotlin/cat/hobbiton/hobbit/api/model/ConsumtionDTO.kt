package cat.hobbiton.hobbit.api.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Consumption item of a child
 * @param productId
 * @param units
 * @param grossAmount
 * @param note
 */
data class ConsumtionDTO(

        @get:NotNull
        @JsonProperty("productId") val productId: kotlin.String,

        @get:NotNull
        @JsonProperty("units") val units: kotlin.Double,

        @get:NotNull
        @JsonProperty("grossAmount") val grossAmount: kotlin.Double,

        @JsonProperty("note") val note: kotlin.String? = null
)

