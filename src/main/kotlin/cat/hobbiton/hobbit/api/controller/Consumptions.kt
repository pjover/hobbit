package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.SetYearMonthConsumptionsDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
import cat.hobbiton.hobbit.service.billing.ConsumptionsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.RequestBody
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
class ConsumptionsController(@Autowired(required = true) val service: ConsumptionsService) {


    @RequestMapping(
            value = ["/consumptions/{childCode}"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getChildConsumptions(@Min(1) @PathVariable("childCode") childCode: Int
    ): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.getChildConsumptions(childCode), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/consumptions"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getConsumptions(): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.getConsumptions(), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/consumptions/lastMonth"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getLastMonthConsumptions(): ResponseEntity<SetYearMonthConsumptionsDTO> {
        return ResponseEntity(service.getLastMonthConsumptions(), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/consumptions"],
            produces = ["application/json"],
            consumes = ["application/json"],
            method = [RequestMethod.POST])
    fun setConsumptions(@Valid @RequestBody setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO
    ): ResponseEntity<List<YearMonthConsumptionsDTO>> {
        return ResponseEntity(service.setConsumptions(setYearMonthConsumptionsDTO), HttpStatus.valueOf(201))
    }
}
