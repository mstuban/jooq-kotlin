package com.example.jooq.repository

import com.example.jooq.entity.Person
import com.example.jooq.entity.PersonUid
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository: JpaRepository<Person, PersonUid>
