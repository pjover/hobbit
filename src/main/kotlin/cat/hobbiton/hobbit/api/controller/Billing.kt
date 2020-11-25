package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.SetYearMonthConsumptionsDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
import cat.hobbiton.hobbit.service.billing.BillingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.Valid

import kotlin.collections.List

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class BillingController(@Autowired(required = true) val service: BillingService) {


    @RequestMapping(
            value = ["/billing/consumptions"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getConsumptions(@RequestParam(value = "childCode", required = false) childCode: Int?
    ): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.getConsumptions(childCode), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/billing/consumptions"],
            produces = ["application/hal+json"],
            consumes = ["application/json"],
            method = [RequestMethod.POST])
    fun setConsumptions(@Valid @RequestBody setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO?
    ): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.setConsumptions(setYearMonthConsumptionsDTO), HttpStatus.valueOf(201))
    }
}
