package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Adult

fun Adult.shortName(): String = "$name $surname"

fun Adult.completeName(): String {
    return if (secondSurname.isNullOrBlank()) shortName()
    else "${shortName()} $secondSurname"
}

fun Adult.formattedText(): String {
    val em = if (!email.isNullOrBlank()) " <$email>" else ""
    val ph = if (!mobilePhone.isNullOrBlank()) " ($mobilePhone)" else ""
    return "[${role.initial}] ${shortName()}$ph$em"
}
