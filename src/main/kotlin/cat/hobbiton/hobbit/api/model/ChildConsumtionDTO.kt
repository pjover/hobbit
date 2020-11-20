package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Pending consumption of a child for a year-month
 * @param code
 * @param shortName
 * @param consumptions
 */
data class ChildConsumtionDTO(

        @JsonProperty("code") val code: kotlin.Int? = null,

        @JsonProperty("shortName") val shortName: kotlin.String? = null,

        @JsonProperty("consumptions") val consumptions: kotlin.collections.List<ConsumtionDTO>? = null
)

