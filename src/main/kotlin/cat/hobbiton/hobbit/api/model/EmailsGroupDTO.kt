package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * List of customers' emails for a concrete group
 * @param emails The customers email
 * @param group
 */
data class EmailsGroupDTO(

    @get:NotNull
    @JsonProperty("emails") val emails: String,

    @JsonProperty("group") val group: GroupDTO? = null
)

