package cat.hobbiton.hobbit.util

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.nio.charset.StandardCharsets
import java.util.zip.ZipInputStream

class ZipServiceImplTest : DescribeSpec() {

    init {
        val sut = ZipServiceImpl()

        describe("zipFiles") {
            val files = listOf(
                ZipFile("file.1", "test".byteInputStream(StandardCharsets.UTF_8)),
                ZipFile("file.2", "test".byteInputStream(StandardCharsets.UTF_8)),
                ZipFile("file.3", "test".byteInputStream(StandardCharsets.UTF_8))
            )

            val actual = sut.zipFiles(files)

            val zis = ZipInputStream(actual.inputStream)

            it("contains the files") {
                zis.nextEntry.name shouldBe "file.1"
                zis.nextEntry.name shouldBe "file.2"
                zis.nextEntry.name shouldBe "file.3"
            }
        }
    }
}