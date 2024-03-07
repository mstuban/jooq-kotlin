package com.example.jooq.util

import com.example.jooq.entity.sort.model.SortModel
import org.springframework.data.domain.Sort

fun <T : SortModel> List<T>?.toSort(defaultSort: Sort = Sort.unsorted()): Sort =
    if (isNullOrEmpty()) {
        defaultSort
    } else {
        map {
            if (it.reversed) {
                Sort.by(it.type.name).descending()
            } else {
                Sort.by(it.type.name)
            }
        }.reduce { s: Sort, t: Sort -> s.and(t) }
    }
