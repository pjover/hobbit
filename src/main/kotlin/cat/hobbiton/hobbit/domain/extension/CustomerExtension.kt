package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Adult
import cat.hobbiton.hobbit.domain.AdultRole
import cat.hobbiton.hobbit.domain.Child
import cat.hobbiton.hobbit.domain.Customer
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate


fun Customer.getActiveChildren(): List<Child> = children.filter { it.active }

fun Customer.getActiveChildrenCodes(): List<Int> {
    return children.filter { it.active }
            .map { child -> child.code }
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

fun Customer.validate() {

    require(children.isNotEmpty()) {
        ValidationMessages.ERROR_CUSTOMER_WHITOUT_CHILD.translate()
    }

    require(adults.isNotEmpty()) {
        ValidationMessages.ERROR_CUSTOMER_WHITOUT_ADULT.translate()
    }

    children.forEach { it.validate() }

    adults.forEach { it.validate() }

    invoiceHolder.validate()
}