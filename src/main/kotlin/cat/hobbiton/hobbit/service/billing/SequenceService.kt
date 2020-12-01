package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType

interface SequenceService {
    fun increment(sequenceType: SequenceType): Sequence
    fun decrement(sequenceType: SequenceType): Sequence
}