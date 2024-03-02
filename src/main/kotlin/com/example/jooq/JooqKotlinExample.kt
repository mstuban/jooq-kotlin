//package com.example.jooq;
//
//import com.example.jooq.search.PersonSearch
//import com.example.jooq.search.PersonSearchService
//import jakarta.annotation.PostConstruct
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.core.env.Environment
//import org.springframework.stereotype.Component
//import service.AbstractSearchService
//
//@Component
//class JooqKotlinExample(
//    @Autowired
//    private val environment: Environment,
//    @Autowired
//    private val personSearchService: PersonSearchService
//) {
//    var logger: Logger = LoggerFactory.getLogger(AbstractSearchService::class.java)
//
//    @PostConstruct
//    fun init() {
//        logger.info(environment.activeProfiles.toString())
//        personSearchService.findAllByQuery(PersonSearch())
//    }
//}