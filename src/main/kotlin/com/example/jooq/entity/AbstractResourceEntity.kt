package com.example.jooq.entity

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractResourceEntity<T : Long> : AbstractEntity() {
    private val idString: String
        get() = id.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AbstractResourceEntity<*>
        return true
    }

    override fun hashCode(): Int {
        return idString.let { idString.hashCode() }
    }
}
