package com.example.jooq.config

import org.jooq.ExecuteContext
import org.jooq.ExecuteListener
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import org.springframework.jdbc.support.SQLExceptionTranslator
import org.springframework.stereotype.Component

@Component
internal class JOOQToSpringExceptionTransformer : ExecuteListener {

    override fun exception(ctx: ExecuteContext) {
        val dialect = ctx.configuration().dialect()
        val translator: SQLExceptionTranslator = SQLErrorCodeSQLExceptionTranslator(dialect.name)

        val sqlException = ctx.sqlException()
        if (sqlException !== null) {
            ctx.exception(translator.translate("jOOQ", ctx.sql(), sqlException))
        }
    }
}
