package cat.hobbiton.hobbit.service.init

import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.util.AppException
import cat.hobbiton.hobbit.util.translate
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
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

    fun getPaymentTypeNotes(paymentType: PaymentType): String {
        require(paymentTypeNotes.containsKey(paymentType.name)) { ValidationMessages.ERROR_INVOICE_HOLDER_EMAIL_INVALID.translate(paymentType.name) }
        return paymentTypeNotes[paymentType.name]!!
    }
}
