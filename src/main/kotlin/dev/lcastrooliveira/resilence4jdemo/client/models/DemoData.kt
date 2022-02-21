package dev.lcastrooliveira.resilence4jdemo.client.models

import java.time.Instant

data class Response<T>(val content: T, val errors: List<String> = emptyList(), val timestamp: Instant = Instant.now())
data class DemoData(val message: String, val version: Int)