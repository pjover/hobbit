package cat.hobbiton.hobbit.api.model

import java.util.Objects
import cat.hobbiton.hobbit.api.model.SetConsumtionDTO
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Pending consumption of a child for a year-month
 * @param code
 * @param consumptions
 */
data class SetChildConsumtionDTO(

        @get:NotNull
        @JsonProperty("code") val code: kotlin.Int,

        @get:NotNull
        @JsonProperty("consumptions") val consumptions: kotlin.collections.List<SetConsumtionDTO>
)

