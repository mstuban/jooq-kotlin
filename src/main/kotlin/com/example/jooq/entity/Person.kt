package com.example.jooq.entity

import com.example.jooq.util.UidConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "persons")
class Person : AbstractResourceEntity<PersonUid>() {
    @Column(name = "uid", columnDefinition = "VARCHAR(500)", length = 16, unique = true, updatable = false, nullable = false)
    @Convert(converter = UidConverter::class)
    override var uid: PersonUid = PersonUid()

    @Column(name = "name", columnDefinition = "VARCHAR(500)", nullable = false)
    lateinit var name: String
}
