package cat.hobbiton.hobbit.util.resource

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.nio.charset.StandardCharsets
import java.util.zip.ZipInputStream

class ZipServiceImplTest : DescribeSpec() {

    init {
        val sut = ZipServiceImpl()

        describe("zipFiles") {
            val files = listOf(
                FileResource("test".toByteArray(StandardCharsets.UTF_8), "file.1"),
                FileResource("test".toByteArray(StandardCharsets.UTF_8), "file.2"),
                FileResource("test".toByteArray(StandardCharsets.UTF_8), "file.3")
            )

            val actual = sut.zipFiles(files, "file.zip")

            it("returns the filename") {
                actual.filename shouldBe "file.zip"
            }

            val zis = ZipInputStream(actual.inputStream)
            it("contains the files") {
                zis.nextEntry.name shouldBe "file.1"
                zis.nextEntry.name shouldBe "file.2"
                zis.nextEntry.name shouldBe "file.3"
            }
        }
    }
}