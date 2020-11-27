package cat.hobbiton.hobbit.db.eventhandler

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.extension.validate
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