package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Pending consumptions for a year-month
 * @param yearMonth
 * @param grossAmount
 * @param children
 */
data class YearMonthConsumptionsDTO(

        @JsonProperty("yearMonth") val yearMonth: String? = null,

        @JsonProperty("grossAmount") val grossAmount: Double? = null,

        @JsonProperty("children") val children: List<ChildConsumtionDTO>? = null
)

