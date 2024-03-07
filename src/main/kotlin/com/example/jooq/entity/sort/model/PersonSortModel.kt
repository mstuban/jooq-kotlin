package com.example.jooq.entity.sort.model

data class PersonSortModel @JvmOverloads constructor(
    override val type: PersonSortTypeModel,
    override var reversed: Boolean = false
) : SortModel
