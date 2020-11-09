package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Sequence
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.util.translate
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler(Sequence::class)
class SequenceEventHandler {

    @HandleBeforeSave
    @HandleBeforeCreate
    fun validate(sequence: Sequence) {
        require(sequence.counter > 0) { ValidationMessages.ERROR_SEQUENCE_COUNTER_INVALID.translate() }
    }
}