package com.example.demo.cqrs

import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespec
<<<<<<< HEAD:src/main/kotlin/com/example/demo/cqrs/CqrsDemoApplication.kt
import community.flock.wirespec.integration.spring.kotlin.configuration.EnableWirespecController
=======
>>>>>>> 3a2841c (WI):src/main/kotlin/com/example/cqrs_demo/CqrsDemoApplication.kt
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
<<<<<<< HEAD:src/main/kotlin/com/example/demo/cqrs/CqrsDemoApplication.kt
@EnableWirespecController
=======
@EnableWirespec
>>>>>>> 3a2841c (WI):src/main/kotlin/com/example/cqrs_demo/CqrsDemoApplication.kt
class CqrsDemoApplication

fun main(args: Array<String>) {
	runApplication<CqrsDemoApplication>(*args)
}
