package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * List of customers' emails for a concrete group
 * @param group The children's group
 * @param emails The customers email
 */
data class EmailsGroupDTO(

        @JsonProperty("group") val group: String? = null,

        @JsonProperty("emails") val emails: List<String>? = null
)

