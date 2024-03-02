package com.example.jooq.config

import org.jooq.conf.RenderImplicitJoinType
import org.jooq.conf.RenderQuotedNames
import org.jooq.conf.Settings
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
internal open class JooqCustomConfiguration(private val dataSource: DataSource) {

    @Bean
    open fun configurationCustomizer() = DefaultConfigurationCustomizer { config ->
        config
            .set(DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource)))
            .set(DefaultExecuteListenerProvider(JOOQToSpringExceptionTransformer()))
            .set(
                Settings()
                    .withRenderCatalog(false)
                    .withRenderSchema(false)
                    .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED)
                    .withRenderImplicitJoinType(RenderImplicitJoinType.LEFT_JOIN)
            )
    }
}
