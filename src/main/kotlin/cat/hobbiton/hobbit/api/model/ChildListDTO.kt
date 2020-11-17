package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Children's basic info
 * @param shortName
 * @param code
 */
data class ChildListDTO(

        @JsonProperty("code") val code: Int? = null,

        @JsonProperty("shortName") val shortName: String? = null
) {

}

