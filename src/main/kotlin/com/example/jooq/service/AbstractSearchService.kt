package com.example.jooq.service

import com.example.jooq.entity.*
import com.example.jooq.search.AbstractSearch
import com.example.jooq.entity.sort.AbstractSort
//import com.example.jooq.search.ExcludeSoftDeletedEntitiesVisitor
import com.example.jooq.search.OffsetBasedPageRequest
import com.example.jooq.util.fetchWithWindowFunction
import jakarta.persistence.EntityManager
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Table
import org.jooq.impl.DSL
import org.jooq.impl.DSL.asterisk
import org.jooq.impl.DSL.count
import org.jooq.impl.DSL.inline
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.rowNumber
import org.jooq.impl.DSL.select
import org.jooq.impl.DSL.selectOne
import org.jooq.impl.DSL.table
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.support.PageableExecutionUtils
import java.util.function.LongSupplier
import kotlin.reflect.KClass

abstract class AbstractSearchService<ENTITY : AbstractResourceEntity<*>, SORT>(
    private val entityManager: EntityManager,
    dslContext: DSLContext
) where SORT : AbstractSort, SORT : Enum<*> {

    protected abstract val targetClass: KClass<ENTITY>
    protected abstract val targetTable: Table<out Record>

    private val softDeletionAwareDslContext by lazy {
        DSL.using(
            dslContext
                .configuration()
              //  .derive(ExcludeSoftDeletedEntitiesVisitor(targetTable))
        )
    }

    fun existsByQuery(search: AbstractSearch): Boolean {
        val condition = search.toCondition()
        return softDeletionAwareDslContext
            .select(inline(true))
            .whereExists(
                selectOne().from(targetTable).where(condition)
            )
            .query
            .singleOrNull()?.value1() ?: false
    }

    fun findAllByQuery(search: AbstractSearch, sort: Sort = Sort.unsorted()): List<ENTITY> =
        findByQuery(search, OffsetBasedPageRequest(0, Int.MAX_VALUE, sort)).content

    private fun findByQuery(
        search: AbstractSearch,
        pageable: OffsetBasedPageRequest
    ): Page<ENTITY> {
        val condition = search.toCondition()

        val orderBy = pageable.toOrderBy()
        val rowNumberField = rowNumber().over()
            // assuming "id" is the Primary Key of all entities
            .partitionBy(targetTable.field("id"))
            .orderBy(orderBy)
            .`as`("__row_number")
        val totalCountField = count(asterisk()).over().`as`("__total_count")

        val cteAlias = name("__cte")
        val (records, counterFromWindowFunction) = softDeletionAwareDslContext.with(cteAlias).`as`(
            select(targetTable.asterisk(), rowNumberField)
                .from(targetTable)
                .where(condition)
                .orderBy(orderBy)
        )
            .select(totalCountField, asterisk())
            .from(table(cteAlias))
            .where(rowNumberField.eq(inline(1)))
            .offset(pageable.offset)
            .limit(pageable.pageSize)
            .query
            .fetchWithWindowFunction(entityManager, totalCountField, targetClass.java)

        val totalSupplier = LongSupplier {
            // using window function to compute totalCount only works if offset is NOT beyond the last record
            if (records.isNotEmpty() || pageable.offset == 0L) {
                counterFromWindowFunction
            } else {
                softDeletionAwareDslContext
                    .selectCount()
                    .from(targetTable)
                    .where(condition)
                    .fetchSingle().value1().toLong()
            }
        }

        return PageableExecutionUtils.getPage(records, pageable, totalSupplier)
    }

    abstract fun sortEnumValues(): List<SORT>

    private fun OffsetBasedPageRequest.toOrderBy() =
        sort.map { it.toJooqFieldOrder() }.toList()

    private fun Sort.Order.toJooqFieldOrder() = sortEnumValues().first { it.name == this.property }.let {
        if (this.isAscending) {
            it.jooqField.asc()
        } else {
            it.jooqField.desc().nullsLast()
        }
    }
}
