package com.example.jooq.util

import jakarta.persistence.EntityManager
import org.hibernate.query.NativeQuery
import org.hibernate.type.StandardBasicTypes
import org.jooq.Condition
import org.jooq.Field
import org.jooq.Query
import java.util.concurrent.atomic.AtomicLong

fun <T : Any> Condition.andFilter(value: T?, block: (T) -> Condition): Condition = if (value != null) {
    this.and(block(value))
} else {
    this
}

fun <T> Query.fetchWithWindowFunction(
    em: EntityManager,
    windowFunction: Field<Int>,
    entityClass: Class<T>
): Pair<List<T>, Long> {
    val counter = AtomicLong(0)
    val nativeQuery = em.createNativeQuery(sql.escapePostgresAssignmentColonsInSql())
        .unwrap(NativeQuery::class.java)
        .bindValues(bindValues)
        .addScalar(windowFunction.name, StandardBasicTypes.LONG)
        .addEntity(entityClass)
        .setTupleTransformer { tuple, _ ->
            counter.compareAndSet(0, tuple[0] as Long)
            tuple[1]
        }
    return (nativeQuery as NativeQuery<T>).resultList to counter.get()
}

// Hibernate parser bug
private fun String.escapePostgresAssignmentColonsInSql() = this.replace(Regex(":="), "\\\\\\:=")

private fun NativeQuery<*>.bindValues(values: List<Any>) = apply {
    values.forEachIndexed { index, value ->
        setParameter(index + 1, value)
    }
}
