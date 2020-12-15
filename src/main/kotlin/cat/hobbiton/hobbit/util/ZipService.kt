package cat.hobbiton.hobbit.util

interface ZipService {
    fun zipFiles(files: List<ZipFile>, zipFileName: String): InputStreamFilenameResource
}