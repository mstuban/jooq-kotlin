package com.example.jooq.entity.sort.model

import co.utmost.gwg.api.model.sorting.SortTypeModel

interface SortModel {
    val type: SortTypeModel
    val reversed: Boolean
}
