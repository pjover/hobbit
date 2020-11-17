package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * List of children's basic info for a concrete group
 * @param group The children's group
 * @param children
 */
data class ChildrenGroupDTO(

        @JsonProperty("group") val group: String? = null,

        @JsonProperty("children") val children: List<ChildListDTO>? = null
) {

}

