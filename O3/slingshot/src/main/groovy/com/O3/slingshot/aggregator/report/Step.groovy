package com.O3.slingshot.aggregator.report

import groovy.json.StringEscapeUtils
import groovy.util.logging.Log4j

@Log4j
@Mixin(ExecutionOutcome)
class Step {

    def name

    def keyword
	
	def output = ""
    
	//Commenting out below line - "result" is already defined in ExecutionOutcome
	//def result = new Result()

    def json() {
        def stepBuilder = new StringBuilder()
        stepBuilder
                .append('{')
                .append(result.json())
                .append('"name":"').append(StringEscapeUtils.escapeJava(name)).append('",')
                .append('"keyword":"').append(keyword).append('",')
				.append('"output":["'+output+'"]')
        stepBuilder.append('}')
    }
}
