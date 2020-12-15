package cat.hobbiton.hobbit.util

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class ByteArrayFilenameResource(
    byteArray: ByteArray,
    private val fileName: String
) : ByteArrayResource(byteArray, fileName) {

    override fun getFilename() = fileName
}

fun Resource.getResponseHeaders(mediaType: MediaType): HttpHeaders {
    val headers = HttpHeaders()
    headers.contentType = mediaType
    headers.contentLength = this.contentLength()
    headers.setContentDispositionFormData("attachment", filename)
    return headers
}