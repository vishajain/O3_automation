package com.O3.slingshot.parser

import groovy.util.logging.Log4j

@Log4j
class BackgroundParser {

    def folder

    def extension

    def parse(def backgroundFile) {
        log.info "Parsing the background file ${backgroundFile} from background folder"
        def backgroundText = new File(folder + backgroundFile + extension)?.text
        log.info "Contents is :: "
        log.info backgroundText
        backgroundText
    }
}
