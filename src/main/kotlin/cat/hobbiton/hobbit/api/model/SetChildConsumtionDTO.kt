package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Pending consumption of a child for a year-month
 * @param code The child code
 * @param consumptions
 */
data class SetChildConsumtionDTO(

    @get:NotNull
    @JsonProperty("code") val code: Int,

    @get:NotNull
    @JsonProperty("consumptions") val consumptions: List<SetConsumtionDTO>
)

