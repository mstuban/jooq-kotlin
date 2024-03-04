package com.example.jooq.search

import com.example.jooq.Tables.PERSONS
import com.example.jooq.util.andFilter
import org.jooq.impl.DSL
import java.io.Serializable

data class PersonSearch(
    var offset: Int = 0,
    var limit: Int = 20,
    var expression: String? = null,
) : Serializable, AbstractSearch {
    override fun toCondition() = DSL.noCondition()
        .andFilter(expression) {
            PERSONS.NAME.containsIgnoreCase(it)
        }
}
