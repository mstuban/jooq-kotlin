package com.example.jooq.entity

import com.example.jooq.util.UidDeserializer
import com.example.jooq.util.UidSerializer
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

import java.io.Serializable
import java.util.*

@JsonSerialize(using = UidSerializer::class)
@JsonDeserialize(using = UidDeserializer::class)
abstract class Uid : Serializable, Comparable<Uid> {

    val uuid: UUID get() = field

    protected constructor(uid: String, validate: Boolean = true) {
        this.uuid = UUID.fromString(uid.substring(getPrefixString().length))
    }


    protected constructor(uuid: UUID?) {
        this.uuid = uuid ?: throw Exception("Invalid uuid: null")
    }


    constructor() {
        uuid = UUID.randomUUID()
    }

    fun uuid(): UUID = uuid

    override fun toString(): String = getPrefix().toString() + uuid

    @JsonIgnore
    abstract fun getPrefix(): UidPrefix

    @JsonIgnore
    fun getPrefixString() = getPrefix().toString()

    override fun compareTo(other: Uid): Int = toString().compareTo(other.toString())

    fun asType(type: Class<Any>): Any {
        return when (type) {
            String::class.java -> toString()
            else -> type.cast(this)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Uid

        if (uuid != other.uuid) return false
        if (getPrefix() != other.getPrefix()) return false

        return true
    }

    @JsonIgnore
    @Transient
    private var _hashCode: Int? = null

    override fun hashCode(): Int {
        if (_hashCode == null)
            _hashCode = 31 * uuid.hashCode() + getPrefix().hashCode()
        return _hashCode ?: error("Unexpected null cached hashCode")
    }

    companion object {
        const val serialVersionUID = 1L
    }
}
