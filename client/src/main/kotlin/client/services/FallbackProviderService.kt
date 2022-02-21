package client.services

import client.models.DemoData
import client.models.Response
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class FallbackProviderService(restTemplateBuilder: RestTemplateBuilder) {
    private companion object {
        const val ROOT_URL = "http://localhost:8082"
    }

    private val restTemplate: RestTemplate = restTemplateBuilder.build()

    fun getDemoData(): DemoData =
        restTemplate.exchange(
            "$ROOT_URL/demo-data",
            HttpMethod.GET,
            null,
            object: ParameterizedTypeReference<Response<DemoData>>() {}
        ).body?.content ?: throw Exception("Not found")

}