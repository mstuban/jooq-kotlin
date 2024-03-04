package com.example.jooq.entity.uid

import com.example.jooq.entity.deserializers.UidDeserializer
import com.example.jooq.entity.serializers.UidSerializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

import java.io.Serializable
import java.util.*

@JsonSerialize(using = UidSerializer::class)
@JsonDeserialize(using = UidDeserializer::class)
abstract class Uid : Serializable, Comparable<Uid> {

    val uuid: UUID

    protected constructor(uid: String) {
        this.uuid = UUID.fromString(uid.substring(getPrefixString().length))
    }

    constructor() {
        uuid = UUID.randomUUID()
    }

    override fun toString(): String = getPrefix().toString() + uuid

    abstract fun getPrefix(): UidPrefix

    private fun getPrefixString() = getPrefix().toString()

    override fun compareTo(other: Uid): Int = toString().compareTo(other.toString())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Uid

        if (uuid != other.uuid) return false
        if (getPrefix() != other.getPrefix()) return false

        return true
    }

    override fun hashCode(): Int {
        return 31 * uuid.hashCode() + getPrefix().hashCode()
    }
}
