package com.O3.slingshot.model

import com.O3.slingshot.aggregator.report.Element
import com.O3.slingshot.spring.SpringContextManager
import groovy.transform.ToString
import groovy.util.logging.Log4j

@Log4j
@ToString
class Scenario {

    def dataReader = SpringContextManager.getBean('dataReader')
	
	def annotationParser  = SpringContextManager.getBean('annotationParser')

    def definition = new ArrayList<String>()

    def data

    def sheetName

    def steps = new ArrayList<Step>()
	
	def type = 'scenario' // Default to Scenario - it could change to "background" for background scenarios

    Element element = new Element()
	
	def String scenarioName = "";
	def List<String> groups = new ArrayList<String>();
	def boolean ignore = false; // if true, this scenario will not be executed

	def cloneMe () {
		def propsMap = this.properties
		propsMap.remove('metaClass')
		propsMap.remove('class')
		
		def cloned = new Scenario(propsMap)
		cloned.element = new Element();
		cloned.steps = new ArrayList<Step>();
		steps.each { step-> 
			def clonedStep = step.cloneMe();
			cloned.steps.add(clonedStep);
		}
		return cloned;
	}
	
    def parse(def line) {
		line = line?.trim()
		if(line==null||line.length() == 0) {
			// ignore if line is empty
			return
		}
        //log.info "Parsing the scenario ${line}"
        if (line?.trim()?.startsWith('Scenario:')) {
            definition << line
            //loadData(line)
			fetchSheetName (line);
        } else {
            //log.info "Processing steps"
            Step step = new Step()
            step.parse(line)
            steps << step
        }
    }
	
	def parseAnnotation (def annotationStr) {
		//log.info("annotationParser ${annotationParser}")
		if(annotationStr == null || annotationStr.length()==0) return;
		List<Annotation> annotations = annotationParser.parse(annotationStr);
		if(annotations!=null) {
			for(Annotation anno : annotations) {
				log.info("Update scenario with annotation: /${anno.annotationName}/")
				if(anno.annotationName.equalsIgnoreCase("Name")) {
					// Scenario Name
					if(anno.values!=null && anno.values.size()>0)
						this.scenarioName = anno.values.get(0);
				} else if(anno.annotationName.equalsIgnoreCase("Group")) {
					// Group 
					if(anno.values!=null && anno.values.size()>0)
						this.groups.addAll(anno.values)
				} else if(anno.annotationName.equalsIgnoreCase("Ignore")) {
					log.info("Ignore found..")
					// Ignore 
					this.ignore = true;
				}
			}
		}
		//log.info("Scenario Name: ${this.scenarioName}")
		//log.info("Scenario Groups: ${this.groups}")
	}

    def generateReport(def serialNumber) {
		element.name = serialNumber + ") ";
		if(scenarioName!=null && scenarioName.length()>0) element.name += "<b>${scenarioName}</b> - ";
		element.name += definition?.join('')?.replaceAll('Scenario:', '')
        if(sheetName!=null && sheetName.length()>0) element.name += "<div style='padding-left:30px;font-style:italic;'>Input Data: " + data[0] + " </div>"
		element.description = "Description for " + element.name 
        element.keyword = 'Scenario'
		element.type = type
		if(type == 'scenario') element.keyword = "Scenario"
		else if(type=='background') element.keyword = "Background"
        steps?.eachWithIndex { it, index ->
			log.info("Generating report for Step ${it.definition}")
            element.steps.add(it.generateReport(index+1))
        }
		groups?.each {
			element.tags.add(it);
		}
        element
    }

    /*def loadData(def line) {
        log.info "Processing line to extract data"
        def tokensInLine = line?.tokenize()
        def tokenCollection = SpringContextManager.getBean('keywords').intersect(tokensInLine)
        if (tokenCollection) {
            def token = tokenCollection?.get(0)
            log.info "Token is :: " + token
            def indexPositionOfToken = tokensInLine?.indexOf(token)
            if (indexPositionOfToken && tokensInLine && token == 'DATA') {
                if (indexPositionOfToken + 1 <= tokensInLine.size()) {
                    sheetName = tokensInLine?.get(indexPositionOfToken + 1)
                    log.info "SheetName is :: ${sheetName}"
                    data = dataReader.read(sheetName)
                }
            }
        }
        log.info "Data read is :: " + data
    }*/
	
	def private fetchSheetName (def line) {
		def tokensInLine = line?.tokenize()
		def tokenCollection = SpringContextManager.getBean('keywords').intersect(tokensInLine)
		if (tokenCollection) {
			def token = tokenCollection?.get(0)
			//log.info "Token is :: " + token
			def indexPositionOfToken = tokensInLine?.indexOf(token)
			if (indexPositionOfToken && tokensInLine && token == 'DATA') {
				if (indexPositionOfToken + 1 <= tokensInLine.size()) {
					sheetName = tokensInLine?.get(indexPositionOfToken + 1)
					log.info "SheetName is :: ${sheetName}"
					//data = dataReader.read(sheetName)
				}
			}
		}
	}

    def tokenize() {
        def commands = new ArrayList()
        steps?.eachWithIndex { step, index ->
            commands.addAll(step.tokenize())
        }
        commands
    }
}
