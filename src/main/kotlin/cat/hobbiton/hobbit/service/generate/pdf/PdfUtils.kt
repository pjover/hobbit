package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.model.Invoice

fun Invoice.getPdfName() = "${this.id} (${this.customerId}).pdf"