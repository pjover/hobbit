package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Sequence

fun Sequence.formattedText() = "${id.prefix}-$counter"

