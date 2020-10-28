package cat.hobbiton.hobbit.domain

import org.springframework.data.annotation.Id

data class Sequence(
        @Id val id: SequenceType,
        val counter: Int
)
