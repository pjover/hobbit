package cat.hobbiton.hobbit.model

import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.TextScore
import java.util.*

data class Adult(
        @TextIndexed(weight = 10F) val name: String,
        @TextIndexed(weight = 7F) val surname: String,
        val secondSurname: String? = null,
        val taxId: String? = null,
        val role: AdultRole,
        val address: Address? = null,
        val email: String? = null,
        val mobilePhone: String? = null,
        val homePhone: String? = null,
        val grandMotherPhone: String? = null,
        val grandParentPhone: String? = null,
        val workPhone: String? = null,
        val birthDate: Date? = null,
        val nationality: String? = null,
        @TextScore val score: Double = 0.0
)
