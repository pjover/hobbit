package cat.hobbiton.hobbit.api.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Children's basic info
 * @param shortName
 * @param code
 */
data class ChildListDTO(

        @JsonProperty("shortName") val shortName: kotlin.String? = null,

        @JsonProperty("code") val code: kotlin.Int? = null
) {

}

