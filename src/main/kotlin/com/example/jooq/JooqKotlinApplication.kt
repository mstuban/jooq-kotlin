package com.example.jooq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JooqKotlinApplication

fun main(args: Array<String>) {
	runApplication<JooqKotlinApplication>(*args)
}
