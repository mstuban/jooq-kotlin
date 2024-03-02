package com.example.jooq.search

import com.example.jooq.Tables.PERSONS
import com.example.jooq.entity.Person
import com.example.jooq.tables.Persons
import jakarta.persistence.EntityManager
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import service.AbstractSearchService

@Component
@Suppress("SpreadOperator")
class PersonSearchService(
    entityManager: EntityManager,
    dslContext: DSLContext
) : AbstractSearchService<Person, PersonSort>(entityManager, dslContext) {

    override val targetClass = Person::class
    override val targetTable = PERSONS

    override fun sortEnumValues() = PersonSort.entries
}
