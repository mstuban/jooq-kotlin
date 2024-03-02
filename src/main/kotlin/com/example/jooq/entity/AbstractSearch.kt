package com.example.jooq.entity

import org.jooq.Condition

interface AbstractSearch {
    fun toCondition(): Condition
}
