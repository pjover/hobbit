package cat.hobbiton.hobbit.service.init

import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.util.Logging
import cat.hobbiton.hobbit.util.translate
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties(prefix = "business")
class BusinessProperties {
    lateinit var businessName: String
    lateinit var addressLine1: String
    lateinit var addressLine2: String
    lateinit var addressLine3: String
    lateinit var addressLine4: String
    lateinit var taxIdLine: String
    lateinit var bddBusinessPrefix: String
    lateinit var bddBusinessId: String
    lateinit var bddBusinessIban: String
    lateinit var bddBankBic: String
    lateinit var bddCountry: String
    lateinit var bddPurposeCode: String
    lateinit var paymentTypeNotes: MutableMap<String, String>
    lateinit var locale: String
    lateinit var longDateFormat: String

    private val logger by Logging()

    @PostConstruct
    private fun init() {
        enumValues<PaymentType>().forEach {
            require(paymentTypeNotes.containsKey(it.name)) { ValidationMessages.ERROR_PAYMENT_TYPE_CONFIGURATION_UNDEFINED.translate(it.name) }
        }
        logger.info("Loaded business info for '$businessName'")
    }


    fun getPaymentTypeNotes(paymentType: PaymentType): String {
        return paymentTypeNotes[paymentType.name]!!
    }
}
