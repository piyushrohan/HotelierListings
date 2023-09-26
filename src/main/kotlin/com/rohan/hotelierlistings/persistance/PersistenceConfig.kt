package com.rohan.hotelierlistings.persistance

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PersistenceConfig(
	@Value("\${hl.persistence.host}")
	val host: String,

	@Value("\${hl.persistence.port}")
	val port: String,

	@Value("\${hl.persistence.user}")
	val user: String,

	@Value("\${hl.persistence.password}")
	val password: String,

	@Value("\${hl.persistence.db-index}")
	val database: String
)
