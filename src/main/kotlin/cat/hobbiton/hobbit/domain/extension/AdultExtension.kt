package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Adult

fun Adult.shortName(): String = "$name $surname"

fun Adult.completeName(): String {
    return if (secondSurname.isNullOrBlank()) shortName()
    else "${shortName()} $secondSurname"
}
