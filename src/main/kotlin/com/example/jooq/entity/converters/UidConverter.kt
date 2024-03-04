package com.example.jooq.entity.converters

import com.example.jooq.entity.uid.Uid
import com.example.jooq.util.toTypedCommonUidSafely
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class UidConverter : AttributeConverter<Uid, String> {
    override fun convertToDatabaseColumn(uid: Uid?): String? {
        return uid?.toString()
    }

    override fun convertToEntityAttribute(uidAsString: String?): Uid? {
        return if (uidAsString != null) toTypedCommonUidSafely(uidAsString) else null
    }
}
