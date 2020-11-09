package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Child

fun Child.shortName(): String = "$name $surname"

fun Child.completeName(): String {
    return if (secondSurname.isNullOrBlank()) shortName()
    else "${shortName()} $secondSurname"
}
