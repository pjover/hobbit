package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Pending consumption of a child for a year-month
 * @param code
 * @param shortName
 * @param grossAmount
 * @param consumptions
 */
data class ChildConsumtionDTO(

        @JsonProperty("code") val code: Int? = null,

        @JsonProperty("shortName") val shortName: String? = null,

        @JsonProperty("grossAmount") val grossAmount: Double? = null,

        @JsonProperty("consumptions") val consumptions: List<ConsumtionDTO>? = null
)

