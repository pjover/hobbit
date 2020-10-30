package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Adult
import cat.hobbiton.hobbit.domain.AdultRole
import cat.hobbiton.hobbit.domain.Child
import cat.hobbiton.hobbit.domain.Customer


fun Customer.formattedText(): String {
    val adult = getAdult(AdultRole.MOTHER) ?: adults.first()
    return if (active) "[$id] ${activeAndInactiveChildrenText()} : ${adult.shortName()}"
    else "($id) ${activeAndInactiveChildrenText()} : ${adult.shortName()}"
}

fun Customer.getActiveChildren(): List<Child> = children.filter { it.active }

fun Customer.activeChildrenText(): String {
    return getActiveChildren().joinToString { child -> child.formattedText() }
}

fun Customer.activeAndInactiveChildrenText(): String {
    return children.joinToString { child -> child.formattedText() }
}

fun Customer.getActiveChildrenCodes(): List<Int> {
    return children.filter { it.active }
            .map { child -> child.code }
}

fun Customer.getActiveChildrenNames(): List<String> {
    return children.filter { it.active }
            .map { child -> child.name }
}

fun Customer.getChild(code: Int): Child? {
    val list = children.filter { it.code == code }
    return if (list.isEmpty()) null
    else list.first()
}

fun Customer.getAdult(role: AdultRole): Adult? {
    val list = adults.filter { it.role == role }
    return if (list.isEmpty()) null
    else list.first()
}

fun Customer.emailText() = invoiceHolder.emailText()
