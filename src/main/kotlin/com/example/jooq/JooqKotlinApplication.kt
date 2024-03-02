package com.example.jooq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan( basePackages = ["com.example.jooq"] )
class JooqKotlinApplication

fun main(args: Array<String>) {
	runApplication<JooqKotlinApplication>(*args)
}
