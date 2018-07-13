package com.O3.slingshot.model

import com.O3.slingshot.spring.SpringContextManager
import groovy.transform.ToString
import groovy.util.logging.Log4j

@Log4j
@ToString
class Step {

    def dataReader = SpringContextManager.getBean('dataReader')

    def sheetName

    def definition = new ArrayList<String>()

    def commands = new ArrayList<Command>()

    def step = new com.O3.slingshot.aggregator.report.Step()

    def data

    def parse(def line) {
        //log.info "Parsing step definition ${line}"
        if (line) {
            definition << line?.trim()
            processCommands(line)
        }
    }

	def cloneMe () {
		def propsMap = this.properties
		propsMap.remove('metaClass')
		propsMap.remove('class')
		
		def cloned = new Step(propsMap)
		cloned.step = new com.O3.slingshot.aggregator.report.Step();
		cloned.data = null;
		return cloned;
	}

	
    def processCommands(def line) {
        //log.info "Processing commands"
        def tokensInLine = line?.tokenize()
        def tokenCollection = SpringContextManager.getBean('keywords').intersect(tokensInLine)
        //log.info "Token Collection is :: ${tokenCollection}"
        if (tokenCollection) {
            tokenCollection.each {
                def indexPositionOfToken = tokensInLine?.indexOf(it)
                if (indexPositionOfToken && tokensInLine) {
                    def token = it
                    //log.info "Processing the token ${token}"
                    if (token == 'DATA') {
                        //log.info "Processing the data ${token}"
                        if (indexPositionOfToken + 1 <= tokensInLine.size()) {
                            sheetName = tokensInLine?.get(indexPositionOfToken + 1)
                            //log.info "SheetName is :: ${sheetName}"
                            //data = dataReader.read(sheetName)
                            //log.info "Processing of data from sheet completed, data is ${data}"
                        }
                    } else {
                        //log.info "Processing the command ${token}"
                        Command command = new Command()
                        command.action = token
                        if (indexPositionOfToken + 1 <= tokensInLine.size()) {
                            command.parameter = tokensInLine?.get(indexPositionOfToken + 1)
                        }
                        commands.add(command)
                    }
                }
            }
        }
    }

    def generateReport(def serialNumber) {
        step.name = serialNumber + ". " + definition?.join('')
        step.keyword = ''
        step
    }

    def tokenize() {
        commands
    }
}
