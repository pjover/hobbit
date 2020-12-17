package cat.hobbiton.hobbit.util.crypt

import org.jasypt.util.text.BasicTextEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CryptServiceImpl(
    @Value("\${apiKey}") private val seed: String
) : CryptService {

    override fun decrypt(encryptedText: String): String {

        val textEncryptor = BasicTextEncryptor()
        textEncryptor.setPassword(seed)
        return textEncryptor.decrypt(encryptedText)
    }

    override fun encrypt(decryptedText: String): String {

        val textEncryptor = BasicTextEncryptor()
        textEncryptor.setPassword(seed)
        return textEncryptor.encrypt(decryptedText)
    }
}
