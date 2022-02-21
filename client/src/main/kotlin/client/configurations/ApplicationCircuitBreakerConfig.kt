package client.configurations

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration


@Configuration
class ApplicationCircuitBreakerConfig {

    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry {
        val cbConfig = CircuitBreakerConfig.custom()
             // Tipo de janela deslizante: Tempo ou Contagem absoluta
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            // Qual o tamanho desta janela?
            .slidingWindowSize(20)
            // Quantas vezes deve chamar no mínimo para começar a calcular as taxas de erro/lentidão?
            .minimumNumberOfCalls(10)
            // Quantos % de falhas tolerar (relativo a janela)?
            .failureRateThreshold(10.0f)
            // Quantos % de "chamadas lentas" tolerar?
            .slowCallRateThreshold(10.0f)
            // O que é considerado uma "chamada lenta"?
            .slowCallDurationThreshold(Duration.ofSeconds(1))
            // Se abrir o circuito quanto tempo ficar com ele aberto até poder tentar recuperar?
            .waitDurationInOpenState(Duration.ofSeconds(30))
            // Quando estiver no estado "MEIO_ABERTO" quantas vezes tentar bater no serviço primário?
            .permittedNumberOfCallsInHalfOpenState(5)
            .build()
        return CircuitBreakerRegistry.of(cbConfig)
    }
}
