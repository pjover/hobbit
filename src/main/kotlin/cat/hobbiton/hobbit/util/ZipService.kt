package cat.hobbiton.hobbit.util

import org.springframework.core.io.Resource

interface ZipService {
    fun zipFiles(files: List<ZipFile>): Resource
}