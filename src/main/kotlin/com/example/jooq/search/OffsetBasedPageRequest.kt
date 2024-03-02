package com.example.jooq.search

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable

class OffsetBasedPageRequest(
    private val offset: Int,
    private val limit: Int,
    private val sort: Sort = Sort.unsorted()
) : Pageable, Serializable {
    init {
        require(offset >= 0)
        require(limit > 0)
    }

    override fun getPageNumber(): Int = offset / limit

    override fun getPageSize(): Int = limit

    override fun getOffset(): Long = offset.toLong()

    override fun getSort(): Sort = sort

    override fun next(): Pageable = OffsetBasedPageRequest(offset + pageSize, pageSize, sort)

    override fun previousOrFirst(): Pageable = if (hasPrevious()) {
        OffsetBasedPageRequest(offset - pageSize, pageSize, sort)
    } else {
        first()
    }

    override fun first(): Pageable = OffsetBasedPageRequest(0, pageSize, sort)

    override fun hasPrevious(): Boolean = offset > limit

    override fun withPage(pageNumber: Int): Pageable = OffsetBasedPageRequest(pageSize * pageNumber, pageSize, getSort())
}
