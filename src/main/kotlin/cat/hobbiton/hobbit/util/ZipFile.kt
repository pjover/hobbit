package cat.hobbiton.hobbit.util

import java.io.InputStream

data class ZipFile(
    val name: String,
    val inputStream: InputStream
)