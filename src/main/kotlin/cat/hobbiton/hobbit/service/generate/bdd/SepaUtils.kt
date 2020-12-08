package cat.hobbiton.hobbit.service.generate.bdd

import org.apache.commons.lang3.RegExUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class SepaUtils {

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
