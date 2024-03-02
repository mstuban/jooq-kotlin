@file:JvmName("UidUtils")

package com.example.jooq.util

import com.example.jooq.entity.UidPrefix
import java.util.*

object UidUtils {
    private val prefixStringToEnum = UidPrefix.values().associateBy { it.prefix }

    @JvmStatic
    fun calculatePrefix(uid: String) = takeUntilUppercaseLetters(uid)?.let { prefixStringToEnum[it] }

    @JvmStatic
    fun isValidUid(string: String): Boolean = calculatePrefix(string)?.let { prefix ->
        try {
            UUID.fromString(string.substring(prefix.toString().length))
            true
        } catch (ignored: IllegalArgumentException) {
            false
        }
    } ?: false

    @JvmStatic
    private fun takeUntilUppercaseLetters(uid: String) = uid.takeWhile { it >= 'A' && it <= 'Z' }.ifEmpty { null }
}
