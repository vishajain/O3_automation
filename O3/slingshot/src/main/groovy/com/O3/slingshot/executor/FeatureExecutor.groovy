package com.O3.slingshot.executor

import groovy.util.logging.Log4j

import com.O3.app.pages.user.LoginPage
import com.O3.slingshot.commands.CommandData
import com.O3.slingshot.commands.CommandResult
import com.O3.web.WebPage

@Log4j
class FeatureExecutor {
	def slingShotWeb
    def commandExecutor
	
	def close(){
		slingShotWeb.close();
	}

    def execute(def feature) {
		slingShotWeb.initialize();
		
        log.info "Executing the feature started"
        log.info "Total number of scenarios to execute is :: ${feature?.scenarios?.size}"
		def boolean firstScenario = true;
        feature?.scenarios?.each { scenario ->
            log.info "Started executing the scenario :: ${scenario?.definition}"
            log.info "Total size of data :: ${scenario?.data?.length}"
			log.info "Total steps in scenario :: ${scenario?.steps?.size()}"
			
			// TODO: Should system be brought back to initial state before beginning new Scenario testing
			if(!firstScenario) slingShotWeb.reset();
			
			// Execute Each Background before executing Scenario
			log.info("Backgrounds to execute: ${feature.bgScenarios?.size()}")
			if(feature.bgScenarios?.size()>0) {
				feature.bgScenarios?.each { bgScenario ->
					log.info("Executing Background: ${bgScenario?.definition}")
					if (bgScenario?.data) {
						executeScenarioWithData(bgScenario)
					} else {
						executeStepWithData(bgScenario)
					}
				}
			}
			log.info("Backgrounds completed.. executing the scenario now ${scenario?.definition}")
            if (scenario?.data) {
                // execute scenario where data is provided, in case step has data provided too
                // then step data will be overriding the scenario data
                executeScenarioWithData(scenario)
            } else {
                // execute step with data provided
                executeStepWithData(scenario)
            }
			firstScenario = false;
        }
        log.info "Executing the feature completed"		
    }

    def executeScenarioWithData(def scenario) {
        log.info "Executing scenario with data provided"
        def scenarioFailed = false;
        scenario?.data?.eachWithIndex { data, rowIndex -> // TODO : Add concept to deal with row failures ...
			if(scenarioFailed) return; // Scenario already failed for one set of data
			log.info "Executing scenario for data :: ${data}"
            def commandData = new CommandData()
            commandData.put("data", data)
            commandData.put("usecase", scenario?.sheetName)
            log.info "Commands to execute is ${scenario.tokenize()}"
            def stepFailed = false;
			def activePageRef = null; // Reference to current page object (Think thru this a bit more)

            scenario?.steps?.eachWithIndex { step, index ->
                if (stepFailed) { // if already stepFailed, skip remaining steps...
                    step?.step?.skip()
                    return
                }

                def commands = new ArrayList()
                commands.addAll(step.tokenize())
                log.info "Commands to execute is :: ${commands}"
                long startTime = System.currentTimeMillis()
                def status = 'passed'
				
                try {
                    // in case step has data, override it
                    if (step.data) {
						log.info("Step data available - overridding with : " + step.data)
                        data = step.data; 
                    }
                    commandData.put("data", data[0])
					if(activePageRef!=null) commandData.put("activePage", activePageRef);
					
                    def expectedErrors = (data.size() > 1) ? data [1] : []
					if(expectedErrors.size()>0) {
						/* Expected result is actually a comma-separated string, not a list - so, convert it to array list */
						def expectedStr = expectedErrors.get(0);
						expectedErrors = expectedStr.tokenize(",");
						commandData.put("expectedResult", expectedErrors);
					} else {
						commandData.put("expectedResult", []);
					}
					
					commands?.each { command ->
                        // if executing a command fails, continue further
                        def result = commandExecutor?.execute(command, commandData)

                         // now validate this with the expectation that is set in the sucess and the failure case.
                        log.info('==============================')
                        log.info('commandData is ' + commandData.toString())
                        log.info('expectation is ' + expectedErrors)
                        log.info('result is ' + result)
                        log.info('==============================')

                        // now match the result to the expectedErrors.
                        // if not null, coerce empty tp false and match against result. Whoa - nice !
                        //def outcome = expectedErrors?.sort(false) == result?.sort(false)
						if(result instanceof CommandResult) {
							if(result.isSuccess()) {
								step?.step?.pass()
								if(result.activePage!=null) activePageRef = result.activePage;
							} else {
								log.info("Step Failed with data ${data[0]}")
								step?.step?.fail(result.message, result.expectedOutcome, result.actualOutcome, data[0])
								stepFailed = true
								scenarioFailed = true
							}
						}
                    }
                } catch (Exception e) {
                    log.error(e, e)
                    step?.step?.fail(e.toString())
                    // should have Result stacktrace.
                }

                //step?.step?.result?.status = Result.PASS // pass, if everything passed.
                if(!stepFailed) step?.step?.pass()
                long endTime = System.currentTimeMillis()
                log.info "Time taken for execution is :: ${endTime - startTime}"
                step?.step?.result?.duration = (endTime - startTime) * 1000 * 1000
				log.info("Final result is " + step?.step?.result?.status)
				
                // have to take care of some pass, some fail in the xls case.
            }
			
            // the scenario fails, if the step of any 1 of the data element stepFailed
            //scenario.
        }

        log.info "Executing scenario with data provided completed"
		
    }

