package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Children's basic info
 * @param code
 * @param shortName
 */
data class ChildListDTO(

        @JsonProperty("code") val code: Int? = null,

        @JsonProperty("shortName") val shortName: String? = null
)

