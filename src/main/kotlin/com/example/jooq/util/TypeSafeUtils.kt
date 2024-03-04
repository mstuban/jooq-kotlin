@file:JvmName("TypeSafeUtils")

package com.example.jooq.util

import com.example.jooq.entity.uid.PersonUid
import com.example.jooq.entity.uid.Uid
import com.example.jooq.entity.uid.UidPrefix

fun toTypedCommonUidSafely(uid: String?): Uid? {
    return if (uid == null)
        null
    else when (UidUtils.calculatePrefix(uid)) {
        UidPrefix.PERSON -> PersonUid(uid)
        else -> null
    }
}