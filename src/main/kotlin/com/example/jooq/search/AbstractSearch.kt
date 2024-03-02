package com.example.jooq.search

import org.jooq.Condition

interface AbstractSearch {
    fun toCondition(): Condition
}
