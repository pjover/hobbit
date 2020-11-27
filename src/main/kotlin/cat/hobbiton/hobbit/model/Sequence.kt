package cat.hobbiton.hobbit.model

import org.springframework.data.annotation.Id

data class Sequence(
        @Id val id: SequenceType,
        val counter: Int
)
