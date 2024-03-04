package com.example.demo

import com.example.jooq.entity.Person
import com.example.jooq.repository.PersonRepository
import com.example.jooq.search.PersonSearch
import com.example.jooq.service.PersonSearchService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(TestConfiguration::class)
class JooqKotlinApplicationTests {

	@Autowired
	lateinit var personRepository: PersonRepository

	@Autowired
	lateinit var personSearchService: PersonSearchService

	@BeforeEach
	fun beforeEach() {
		personRepository.deleteAll()
	}

	@Test
	fun `search by person search parameter - no expression`() {
		personRepository.save(
			Person().apply {
				name = "John Doe"
			}
		)
		with(personSearchService.findAllByQuery(PersonSearch())) {
			size shouldBe 1
			first().name.shouldBe("John Doe")
		}
	}

	@Test
	fun `search by person search parameter - expression`() {
		personRepository.save(
			Person().apply {
				name = "John Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Johnny Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Mark Doe"
			}
		)

		with(personSearchService.findAllByQuery(PersonSearch(expression = "John"))) {
			size shouldBe 2
			first().name.shouldBe("John Doe")
			this[1].name.shouldBe("Johnny Doe")
		}
	}

	@Test
	fun `search by person search parameter - expression - page limit & offset`() {
		personRepository.save(
			Person().apply {
				name = "John Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Johnny Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Johnny Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Mark Doe"
			}
		)

		with(personSearchService.findAllByQuery(PersonSearch(limit = 10, offset = 5, expression = "John"))) {
			size shouldBe 1
			first().name.shouldBe("Johnny Doe")
		}
	}
}