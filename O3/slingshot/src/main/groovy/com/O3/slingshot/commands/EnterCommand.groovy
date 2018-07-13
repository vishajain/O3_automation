package com.O3.slingshot.commands

import groovy.util.logging.Log4j

import com.O3.model.Outcome
import com.O3.web.WebPage

@Log4j
class EnterCommand implements ICommand{
	def o3Dsl
	def slingShotWeb
    def dataReader

    public CommandResult execute(CommandData commandData) {
		log.info("EnterCommand " + commandData);
		def data = commandData.get('data')
		log.info "Data to populate is :: ${data}"

		def activePage = commandData.get("activePage")
		log.info("Active Page is " + activePage);
		if(activePage!=null && activePage instanceof WebPage) {
			def outcome = activePage.populateData(slingShotWeb?.browser, commandData.get('parameter'), data)
			if(outcome instanceof Outcome && outcome.isFailure()) {
				def cmdResult = new CommandResult(ResultType.FAILURE);
				cmdResult.message = outcome.getFailureMessage();
				return cmdResult;
			}
		}
		return new CommandResult(ResultType.SUCCESS);
    }

}
