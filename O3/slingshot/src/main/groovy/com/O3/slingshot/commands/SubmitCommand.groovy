package com.O3.slingshot.commands

import groovy.util.logging.Log4j

import com.O3.model.Outcome
import com.O3.web.WebPage

@Log4j
class SubmitCommand implements ICommand{
	def o3Dsl
	def slingShotWeb

    public CommandResult execute(CommandData commandData) {
		log.info("SubmitCommand " + commandData);
		def data = commandData.get('data')
		
		if(data == null) data = []
		def expectedResult = commandData.get("expectedResult");
		log.info "Data to populate is :: ${data}"
		
		def activePage = commandData.get("activePage")
		log.info("Active Page is " + activePage);
		if(activePage!=null && activePage instanceof WebPage) {
			def outcome = activePage.submit(slingShotWeb?.browser, commandData.get('parameter'), data)
			if(outcome instanceof Outcome && outcome.isSuccess()) {
				log.info("Outcome: " + outcome.getResultData() + " : Expected : " + expectedResult);
				//Verify if expected result matches Actual result
				if(expectedResult?.sort(false) == outcome.getResultData()?.sort(false) ) {
					log.info("Expected & Actual Matches")
					def cmdResult = new CommandResult(ResultType.SUCCESS);
					return cmdResult;
				} else {
					log.info("Expected & Actual Doesnt match")
					def pageName = activePage.toString()?.split('\\.').last().split('@').first()
					def fileName = pageName+"ValidationMsgMismatch"  //take screen shot for validation message mismatch
					slingShotWeb.takeScreenShot(fileName)
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
		}
		return new CommandResult(ResultType.SUCCESS);
    } // End of execute(..) method

}
