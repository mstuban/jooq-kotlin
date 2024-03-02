package com.example.jooq.entity

import org.jooq.Field

interface AbstractSort {
    val name: String
    val jooqField: Field<out Any>
}
