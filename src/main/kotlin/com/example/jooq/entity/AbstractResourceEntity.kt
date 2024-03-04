package com.example.jooq.entity

import com.example.jooq.entity.uid.ResourceWithUid
import com.example.jooq.entity.uid.Uid
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractResourceEntity<T : Uid> : AbstractEntity(), ResourceWithUid<T> {
    private val uidString: String
        get() = uid.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AbstractResourceEntity<*>
        return true
    }

    override fun hashCode(): Int {
        return uidString.hashCode()
    }
}
