package com.example.jooq.util

import com.example.jooq.entity.Uid

fun toTypedCommonUidSafely(uid: String?): Uid? {
    return if (uid == null)
        null
    else when (UidUtils.calculatePrefix(uid)) {
        else -> null
    }
}