package com.example.demo.cqrs

import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespec
import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespecController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableWirespecController
class CqrsDemoApplication

fun main(args: Array<String>) {
	runApplication<CqrsDemoApplication>(*args)
}
