package cat.hobbiton.hobbit.domain.extension

import cat.hobbiton.hobbit.domain.Customer
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.HandleBeforeSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler(Customer::class)
class CustomerEventHandler {

    @HandleBeforeSave
    @HandleBeforeCreate
    fun validate(customer: Customer) = customer.validate()
}