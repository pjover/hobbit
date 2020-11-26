package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Pending consumptions for a year-month
 * @param yearMonth
 * @param grossAmount
 * @param children
 */
data class YearMonthConsumptionsDTO(

        @get:NotNull
        @JsonProperty("yearMonth") val yearMonth: String,

        @get:NotNull
        @JsonProperty("grossAmount") val grossAmount: Double,

        @get:NotNull
        @JsonProperty("children") val children: List<ChildConsumtionDTO>
)

