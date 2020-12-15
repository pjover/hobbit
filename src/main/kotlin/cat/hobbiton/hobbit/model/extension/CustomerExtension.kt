package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.Adult
import cat.hobbiton.hobbit.model.AdultRole
import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.util.error.AppException
import cat.hobbiton.hobbit.util.i18n.translate


fun Customer.getActiveChildren(): List<Child> = children.filter { it.active }

fun Customer.getActiveChildrenCodes(): List<Int> {
    return children.filter { it.active }
        .map { child -> child.code }
}

fun Customer.getChild(code: Int): Child {
    val list = children.filter { it.code == code }
    if(list.isEmpty()) throw AppException(ErrorMessages.ERROR_CHILD_NOT_FOUND, id)
    return list.first()
}

fun Customer.getAdult(role: AdultRole): Adult? {
    val list = adults.filter { it.role == role }
    return if(list.isEmpty()) null
    else list.first()
}

fun Customer.getFirstAdult() = adults.minByOrNull { it.role.order }!!

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