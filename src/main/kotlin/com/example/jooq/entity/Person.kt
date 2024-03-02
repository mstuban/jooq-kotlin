package com.example.jooq.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "persons")
class Person : AbstractEntity() {
    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    lateinit var name: String
}
