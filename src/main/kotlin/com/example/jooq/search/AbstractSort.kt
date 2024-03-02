package com.example.jooq.search

import org.jooq.Field

interface AbstractSort {
    val name: String
    val jooqField: Field<out Any>
}
