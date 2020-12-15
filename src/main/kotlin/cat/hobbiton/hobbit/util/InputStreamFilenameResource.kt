package cat.hobbiton.hobbit.util

import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.io.InputStream

class InputStreamFilenameResource(
    inputStream: InputStream,
    private val fileName: String
) : InputStreamResource(inputStream, fileName) {

    override fun getFilename() = fileName
}

fun Resource.getResponseHeaders(mediaType: MediaType): HttpHeaders {
    val headers = HttpHeaders()
    headers.contentType = mediaType
    headers.contentLength = this.contentLength()
    headers.setContentDispositionFormData("attachment", filename)
    return headers
}