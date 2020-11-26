package cat.hobbiton.hobbit.api.model

import java.util.Objects
import cat.hobbiton.hobbit.api.model.ChildConsumtionDTO
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Pending consumptions for a year-month
 * @param yearMonth
 * @param grossAmount
 * @param children
 */
data class YearMonthConsumptionsDTO(

        @get:NotNull
        @JsonProperty("yearMonth") val yearMonth: kotlin.String,

        @get:NotNull
        @JsonProperty("grossAmount") val grossAmount: kotlin.Double,

        @get:NotNull
        @JsonProperty("children") val children: kotlin.collections.List<ChildConsumtionDTO>
)

