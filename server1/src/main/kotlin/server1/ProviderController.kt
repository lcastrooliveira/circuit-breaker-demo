package server1

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Response<T>(val content: T, val errors: List<String> = emptyList())
data class DemoData(val message: String, val version: Int)

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/data-demo")
class ProviderController {

    @GetMapping
    fun getDataDemo(): Response<DemoData> {
        val content = DemoData(message = "Information provided by SERVICE 1", 1)
        logger.info { "Sending Client with data from SERVICE 1" }
        return Response(content = content)
    }
}
