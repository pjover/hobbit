package cat.hobbiton.hobbit.domain.aux

import org.apache.commons.lang3.RegExUtils
import org.apache.commons.lang3.StringUtils
import java.math.BigInteger

fun String.isValidBankAccount(): Boolean {
    return if (this.isBlank()) false
    else isCodeValid(prepare(this))
}

private fun prepare(code: String): String {
    return code
            .trim()
            .capitalize()
            .replace(" ", "")
            .replace("-", "")
}

fun isCodeValid(code: String): Boolean {
    return getControlCode(code) == computeControlCode(moveCountryCodeToTheEnd(code))
}

private fun getControlCode(code: String): String {
    return code.substring(2, 4)
}

private fun moveCountryCodeToTheEnd(code: String): String {
    val formattedCode = formatCode(code)
    return formattedCode.substring(4) + getCountryCode(formattedCode)
}

fun formatCode(rawCode: String?): String {
    return if (rawCode == null || rawCode.isBlank()) "" else RegExUtils.removeAll(rawCode.toUpperCase(), "[\\s-]")
}

private fun getCountryCode(code: String): String {
    return code.substring(0, 2)
}

fun computeControlCode(code: String): String {
    return apply9710Model(assignWeightsToLetters(prepareParam(code)))
}

private fun prepareParam(rawCode: String): String {
    return formatCode(rawCode) + "00"
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