    def executeStepWithData(def scenario) {
        log.info "Executing scenario with Step-data provided"
        log.info "Commands to execute is ${scenario.tokenize()}"
		def scenarioFailed = false;
		def activePageRef = null; // Reference to current page object (Think thru this a bit more)
		def stepFailed = false;
		def data = []
        scenario?.steps?.eachWithIndex { step, index ->
			if (stepFailed) { // if already stepFailed, skip remaining steps...
				step?.step?.skip()
				return
			}
			//def data = []
			if(step.data!=null) data = step.data[0]; // Get only the first entry. TODO: Think of better approach to execute all data in data-set
			log.info("Step Data is: " + step.data)
            def commandData = new CommandData()
            commandData.put("data", data[0])
            commandData.put("usecase", step.sheetName)
			if(activePageRef!=null) commandData.put("activePage", activePageRef);
            def commands = new ArrayList()
            commands.addAll(step.tokenize())
            log.info "Commands to execute is :: ${commands}"
            long startTime = System.currentTimeMillis()
            //def status = 'passed'
			def expectedErrors = (data.size() > 1) ? data [1] : []
			log.info("expectedErrors " + expectedErrors);
			if(expectedErrors.size()>0) {
				/* Expected result is actually a comma-separated string, not a list - so, convert it to array list */
				def expectedStr = expectedErrors.get(0);
				expectedErrors = expectedStr.tokenize(",");
				commandData.put("expectedResult", expectedErrors);
			} else {
				commandData.put("expectedResult", []);
			}
			
            try {
                commands?.each { command ->
                    // if executing a command fails, continue further
                    //commandExecutor?.execute(command, commandData)
					log.info("Executing Command.. " + command + " with data " + commandData)
					def result = commandExecutor?.execute(command, commandData)
					if(result instanceof CommandResult) {
						if(result.isSuccess()) {
							step?.step?.pass()
							if(result.activePage!=null) activePageRef = result.activePage;
						} else {
							step?.step?.fail(result.message, result.expectedOutcome, result.actualOutcome, data[0])
							stepFailed = true
							scenarioFailed = true
						}
					}
                }
            } catch (Exception e) {
                log.error(e, e)
            }
			if(!stepFailed) step?.step?.pass()
			long endTime = System.currentTimeMillis()
			log.info "Time taken for execution is :: ${endTime - startTime}"
			step?.step?.result?.duration = (endTime - startTime) * 1000 * 1000
			log.info("Final result is " + step?.step?.result?.status)
        }
        log.info "Executing scenario with data provided completed"
    }
}
