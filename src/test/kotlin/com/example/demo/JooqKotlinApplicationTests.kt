package com.example.demo

import com.example.jooq.entity.Person
import com.example.jooq.entity.sort.model.PersonSortModel
import com.example.jooq.entity.sort.model.PersonSortTypeModel
import com.example.jooq.repository.PersonRepository
import com.example.jooq.search.PersonSearch
import com.example.jooq.service.PersonSearchService
import com.example.jooq.util.toSort
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@PostgresSpringTest
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
				name = "Johnny1 Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Johnny2 Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Mark Doe"
			}
		)

		with(
			personSearchService.findAllByQuery(
				PersonSearch(expression = "John"),
				limit = 10,
				offset = 1
			)
		) {
			size shouldBe 2
			first().name.shouldBe("Johnny1 Doe")
			this[1].name.shouldBe("Johnny2 Doe")
		}
	}

	@Test
	fun `search by person search parameter - expression - sort`() {
		personRepository.save(
			Person().apply {
				name = "Mark Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Josip Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Ivan Doe"
			}
		)
		personRepository.save(
			Person().apply {
				name = "Luka Doe"
			}
		)

		with(
			personSearchService.findAllByQuery(
				PersonSearch(),
				listOf(PersonSortModel(PersonSortTypeModel.NAME, false)).toSort(),
				limit = 10,
				offset = 0
			)
		) {
			size shouldBe 4
			first().name.shouldBe("Ivan Doe")
			this[1].name.shouldBe("Josip Doe")
			this[2].name.shouldBe("Luka Doe")
			this[3].name.shouldBe("Mark Doe")
		}
	}
}
