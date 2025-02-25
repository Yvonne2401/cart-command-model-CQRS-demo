package com.example.cqrs_demo

import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespec
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableWirespec
class CqrsDemoApplication

fun main(args: Array<String>) {
	runApplication<CqrsDemoApplication>(*args)
}
