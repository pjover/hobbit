package cat.hobbiton.hobbit.util.resource

interface ZipService {
    fun zipFiles(files: List<FileResource>, zipFileName: String): FileResource
}