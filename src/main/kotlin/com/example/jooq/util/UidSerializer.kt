package com.example.jooq.util

import com.example.jooq.entity.Uid
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class UidSerializer : StdSerializer<Uid>(Uid::class.java) {
    override fun serialize(value: Uid?, gen: JsonGenerator, provider: SerializerProvider) {
        value?.let { gen.writeObject(value.toString()) }
    }
}
