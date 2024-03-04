package com.example.jooq.entity.deserializers

import com.example.jooq.entity.uid.Uid
import com.example.jooq.util.toTypedCommonUidSafely
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonTokenId.ID_START_OBJECT
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.util.UUID

open class UidDeserializer : ContextualDeserializer, StdDeserializer<Uid>(Uid::class.java) {

    var rawClass: Class<*>? = null

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        rawClass = context.contextualType?.rawClass
        return this
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Uid? {
        return when (parser.currentTokenId()) {
            ID_START_OBJECT -> deserializeLegacyClassSerializedValue(parser)
            else -> deserializeValue(parser)
        }
    }

    open fun deserializeValue(parser: JsonParser) = toTypedCommonUidSafely(parser.readValueAs(String::class.java))

    private fun deserializeLegacyClassSerializedValue(parser: JsonParser): Uid? {
        val node: JsonNode = parser.codec.readTree(parser)
        val className = determineLegacyClassName(node)
        val uuidConstructor = Class.forName(className).kotlin.constructors.find { constructor ->
            constructor.parameters.size == 1 && constructor.parameters.any { parameter -> parameter.type.classifier == UUID::class }
        } ?: throw Exception("Constructor not found")
        return uuidConstructor.call(UUID.fromString(node.get("uuid").asText())) as Uid?
    }

    private fun determineLegacyClassName(node: JsonNode): String? {
        val classNameProperty = node.get("@class")?.asText()
        if (classNameProperty != null)
            return resolveClassName(classNameProperty)
        return rawClass?.name
    }

    private fun resolveClassName(classProperty: String): String =
        if (!classProperty.startsWith('c')) "co.utmost.oc.uid.$classProperty"
        else classProperty.replace("com.salteese", "co.utmost")
}
