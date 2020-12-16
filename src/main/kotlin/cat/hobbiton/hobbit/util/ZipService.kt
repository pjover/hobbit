package cat.hobbiton.hobbit.util

interface ZipService {
    fun zipFiles(files: List<ByteArrayFilenameResource>, zipFileName: String): ByteArrayFilenameResource
}