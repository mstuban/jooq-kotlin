package com.example.jooq.service

import com.example.jooq.Tables.PERSONS
import com.example.jooq.entity.Person
import com.example.jooq.entity.sort.PersonSort
import com.example.jooq.tables.Persons
import jakarta.persistence.EntityManager
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
@Suppress("SpreadOperator")
class PersonSearchService(
    entityManager: EntityManager,
    dslContext: DSLContext
) : GeneralizedSearchService<Person, PersonSort>(entityManager, dslContext) {

    override val targetClass = Person::class
    override val targetTable: Persons = PERSONS

    override fun sortEnumValues() = PersonSort.entries
}
