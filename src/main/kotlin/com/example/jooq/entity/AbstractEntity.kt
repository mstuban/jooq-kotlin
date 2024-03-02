package com.example.jooq.entity

import java.io.Serializable
import java.time.OffsetDateTime
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate

@MappedSuperclass
abstract class AbstractEntity : Serializable {

    @Id
    @Column(unique = true, updatable = false)
    @GeneratedValue(strategy = IDENTITY)
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
