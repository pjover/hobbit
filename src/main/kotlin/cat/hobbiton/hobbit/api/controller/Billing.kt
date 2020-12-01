package cat.hobbiton.hobbit.api

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.service.billing.BillingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated
import org.springframework.beans.factory.annotation.Autowired

import kotlin.collections.List

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class BillingController(@Autowired(required = true) val service: BillingService) {


    @RequestMapping(
        value = ["/billing/billConsumptions"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun getInvoices(): ResponseEntity<List<PaymentTypeInvoicesDTO>> {
        return ResponseEntity(service.getInvoices(), HttpStatus.valueOf(200))
    }


    @RequestMapping(
        value = ["/billing/billConsumptions"],
        produces = ["application/json"],
        method = [RequestMethod.POST])
    fun setInvoices(): ResponseEntity<List<PaymentTypeInvoicesDTO>> {
        return ResponseEntity(service.setInvoices(), HttpStatus.valueOf(201))
    }
}
