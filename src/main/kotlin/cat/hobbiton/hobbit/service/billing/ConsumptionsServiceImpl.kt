package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.extension.getChild
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.util.error.AppException
import cat.hobbiton.hobbit.util.i18n.translate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeParseException

@Service
class ConsumptionsServiceImpl(
    private val consumptionRepository: ConsumptionRepository,
    private val customerRepository: CachedCustomerRepository,
    private val productRepository: CachedProductRepository,
    private val timeService: TimeService
) : ConsumptionsService {

    override fun getChildConsumptions(childCode: Int): List<YearMonthConsumptionsDTO> {
        return consumptionRepository.findByChildCodeAndInvoiceId(childCode)
            .groupBy { it.yearMonth }
            .map { (yearMonth, consumptions) ->
                YearMonthConsumptionsDTO(
                    yearMonth.toString(),
                    getGrossAmount(consumptions),
                    groupYearMonth(consumptions)
                )
            }
    }

    private fun getGrossAmount(consumptions: List<Consumption>): BigDecimal {
        return consumptions
            .map { getGrossAmount(it) }
            .sumOf { it }
    }

    private fun getGrossAmount(consumption: Consumption): BigDecimal {
        val product = productRepository.getProduct(consumption.productId)
        return product.price.multiply(consumption.units)
    }

    private fun groupYearMonth(consumptions: List<Consumption>): List<ChildConsumtionDTO> {
        return consumptions
            .groupBy { it.childCode }
            .map { (childCode, consumptions) ->
                groupConsumptions(childCode, consumptions)
            }
            .map {
                ChildConsumtionDTO(
                    code = it.first,
                    shortName = getChildrenShortName(it.first),
                    grossAmount = getGrossAmount(it.second),
                    consumptions = it.second.map { c ->
                        ConsumtionDTO(
                            c.productId,
                            c.units,
                            getGrossAmount(c),
                            c.note
                        )
                    }
                )
            }
    }

    private fun getChildrenShortName(childCode: Int): String {
        return customerRepository.getChild(childCode).shortName()
    }

    override fun getConsumptions(): List<YearMonthConsumptionsDTO> {
        return consumptionRepository.findByInvoiceId()
            .groupBy { it.yearMonth }
            .map { (yearMonth, consumptions) ->
                YearMonthConsumptionsDTO(
                    yearMonth.toString(),
                    getGrossAmount(consumptions),
                    groupYearMonth(consumptions)
                )
            }
    }

    override fun getLastMonthConsumptions(): SetYearMonthConsumptionsDTO {
        val consumptions = getConsumptions()
        if(consumptions.isEmpty()) return SetYearMonthConsumptionsDTO(
            yearMonth = timeService.lastMonth.toString(),
            children = emptyList()
        )
        return getSetYearMonthConsumptionsDTO(
            getConsumptions().last { isLastMonth(it.yearMonth) }
        )
    }

    private fun isLastMonth(yearMonth: String) = getYearMonth(yearMonth) == timeService.lastMonth

    private fun getSetYearMonthConsumptionsDTO(dto: YearMonthConsumptionsDTO) = SetYearMonthConsumptionsDTO(
        yearMonth = dto.yearMonth,
        children = dto.children.map { getSetChildConsumtionDTO(it) }
    )

    private fun getSetChildConsumtionDTO(dto: ChildConsumtionDTO) = SetChildConsumtionDTO(
        code = dto.code,
        consumptions = dto.consumptions.map { getSetConsumtionDTO(it) }
    )

    private fun getSetConsumtionDTO(dto: ConsumtionDTO) = SetConsumtionDTO(
            productId = dto.productId,
            units = dto.units,
            note = dto.note
    )

    override fun setConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO): List<YearMonthConsumptionsDTO> {
        checkChildren(setYearMonthConsumptionsDTO.children)
        saveConsumptions(setYearMonthConsumptionsDTO, false)
        return getConsumptions()
    }

    private fun checkChildren(children: List<SetChildConsumtionDTO>) {
        children.forEach {
            val customer = customerRepository.getCustomerByChildCode(it.code)
            if (!customer.active) throw AppException(ErrorMessages.ERROR_CUSTOMER_INACTIVE, customer.id)
            val child = customer.getChild(it.code)
            if (!child.active) throw AppException(ErrorMessages.ERROR_CHILD_INACTIVE, it.code)
        }
    }
    private fun saveConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO, isRectification: Boolean) {
        val yearMonth = getYearMonth(setYearMonthConsumptionsDTO.yearMonth)
        setYearMonthConsumptionsDTO.children.forEach { saveChildConsumtions(yearMonth, it, isRectification) }
    }

    private fun getYearMonth(yearMonth: String): YearMonth {
        return try {
            YearMonth.parse(yearMonth)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException(ValidationMessages.ERROR_YEAR_MONTH_INVALID.translate(), e)
        }
    }

    private fun saveChildConsumtions(yearMonth: YearMonth, childConsumtionDTO: SetChildConsumtionDTO, isRectification: Boolean) {
        childConsumtionDTO.consumptions.forEach { saveChildConsumtion(yearMonth, childConsumtionDTO.code, it, isRectification) }
    }

    private fun saveChildConsumtion(yearMonth: YearMonth, childCode: Int, consumtionDTO: SetConsumtionDTO, isRectification: Boolean) {
        consumptionRepository.save(Consumption(
            childCode = childCode,
            productId = consumtionDTO.productId,
            units = consumtionDTO.units,
            yearMonth = yearMonth,
            note = consumtionDTO.note,
            isRectification = isRectification
        ))
    }

    override fun setRectificationConsumptions(setYearMonthConsumptionsDTO: SetYearMonthConsumptionsDTO): List<YearMonthConsumptionsDTO> {
        saveConsumptions(setYearMonthConsumptionsDTO, true)
        return getConsumptions()
    }
}

