package com.example.jooq.entity

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractResourceEntity<T : Uid> : AbstractEntity(), ResourceWithUid<T> {
    val uidString: String
        get() = uid.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AbstractResourceEntity<*>
        if (uid != other.uid) return false
        return true
    }

    override fun hashCode(): Int {
        return uid.let { uid.hashCode() }
    }
}
