package com.example.jooq.entity

import org.jooq.Clause
import org.jooq.Field
import org.jooq.Operator
import org.jooq.Record
import org.jooq.Table
import org.jooq.VisitContext
import org.jooq.VisitListener
import org.jooq.impl.DSL.condition
import org.jooq.impl.DSL.inline
import org.jooq.impl.TableImpl
import java.util.ArrayDeque
import java.util.Deque

internal class ExcludeSoftDeletedEntitiesVisitor(
    private val targetTable: Table<out Record>
) : VisitListener {

    override fun clauseStart(context: VisitContext) {
        // Enter a new SELECT clause / nested select, or DML statement
        if (context.clause() == Clause.SELECT) {
            fieldStackPush(context)
        }
    }

    override fun clauseEnd(context: VisitContext) {
        if (context.clause() == Clause.SELECT_WHERE) {
            val conditions = fieldStackPeek(context).map { it.isNull.or(it.ne(inline("DELETED"))) }
            if (conditions.isNotEmpty()) {
                context.context()
                    .formatSeparator()
                    .keyword("and")
                    .sql(' ')
                    .visit(condition(Operator.AND, conditions))
            }
        }

        // Leave a SELECT clause / nested select, or DML statement
        if (context.clause() == Clause.SELECT) {
            fieldStackPop(context)
        }
    }

    override fun visitEnd(context: VisitContext) {
        val table = context.queryPart() as? TableImpl<*>
        val statusField = table?.field("status", String::class.java)
        if (table == targetTable && statusField != null && selectClauses(context).contains(Clause.SELECT_WHERE)) {
            fieldStackPeek(context).add(statusField)
        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        private fun fieldStack(context: VisitContext): Deque<MutableSet<Field<String>>> =
            (context.data() as MutableMap<String, Deque<MutableSet<Field<String>>>>)
                .computeIfAbsent("statuses") { ArrayDeque() }

        private fun fieldStackPush(context: VisitContext) = fieldStack(context).push(mutableSetOf())

        private fun fieldStackPop(context: VisitContext) = fieldStack(context).pop()

        private fun fieldStackPeek(context: VisitContext): MutableSet<Field<String>> = fieldStack(context).peek()

        /**
         * Retrieve all clauses for the current subselect level, starting with the last [Clause.SELECT].
         */
        private fun selectClauses(context: VisitContext): Array<out Clause> {
            val clauses = context.clauses()
            val index = clauses.lastIndexOf(Clause.SELECT)
            return if (index > 0) clauses.sliceArray(index until clauses.size) else clauses
        }
    }
}
