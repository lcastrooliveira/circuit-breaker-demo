package client.controllers

import client.models.DemoData
import client.models.Response
import client.services.FallbackProviderService
import client.services.MainProviderService
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.vavr.control.Try
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.function.Supplier

@RestController
@RequestMapping("/provider-data")
class DemoDataProviderController(
    circuitBreakerRegistry: CircuitBreakerRegistry,
    private val mainProviderService: MainProviderService,
    private val fallbackProviderService: FallbackProviderService
) {

    private companion object {
        val logger = KotlinLogging.logger {}
    }

    private val circuitBreaker: CircuitBreaker = circuitBreakerRegistry.circuitBreaker("mainProviderCB")

    init {
        logger.info { "Client is Ready" }
        circuitBreaker.eventPublisher
            .onSuccess { logger.info { "Main Service is Ok. Returning from main provider" } }
            .onError { logger.warn { "Something is wrong. Registering Error" } }
            .onSlowCallRateExceeded { logger.warn { "Slow call rate exceeded " } }
            .onStateTransition { logger.info { "$it" } }
    }


    @GetMapping("unsafe")
    fun getUnsafeProviderData(): Response<DemoData> {
        logger.info { "Obtaining Demo data from main provider" }
        val demoData = mainProviderService.getDemoData()
        return Response(content = demoData)
    }


    @GetMapping
    fun getProviderData(): Response<DemoData> {
        val protectedSupplier: Supplier<DemoData> = CircuitBreaker.decorateSupplier(circuitBreaker) {
            logger.info { "Obtaining Demo data from main provider" }
            mainProviderService.getDemoData()
        }
        val demoData = Try.ofSupplier(protectedSupplier).recover {
            logger.warn { "Obtaining Demo data from secondary provider" }
            fallbackProviderService.getDemoData()
        }.get()
        return Response(content = demoData)
    }
}
