package cat.hobbiton.hobbit.util

import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class ZipServiceImpl : ZipService {

    override fun zipFiles(files: List<ByteArrayFilenameResource>, zipFileName: String): ByteArrayFilenameResource {

        val os = ByteArrayOutputStream()
        os.use {
            ZipOutputStream(os).use {
                for(file in files) {
                    it.putNextEntry(ZipEntry(file.filename))
                    val bytes = ByteArray(1024)
                    var length: Int
                    while(file.inputStream.read(bytes).also { length = it } >= 0) {
                        it.write(bytes, 0, length)
                    }
                    file.inputStream.close()
                }

            }
        }
        return ByteArrayFilenameResource(os.toByteArray(), zipFileName)
    }
}

