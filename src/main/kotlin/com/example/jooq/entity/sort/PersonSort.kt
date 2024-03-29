package com.example.jooq.entity.sort

import com.example.jooq.Tables.PERSONS
import org.jooq.Field

enum class PersonSort(override val jooqField: Field<out Any>) : AbstractSort {
    CREATED(PERSONS.CREATED),
    MODIFIED(PERSONS.MODIFIED),
    NAME(PERSONS.NAME)
}
