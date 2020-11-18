package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * List of children's basic info
 * @param children
 */
data class ChildrenListDTO(

        @JsonProperty("children") val children: List<ChildListDTO>? = null
)

