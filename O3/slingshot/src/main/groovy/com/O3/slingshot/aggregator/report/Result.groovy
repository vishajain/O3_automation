package com.O3.slingshot.aggregator.report

import groovy.util.logging.Log4j

@Log4j
class Result {

    def duration = 0

    def status = 'pending'

    def static PASS = 'passed'
    def static SKIP = 'skipped'
    def static FAIL = 'failed'
    def static PENDING = 'pending'
	
	def message;
	def expectedResult;
	def actualResult;
	def dataUsed;

    def json() {
        def resultJson = new StringBuilder()
		def errMsg = ""
		if(status.equals(FAIL)) {	
			if(message!=null) errMsg += message + "<br/>";		
			if(expectedResult!=null) errMsg += "Expected: " + expectedResult + "<br/>";
			if(actualResult!=null) errMsg += "Actual: " + actualResult;
			if(dataUsed!=null) errMsg += "<br/>Data Used: " + dataUsed;
		}
        resultJson
                .append('"result":{')
                .append('"duration":').append(duration).append(',')
                .append('"status":"').append(status).append('",')
				.append('"error_message":"').append(errMsg).append('"')
                .append("},")
    }
}
