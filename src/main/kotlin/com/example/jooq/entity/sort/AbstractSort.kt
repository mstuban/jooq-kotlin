package com.example.jooq.entity.sort

import org.jooq.Field

interface AbstractSort {
    val name: String
    val jooqField: Field<out Any>
}
