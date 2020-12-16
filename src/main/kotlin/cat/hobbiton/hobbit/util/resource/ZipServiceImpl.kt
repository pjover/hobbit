package cat.hobbiton.hobbit.util.resource

import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class ZipServiceImpl : ZipService {

    override fun zipFiles(files: List<FileResource>, zipFileName: String): FileResource {

        val os = ByteArrayOutputStream()
        os.use {
            ZipOutputStream(os).use {
                files.forEach { fileResource ->
                    it.putNextEntry(ZipEntry(fileResource.filename))
                    it.write(fileResource.byteArray)
                }
            }
        }
        return FileResource(os.toByteArray(), zipFileName)
    }
}

