package cat.hobbiton.hobbit.util

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EncryptorConfigurationTest : DescribeSpec() {

    init {

        val apiKey = "98162313-dc76-4ac9-b18c-8fd23e938085"
        val sut = EncryptorConfiguration(apiKey)

        describe("encrypt and decrypt") {

            val clearText = "Some clear text"
            val encryptedText = sut.stringEncryptor().encrypt(clearText)

            it("encrypts the original clear text") {
                encryptedText shouldNotBe clearText
            }

            val decryptedText = sut.stringEncryptor().decrypt(encryptedText)

            it("decrypts to the original clear text") {
                decryptedText shouldBe clearText
            }
        }
    }
}