package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
import cat.hobbiton.hobbit.service.billing.BillingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class BillingController(@Autowired(required = true) val service: BillingService) {


    @RequestMapping(
            value = ["/billing/consumptions"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getConsumptions(@RequestParam(value = "childrenCode", required = false) childrenCode: kotlin.Int?
    ): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.getConsumptions(childrenCode), HttpStatus.valueOf(200))
    }
}
