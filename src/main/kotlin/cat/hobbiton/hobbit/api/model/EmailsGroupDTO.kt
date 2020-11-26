package cat.hobbiton.hobbit.api.model

import java.util.Objects
import cat.hobbiton.hobbit.api.model.GroupDTO
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * List of customers' emails for a concrete group
 * @param group
 * @param emails The customers email
 */
data class EmailsGroupDTO(

        @JsonProperty("group") val group: GroupDTO? = null,

        @JsonProperty("emails") val emails: kotlin.collections.List<kotlin.String>? = null
)

