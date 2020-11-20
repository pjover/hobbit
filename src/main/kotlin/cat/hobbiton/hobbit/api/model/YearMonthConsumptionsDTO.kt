package cat.hobbiton.hobbit.api.model

import cat.hobbiton.hobbit.api.model.String
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Pending consumptions for a year-month
 * @param yearMonth
 * @param children
 */
data class YearMonthConsumptionsDTO(

        @JsonProperty("yearMonth") val yearMonth: String? = null,

        @JsonProperty("children") val children: kotlin.collections.List<ChildConsumtionDTO>? = null
)

