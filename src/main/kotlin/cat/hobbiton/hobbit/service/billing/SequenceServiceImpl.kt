package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.db.repository.SequenceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import cat.hobbiton.hobbit.util.AppException
import org.springframework.stereotype.Service

@Service
class SequenceServiceImpl(
    private val sequenceRepository: SequenceRepository
) : SequenceService {

    override fun increment(sequenceType: SequenceType): Sequence {
        val sequence = getSequence(sequenceType)
        val next = Sequence(sequenceType, sequence.counter + 1)
        return sequenceRepository.save(next)
    }

    override fun decrement(sequenceType: SequenceType): Sequence {
        val sequence = getSequence(sequenceType)
        val prev = Sequence(sequenceType, sequence.counter - 1)
        return sequenceRepository.save(prev)
    }

    private fun getSequence(sequenceType: SequenceType): Sequence {
        return sequenceRepository.findById(sequenceType)
            .orElseThrow { AppException(ErrorMessages.ERROR_SEQUENCE_NOT_FOUND, sequenceType) }
    }
}