package cat.hobbiton.hobbit.model

import java.util.*

data class Adult(
        val name: String,
        val surname: String,
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
        val nationality: String? = null
)
