package com.example.jooq.search

import org.jooq.Field

enum class PersonSort(override val jooqField: Field<out Any>) : AbstractSort {
    MODIFIED(PERSONS.MODIFIED)
}
