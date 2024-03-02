package com.example.jooq.util

import com.example.jooq.entity.Uid
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
