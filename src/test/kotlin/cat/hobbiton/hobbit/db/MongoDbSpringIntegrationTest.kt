package cat.hobbiton.hobbit.db

import com.mongodb.BasicDBObjectBuilder
import com.mongodb.DBObject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@DataMongoTest
@ExtendWith(SpringExtension::class)
class MongoDbSpringIntegrationTest {

    @DisplayName("save object using MongoDB template")
    @Test
    fun save(@Autowired mongoTemplate: MongoTemplate) {
        // given an object to save
        val objectToSave = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get()

        // when save object using MongoDB template
        mongoTemplate.save(objectToSave, "collection")

        // then the object is saved
        Assertions.assertThat(mongoTemplate.findAll(DBObject::class.java, "collection")).extracting("key")
                .containsOnly("value")
    }
}