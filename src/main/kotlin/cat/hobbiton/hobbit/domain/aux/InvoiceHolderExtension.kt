package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.InvoiceHolder

fun InvoiceHolder.emailText() = "$name <$email>"

fun InvoiceHolder.isEmailConfigured(): Boolean = email?.isValidEmail() ?: false
