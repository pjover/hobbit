package cat.hobbiton.hobbit.util.crypt

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.DescribeSpec

class CryptServiceImplTest : DescribeSpec() {

    init {
        val seed = "98162313-dc76-4ac9-b18c-8fd23e938085"
        val sut = CryptServiceImpl(seed)

        describe("encrypt and decrypt") {

            val clearText = "Some clear text"
            val encryptedText = sut.encrypt(clearText)

            it("encrypts the original clear text") {
                encryptedText shouldNotBe clearText
            }

            val decryptedText = sut.decrypt(encryptedText)

            it("decrypts to the original clear text") {
                decryptedText shouldBe clearText
            }
        }
    }
}