package cat.hobbiton.hobbit.domain

enum class AdultRole(
        val initial: String,
        val order: Int
) {
    MOTHER("M", 1),
    FATHER("F", 2),
    TUTOR("T", 3)
}
