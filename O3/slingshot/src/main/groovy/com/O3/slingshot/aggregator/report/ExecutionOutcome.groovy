package com.O3.slingshot.aggregator.report

/**
 * Created by nsharma on 7/10/2014.
 */
class ExecutionOutcome {
    def result = new Result();

    def skip() {
        result.status = Result.SKIP
    }

    def pass() {
        result.status = Result.PASS
    }
    def fail() {
        result.status = Result.FAIL
    }
	def fail(def msg) {
		result.status = Result.FAIL
		result.message = msg
	}
	def fail(def expectedResult, def actualResult) {
		result.status = Result.FAIL
		result.expectedResult = expectedResult
		result.actualResult = actualResult
	}
	def fail(def msg, def expectedResult, def actualResult) {
		result.status = Result.FAIL
		result.message = msg
		result.expectedResult = expectedResult
		result.actualResult = actualResult
	}
	def fail(def msg, def expectedResult, def actualResult, def dataUsed) {
		result.status = Result.FAIL
		result.message = msg
		result.expectedResult = expectedResult
		result.actualResult = actualResult
		result.dataUsed = dataUsed
	}
}