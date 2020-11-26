package cat.hobbiton.hobbit.api.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Group where the children are
 * Values: ALL,EI_1,EI_2,EI_3
 */
enum class GroupDTO(val value: kotlin.String) {

    ALL("ALL"),

    EI_1("EI_1"),

    EI_2("EI_2"),

    EI_3("EI_3");

}

