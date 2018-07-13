package com.O3.slingshot.commands

import groovy.transform.ToString;
import groovy.util.logging.Log4j;

@ToString
class CommandResult {
	def result;
	def expectedOutcome;
	def actualOutcome;
	def message;
	def activePage;
	def dataUsed;
	
	public CommandResult(ResultType result) {
		this.result = result;
	}
	
	public boolean isSuccess() {
		return result == ResultType.SUCCESS;
	}
	public boolean isFailure() {
		return result == ResultType.FAILURE;
	}
}
