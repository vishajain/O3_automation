package com.O3.slingshot.spring

import groovy.util.logging.Log4j
import org.springframework.context.support.ClassPathXmlApplicationContext

@Log4j
class SpringContextManager {

    private static def APPLICATION_CONTEXT

    def static final init() {
        log.info "Initializing spring context started"
        APPLICATION_CONTEXT = new ClassPathXmlApplicationContext('classpath:beans.xml')
        log.info "Spring context initialization complete"
    }

    def static final getBean(def bean) {
        APPLICATION_CONTEXT.getBean(bean)
    }

}
