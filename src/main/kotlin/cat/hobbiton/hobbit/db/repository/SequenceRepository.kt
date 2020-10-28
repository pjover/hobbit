package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.domain.Sequence
import cat.hobbiton.hobbit.domain.SequenceType
import org.springframework.data.mongodb.repository.MongoRepository

interface SequenceRepository : MongoRepository<Sequence, SequenceType>
