package cat.hobbiton.hobbit.model

import cat.hobbiton.hobbit.model.extension.calculateGroup
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.TextScore
import java.time.LocalDate

data class Child(
        @TextIndexed(weight = 10F) val code: Int = 0,
        val name: String,
        val surname: String,
        val secondSurname: String? = null,
        val taxId: String? = null,
        val birthDate: LocalDate,
        val group: GroupType = birthDate.calculateGroup(),
        val note: String? = null,
        val active: Boolean = true,
        @TextScore val score: Double = 0.0
)
