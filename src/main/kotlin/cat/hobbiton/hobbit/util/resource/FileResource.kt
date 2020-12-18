package cat.hobbiton.hobbit.util.resource

import org.springframework.core.io.ByteArrayResource

class FileResource(
    byteArray: ByteArray,
    private val fileName: String
) : ByteArrayResource(byteArray, fileName) {

    override fun getFilename() = fileName
}
