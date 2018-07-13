package com.O3.slingshot.commands

import groovy.util.logging.Log4j

import com.O3.model.Outcome

@Log4j
class PerformCommand implements ICommand {

    def slingShotWeb

    def formValidator

    def o3Dsl

    public CommandResult execute(CommandData commandData) {
        /*def data = commandData.get('data')
        log.info "Data is :: ${data}"
        def actionToPerform = commandData.get('parameter')
        log.info "Action to perform is ${actionToPerform}"
		if(data == null) data = []
		def expectedResult = commandData.get("expectedResult");
        def outcome = o3Dsl."$actionToPerform".call(slingShotWeb?.browser, data)
		log.info("Outcome: " + outcome.getResultData() + " : Expected : " + expectedResult);
		if(outcome instanceof Outcome && outcome.isSuccess()) {
			//Verify if expected result matches Actual result		
			if(expectedResult?.sort(false) == outcome.getResultData()?.sort(false) ) {
				log.info("Expected & Actual Matches")
				def cmdResult = new CommandResult(ResultType.SUCCESS);
				return cmdResult;
			} else {
				log.info("Expected & Actual Doesnt match")
				def cmdResult = new CommandResult(ResultType.FAILURE);
				cmdResult.expectedOutcome = expectedResult;
				cmdResult.actualOutcome = outcome.getResultData();
				return cmdResult;
			}
		} else if(outcome instanceof Outcome && outcome.isFailure()) {
			def cmdResult = new CommandResult(ResultType.FAILURE);
			cmdResult.message = outcome.getFailureMessage();
			return cmdResult;
		}
		return new CommandResult(ResultType.SUCCESS);*/
		
		def data = commandData.get('data')
		log.info "Data is :: ${data}"
		def actionToPerform = commandData.get('parameter')
		log.info "Action to perform is ${actionToPerform}"
		if(data == null) data = []
		//def expectedResult = commandData.get("expectedResult");
		def outcome = o3Dsl."$actionToPerform".call(slingShotWeb?.browser, data)
		if(outcome.isSuccess()) {
			def cmdResult = new CommandResult(ResultType.SUCCESS);
			return cmdResult;
		}else if(outcome.isFailure()) {
			def cmdResult = new CommandResult(ResultType.FAILURE);
			cmdResult.message = outcome.getFailureMessage();
			return cmdResult;
		}
    }
}
