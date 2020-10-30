package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Child
import java.time.LocalDate

fun Child.shortName(): String = "$name $surname"

fun Child.completeName(): String {
    return if (secondSurname.isNullOrBlank()) shortName()
    else "${shortName()} $secondSurname"
}

fun Child.formattedText(): String = if (active) name else "($name)"

fun Child.longText(): String = if (active) "$name $surname" else "($name $surname)"

fun Child.listText(): String = "$name $surname, $birthDate, ${group.text}"

fun Child.wasUnderThreeYearsOldAt(localDate: LocalDate): Boolean {
    return this.birthDate.plusYears(3).isAfter(localDate)
}
