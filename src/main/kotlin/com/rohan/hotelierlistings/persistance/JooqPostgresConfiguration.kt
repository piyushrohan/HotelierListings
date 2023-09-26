package com.rohan.hotelierlistings.persistance

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.RenderNameCase
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
@Import(PersistenceConfig::class)
class JooqPostgresConfiguration(@Autowired val persistenceConfig: PersistenceConfig) {

	@Bean
	fun dataSource(): DataSource {
		val dataSourceBuilder = DataSourceBuilder.create()
		dataSourceBuilder.driverClassName("org.postgresql.Driver")
		dataSourceBuilder.url("jdbc:postgresql://${persistenceConfig.host}:${persistenceConfig.port}/${persistenceConfig.database}")
		dataSourceBuilder.username(persistenceConfig.user)
		dataSourceBuilder.password(persistenceConfig.password)
		return dataSourceBuilder.build()
	}

	@Bean
	fun connectionProvider(): DataSourceConnectionProvider? {
		return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource()))
	}

	@Bean
	fun dsl(): DSLContext {
		return DSL.using(connectionProvider(), SQLDialect.POSTGRES).apply {
			settings().withRenderNameCase(RenderNameCase.LOWER)
		}
	}
}
