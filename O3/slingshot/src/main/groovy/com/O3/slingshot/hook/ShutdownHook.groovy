package com.O3.slingshot.hook

import groovy.util.logging.Log4j

@Log4j
class ShutdownHook {

    static final def addShutdownHook(def thread) {
        Runtime.getRuntime().addShutdownHook(thread)
    }
}
