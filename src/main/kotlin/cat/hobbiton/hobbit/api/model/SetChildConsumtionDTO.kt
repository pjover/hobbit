package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Pending consumption of a child for a year-month
 * @param code
 * @param consumptions
 */
data class SetChildConsumtionDTO(

        @JsonProperty("code") val code: Int? = null,

        @JsonProperty("consumptions") val consumptions: List<SetConsumtionDTO>? = null
)

