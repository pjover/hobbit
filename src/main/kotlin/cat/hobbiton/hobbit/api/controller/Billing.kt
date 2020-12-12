package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.service.billing.BillingService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class BillingController(@Autowired(required = true) val service: BillingService) {


    @Operation(
        description = "Simulates billing the current consumption before generating the invoices",
        operationId = "getInvoices"
    )
    @RequestMapping(
        value = ["/billing/billConsumptions"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun getInvoices(): ResponseEntity<List<PaymentTypeInvoicesDTO>> {
        return ResponseEntity(service.getInvoices(), HttpStatus.valueOf(200))
    }


    @Operation(
        description = "Bills the current consumption and generates the invoices into the system",
        operationId = "setInvoices"
    )
    @RequestMapping(
        value = ["/billing/billConsumptions"],
        produces = ["application/json"],
        method = [RequestMethod.POST])
    fun setInvoices(): ResponseEntity<List<PaymentTypeInvoicesDTO>> {
        return ResponseEntity(service.setInvoices(), HttpStatus.valueOf(201))
    }
}
