package cat.hobbiton.hobbit.util.file

interface ZipService {
    fun zipFiles(files: List<FileResource>, zipFileName: String): FileResource
}