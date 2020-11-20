package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import org.springframework.data.mongodb.repository.MongoRepository

interface SequenceRepository : MongoRepository<Sequence, SequenceType>
