package cat.hobbiton.hobbit.db.repository

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductRepositoryIntegrationTests {

    @LocalServerPort
    private val port = 0

    @Autowired
    private val restTemplate: TestRestTemplate? = null

    @Test
    @Throws(Exception::class)
    fun products() {
        val response = restTemplate?.getForEntity(
                "http://localhost:$port/products?page=0&size=2",
                String::class.java)!!

        assertThat(response.statusCode, equalTo(HttpStatus.OK))
    }
}