package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.model.PaymentType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties(prefix = "bdd")
class BddProperties {
    lateinit var businessName: String
    lateinit var addressLine1: String
    lateinit var addressLine2: String
    lateinit var addressLine3: String
    lateinit var addressLine4: String
    lateinit var taxIdLine: String
    lateinit var bddBusinessPrefix: String
    lateinit var bddBusinessId: String
    lateinit var bddBusinessIban: String
    lateinit var bddCountry: String
    lateinit var bddBankBic: String
    lateinit var bddPurposeCode: String
    lateinit var paymentTypeNotes: MutableMap<String, String>

    @PostConstruct
    private fun init() {
        enumValues<PaymentType>().forEach {
            require(paymentTypeNotes.containsKey(it.name)) { "El valor de configuració 'paymentTypeNotes.${it.name}' no està informat" }
        }
    }

    fun getPaymentTypeNotes(paymentType: PaymentType): String {
        return paymentTypeNotes[paymentType.name]!!
    }
}
