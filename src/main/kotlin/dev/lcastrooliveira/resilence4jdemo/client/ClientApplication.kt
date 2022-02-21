package dev.lcastrooliveira.resilence4jdemo.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClientApplication

fun main(args: Array<String>) {
	runApplication<ClientApplication>(*args)
}
