package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.model.Sequence

const val SEQUENCE_SEPARATOR = '-'

fun Sequence.formattedText() = "${id.prefix}$SEQUENCE_SEPARATOR$counter"

fun String.extractCounter(): Int {
    val parts = this.split(SEQUENCE_SEPARATOR)
    return parts[1].toInt()
}


