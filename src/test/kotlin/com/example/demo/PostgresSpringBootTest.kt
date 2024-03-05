package com.example.demo

import org.junit.jupiter.api.parallel.ResourceLock
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * Spring Boot Test that runs against a real dockerized postgres db, managed by testcontainers
 */
@SpringBootTest(
    properties = [
        "spring.datasource.url=jdbc:tc:postgresql:12.0:///?TC_TMPFS=/testtmpfs:rw",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
        "spring.datasource.type=com.zaxxer.hikari.HikariDataSource",
        "spring.datasource.hikari.connectionTimeout=120000"
    ]
)
@Testcontainers
@ResourceLock(value = "TESTCONTAINERS")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(profiles = ["postgres", "test"])
annotation class PostgresSpringBootTest
