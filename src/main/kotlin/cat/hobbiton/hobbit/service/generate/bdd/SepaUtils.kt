package cat.hobbiton.hobbit.service.generate.bdd

import org.apache.commons.lang3.RegExUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class SepaUtils {

    fun isValidIban(iban: String?): Boolean {
        return if(iban == null || iban.isEmpty()) {
            false
        } else calculateControlCode(moveCountryCodeToTheEnd(iban)) == getControlCode(iban)
    }

    private fun moveCountryCodeToTheEnd(iban: String): String {
        val formattedCode = formatCode(iban)
        return formattedCode.substring(4) + getCountryCode(formattedCode)
    }

    private fun getCountryCode(iban: String): String {
        return iban.substring(0, 2)
    }

    private fun getControlCode(iban: String): String {
        return iban.substring(2, 4)
    }

    fun getSepaIndentifier(taxId: String): String {
        return getSepaIndentifier("ES", "000", taxId)
    }

    fun getSepaIndentifier(countryCode: String, suffix: String?, taxId: String): String {
        return countryCode.toUpperCase() +
            calculateControlCode(taxId, countryCode) +
            StringUtils.leftPad(suffix, 3, '0') +
            StringUtils.leftPad(taxId.toUpperCase(), 9, '0')
    }

    fun calculateControlCode(vararg params: String): String {
        return apply9710Model(assignWeightsToLetters(prepareParams(*params)))
    }

    fun formatCode(rawCode: String?): String {
        return if(rawCode == null || rawCode.isBlank()) "" else RegExUtils.removeAll(rawCode.toUpperCase(), "[\\s-]")
    }

    private fun prepareParam(rawCode: String): String {
        return formatCode(rawCode) + "00"
    }

    private fun prepareParams(vararg params: String): String {
        val rawCode = listOf(*params).joinToString("")
        return prepareParam(rawCode)
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
}
