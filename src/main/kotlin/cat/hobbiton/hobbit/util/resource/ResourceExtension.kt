package cat.hobbiton.hobbit.util.resource

import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType


fun Resource.getResponseHeaders(mediaType: MediaType): HttpHeaders {
    val headers = HttpHeaders()
    headers.contentType = mediaType
    headers.contentLength = this.contentLength()
    headers.setContentDispositionFormData("attachment", filename)
    return headers
}