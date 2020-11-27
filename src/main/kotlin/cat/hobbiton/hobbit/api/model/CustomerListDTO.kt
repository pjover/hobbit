package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Customer's basic info
 * @param code
 * @param shortName
 * @param children
 */
data class CustomerListDTO(

        @JsonProperty("code") val code: Int? = null,

        @JsonProperty("shortName") val shortName: String? = null,

        @JsonProperty("children") val children: List<ChildListDTO>? = null
)

