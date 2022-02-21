package dev.lcastrooliveira.resilence4jdemo.client.services

import dev.lcastrooliveira.resilence4jdemo.client.models.DemoData
import dev.lcastrooliveira.resilence4jdemo.client.models.Response
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class MainProviderService(restTemplateBuilder: RestTemplateBuilder) {

    private companion object {
        const val ROOT_URL = "http://localhost:8081"
    }

    private val restTemplate: RestTemplate = restTemplateBuilder.build()

    fun getDemoData(): DemoData =
        restTemplate.exchange(
            "${ROOT_URL}/data-demo",
            HttpMethod.GET,
            null,
            object: ParameterizedTypeReference<Response<DemoData>>() {}
        ).body?.content ?: throw Exception("Not found")
}