package cat.hobbiton.hobbit.api.model

import java.util.Objects
import cat.hobbiton.hobbit.api.model.ChildListDTO
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * List of children's basic info for a concrete group
 * @param group The children's group
 * @param children
 */
data class ChildrenGroupDTO(

        @JsonProperty("group") val group: kotlin.String? = null,

        @JsonProperty("children") val children: kotlin.collections.List<ChildListDTO>? = null
)

