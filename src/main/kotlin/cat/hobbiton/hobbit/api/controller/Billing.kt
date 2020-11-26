package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.SetChildConsumtionDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
import cat.hobbiton.hobbit.service.billing.BillingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.Valid
import javax.validation.constraints.Min

import kotlin.collections.List

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class BillingController(@Autowired(required = true) val service: BillingService) {


    @RequestMapping(
            value = ["/billing/consumptions/{childCode}"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getChildConsumptions(@Min(1) @PathVariable("childCode") childCode: kotlin.Int
    ): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.getChildConsumptions(childCode), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/billing/consumptions"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getConsumptions(): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.getConsumptions(), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/billing/monthConsumptions"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getMonthConsumptions(@RequestParam(value = "yearMonth", required = false) yearMonth: kotlin.String?
    ): ResponseEntity<List<SetChildConsumtionDTO>> {
        return ResponseEntity(service.getMonthConsumptions(yearMonth), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/billing/monthConsumptions"],
            produces = ["application/hal+json"],
            consumes = ["application/json"],
            method = [RequestMethod.POST])
    fun setMonthConsumptions(@Valid @RequestBody setChildConsumtionDTO: SetChildConsumtionDTO, @RequestParam(value = "yearMonth", required = false) yearMonth: kotlin.String?
    ): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.setMonthConsumptions(setChildConsumtionDTO, yearMonth), HttpStatus.valueOf(201))
    }
}
