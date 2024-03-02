package com.example.jooq.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.OffsetDateTime

@MappedSuperclass
abstract class AbstractEntity : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_generator")
    @SequenceGenerator(name = "person_generator", sequenceName = "persons_seq", allocationSize = 1)
    @Column(unique = true)
    var id: Long? = null

    @Column(name = "created", nullable = false, updatable = false)
    open var created: OffsetDateTime = OffsetDateTime.now()

    @Column(name = "modified", nullable = false)
    var modified: OffsetDateTime = OffsetDateTime.now()

    @PrePersist
    open fun prePersist() {
        val now = OffsetDateTime.now()
        created = now
        modified = now
    }

    @PreUpdate
    open fun preUpdate() {
        modified = OffsetDateTime.now()
    }
}
