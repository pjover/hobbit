package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Product

fun Product.formattedText() = "[$id] $name (${price.setScale(2)}€)"
