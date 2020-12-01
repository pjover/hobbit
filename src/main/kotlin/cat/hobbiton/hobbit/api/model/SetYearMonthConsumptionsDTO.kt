package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Pending consumptions for a year-month
 * @param yearMonth
 * @param children
 */
data class SetYearMonthConsumptionsDTO(

    @get:NotNull
    @JsonProperty("yearMonth") val yearMonth: String,

    @get:NotNull
    @JsonProperty("children") val children: List<SetChildConsumtionDTO>
)

