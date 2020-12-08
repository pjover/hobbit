package cat.hobbiton.hobbit.model.extension

import org.apache.commons.lang3.RegExUtils
import org.apache.commons.lang3.StringUtils
import java.math.BigInteger

private const val CIF_LETTERS = "ABCDEFGHJKLMNPQRSVW"
private const val CIF_HEAD_LETTERS = "NPQRSWJUF"
private const val CIF_TAIL_LETTERS = "JABCDEFGHI"
private const val NIE_LETTERS = "XYZ"
private const val NIF_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE"

fun String.isValidTaxId(): Boolean {
    if(this.isBlank()) return false

    val value: String = this.trim().capitalize()
    val letter: String = value.substring(0, 1)
    return when {
        CIF_LETTERS.contains(letter) -> isValidCif(value)
        NIE_LETTERS.contains(letter) -> isValidNie(value)
        else -> isValidNif(value)
    }
}

private fun isValidCif(value: String) = computeCif(value) == value

private fun computeCif(value: String): String {
    // From http://www.jagar.es/Economia/Ccif.htm

    val withoutTail = removeTail(value)
    val body = withoutTail.substring(1, 8)
    val head = withoutTail.substring(0, 1)

    val totalSum = totalSum(body)

    val tail = if (CIF_HEAD_LETTERS.contains(head)) {
        CIF_TAIL_LETTERS[totalSum].toString()
    } else {
        totalSum.toString()
    }
    return withoutTail + tail
}

private fun removeTail(value: String) = value.substring(0, 8)

private fun totalSum(body: String): Int {
    val partialSum = pairSum(body) + oddSum(body)
    var totalSum = 10 - partialSum % 10
    if (totalSum == 10) totalSum = 0
    return totalSum
}

private fun pairSum(body: String): Int {
    var sum = 0
    run {
        var i = 1
        while (i < body.length) {
            val aux = body[i].toString().toInt()
            sum += aux
            i += 2
        }
    }
    return sum
}

private fun oddSum(body: String): Int {
    var sum = 0
    run {
        var i = 0
        while (i < body.length) {
            sum += oddPosition(body[i].toString())
            i += 2
        }
    }
    return sum
}

private fun oddPosition(str: String): Int {
    val aux = str.toInt() * 2
    return aux / 10 + aux % 10
}

private fun isValidNie(value: String) = computeNie(value) == value

private fun computeNie(value: String): String {
    val withoutTail = removeTail(value)
    val seed: String
    seed = when {
        withoutTail.startsWith("X") -> withoutTail.replace('X', '0')
        withoutTail.startsWith("Y") -> withoutTail.replace('Y', '1')
        withoutTail.startsWith("Z") -> withoutTail.replace('Z', '2')
        else -> return withoutTail // Invalid NIE
    }
    return withoutTail + computeLetter(seed)
}

private fun computeLetter(seed: String): Char {
    return NIF_LETTERS[seed.toInt() % 23]
}

private fun isValidNif(value: String) = computeNif(value) == value

private fun computeNif(value: String): String {
    val withoutTail = completeWithZeroes(removeTail(value))
    return withoutTail + computeLetter(withoutTail)
}

private fun completeWithZeroes(str: String): String {
    var completedWithZeroes = str
    while(completedWithZeroes.length < 8) completedWithZeroes = "0$completedWithZeroes"
    return completedWithZeroes
}

fun String.getSepaIndentifier(countryCode: String, suffix: String): String {
    return countryCode.toUpperCase() +
        calculateControlCode(this, countryCode) +
        StringUtils.leftPad(suffix, 3, '0') +
        StringUtils.leftPad(this.toUpperCase(), 9, '0')
}

private fun calculateControlCode(vararg params: String): String {
    return apply9710Model(assignWeightsToLetters(prepareParams(*params)))
}


private fun prepareParams(vararg params: String): String {
    val rawCode = listOf(*params).joinToString("")
    return prepareParam(rawCode)
}

private fun prepareParam(rawCode: String): String {
    return if(rawCode.isBlank()) "00"
    else RegExUtils.removeAll(rawCode.toUpperCase(), "[\\s-]") + "00"
}

private fun assignWeightsToLetters(code: String): String {
    return code.toCharArray().joinToString("") { Character.getNumericValue(it).toString() }
}

private fun apply9710Model(pre: String): String {
    //Applies the 97-10 model according to ISO-7604 (http://is.gd/9HE1zs)
    var id = BigInteger(pre)
        .mod(BigInteger("97"))
        .toInt()
    id = 98 - id
    return StringUtils.leftPad(id.toString(), 2, '0')
}