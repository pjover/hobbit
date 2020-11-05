package cat.hobbiton.hobbit.domain

import java.time.LocalDate

data class Child(
        val code: Int,
        val name: String,
        val surname: String,
        val secondSurname: String? = null,
        val taxId: String? = null,
        val birthDate: LocalDate,
        val group: GroupType = calculateCourse(birthDate),
        val note: String? = null,
        val active: Boolean = true
)

private fun calculateCourse(birthDate: LocalDate): GroupType {
    return when (LocalDate.now().year - birthDate.year) {
        0 -> GroupType.EI_1
        1 -> GroupType.EI_2
        2 -> GroupType.EI_3
        else -> GroupType.UNDEFINED
    }
}
