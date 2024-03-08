@file:JvmName("UidUtils")

package com.example.jooq.util

import com.example.jooq.entity.uid.UidPrefix

object UidUtils {
    private val prefixStringToEnum = UidPrefix.entries.associateBy { it.prefix }

    @JvmStatic
    fun calculatePrefix(uid: String) = takeUntilUppercaseLetters(uid)?.let { prefixStringToEnum[it] }

    @JvmStatic
    private fun takeUntilUppercaseLetters(uid: String) = uid.takeWhile { it in 'A'..'Z' }.ifEmpty { null }
}
