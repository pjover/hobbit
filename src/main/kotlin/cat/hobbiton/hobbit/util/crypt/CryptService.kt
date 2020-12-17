package cat.hobbiton.hobbit.util.crypt

interface CryptService {
    fun decrypt(encryptedText: String): String
    fun encrypt(decryptedText: String): String
}