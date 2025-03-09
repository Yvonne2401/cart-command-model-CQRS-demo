package com.example.demo.cqrs

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class CqrsDemoApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Container
	var axonserver: GenericContainer<*> = GenericContainer("axoniq/axonserver:latest-jdk-17")
		.withExposedPorts(8024, 8024)
		.withExposedPorts(8124, 8124)
		.withExposedPorts(8224, 8224)
		.waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort())

	@Test
	fun contextLoads() {

		val cartId = UUID.randomUUID()
		val productId = UUID.randomUUID()

		mockMvc.perform(post("/carts").contentType(MediaType.APPLICATION_JSON)
			.content("""{"cartId": "$cartId"}"""))
			.andExpect(status().isOk())
			.andExpect(content().string(""))

		mockMvc.perform(post("/carts/$cartId/add-item").contentType(MediaType.APPLICATION_JSON)
			.content("""{"productId": "$productId", "quantity": 1, "price": 10}"""))
			.andExpect(status().isOk())
			.andExpect(content().string(""))
		mockMvc.perform(post("/carts/$cartId/product/$productId/adjust-quantity" ).contentType(MediaType.APPLICATION_JSON)
			.content("""{"quantity": 6}"""))
			.andExpect(status().isOk())
			.andExpect(content().string(""))
		mockMvc.perform(post("/carts/$cartId/product/$productId/adjust-price" ).contentType(MediaType.APPLICATION_JSON)
			.content("""{"price": 4.99}"""))
			.andExpect(status().isOk())
			.andExpect(content().string(""))
		mockMvc.perform(post("/carts/$cartId/product/$productId/remove-item"))
			.andExpect(status().isOk())
			.andExpect(content().string(""))
	}
}

