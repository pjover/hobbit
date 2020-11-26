package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Pending consumption of a child for a year-month
 * @param code
 * @param shortName
 * @param grossAmount
 * @param consumptions
 */
data class ChildConsumtionDTO(

        @get:NotNull
        @JsonProperty("code") val code: Int,

        @get:NotNull
        @JsonProperty("shortName") val shortName: String,

        @get:NotNull
        @JsonProperty("grossAmount") val grossAmount: Double,

        @get:NotNull
        @JsonProperty("consumptions") val consumptions: List<ConsumtionDTO>
)

