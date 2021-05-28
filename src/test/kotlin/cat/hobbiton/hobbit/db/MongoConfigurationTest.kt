package cat.hobbiton.hobbit.db

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.assertFailsWith

class MongoConfigurationTest : DescribeSpec() {

    init {

        describe("getDatabaseName") {

            context("and dbName is empty") {
                val sut = MongoConfiguration("", "host", 8888)

                val executor = {
                    ReflectionTestUtils.invokeMethod<String>(sut, "getDatabaseName")
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Missing configuration value for ´db.name´"
                }
            }
        }
    }

}