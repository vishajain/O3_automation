package com.O3.slingshot.model

import groovy.transform.ToString
import groovy.util.logging.Log4j

import java.util.ArrayList

import com.O3.slingshot.aggregator.report.Report
import com.O3.slingshot.spring.SpringContextManager

@Log4j
@ToString
class Feature {
	def dataReader = SpringContextManager.getBean('dataReader')
    transient boolean featureIndicator

    transient boolean scenarioIndicator
	
	transient boolean backgroundIndicator
	transient boolean annotationIndicator

    def definition = new ArrayList<String>()

    def scenarios = new ArrayList<Scenario>()

    def bgScenarios = new ArrayList<Scenario>()

    Report report = new Report()

    Scenario scenario
	
	def bgParsingLevel = 0;
	def final MAX_BG_LEVEL_PARSING = 1; // Maximum number of levels to parse Backgrounds
	
	def String annotationStr = "";
	
    def parse(def line) {
        //log.info "Parsing line ${line}"
		annotationIndicator = false; //reset
        // if line starts with Feature:, until you receive Scenario: it is all feature info
        if (line?.trim()?.startsWith('Feature:')) {
            log.info "Received a feature"
            featureIndicator = true
            report.name = line?.trim().replaceAll('Feature:', '')
        } else if (line?.trim()?.startsWith('Background:')) {
			def backgrounds = line?.trim().replaceAll('Background:', '')
			backgrounds = backgrounds?.trim();
			processBackgrounds(backgrounds);
			featureIndicator = false
		} else if (line?.trim()?.startsWith('@')) {
			// Annotation for a Scenario - Process it and store it until we come across Scenario: to associate with
			log.info("Annotations found ${line}")
			featureIndicator = false;
			annotationIndicator = true;
			annotationStr = annotationStr + " " + line?.trim();
		} else if (line?.trim()?.startsWith('Scenario:')) {
            log.info "Received a scenario"
            featureIndicator = false
            scenarioIndicator = true
        }

        if (featureIndicator) {
            definition << line
        }

        if (scenarioIndicator) {
            scenario = new Scenario()
			// Parse annotation string and add it to scenario
			log.info("Annotation String for scenario is ${annotationStr}");
			scenario.parseAnnotation(annotationStr);
			annotationStr = "";// Reset annotation String
            scenarios << scenario
            scenarioIndicator = false
			log.info("Scenario Name is: ${scenario.scenarioName}")
        }
		if(!annotationIndicator) scenario?.parse(line)
    }
	
	private processBackgrounds (backgrounds) {
		log.info("Backgrounds found, Parsing Level is " + bgParsingLevel);
		if(bgParsingLevel < MAX_BG_LEVEL_PARSING) {
			bgParsingLevel ++;
			def bgParser = SpringContextManager.getBean('backgroundParser')
			bgParser.bgParsingLevel = bgParsingLevel;
			
			if(backgrounds!=null && backgrounds.length()>0) {
				def backgroundsList = backgrounds.split(",")
				for(String bgFeatureName: backgroundsList) {
					// Check if Scenario Name is specified with Feature Name
					def bgScenarioName = null;
					if(bgFeatureName.indexOf(":")>-1) {
						bgScenarioName = bgFeatureName.substring(bgFeatureName.indexOf(":") + 1);
						bgScenarioName = bgScenarioName?.trim();
						bgFeatureName = bgFeatureName.substring(0, bgFeatureName.indexOf(":"))
					}
					log.info("bgScenarioName is ${bgScenarioName}")
					bgFeatureName = bgFeatureName?.trim();
					Feature bgFeature = bgParser.parse(bgFeatureName)
					log.info("Processing BGFeature: " + bgFeatureName)
					if(bgFeature) {
						for(Scenario sc: bgFeature.scenarios) {
							// TODO: Add the scenarios recursively
							log.info("Background Scenario Name is: ${sc.scenarioName}")
							sc.type = "background"
							if(bgScenarioName!=null && bgScenarioName.length()>0) {
								// Add only specified background scenario
								if(bgScenarioName.equalsIgnoreCase(sc.scenarioName)) {
									bgScenarios.add (sc);
								}
							} else {
								bgScenarios.add (sc);
							}
						}
					}
				}
				log.info("Number of backgrounds to run: " + bgScenarios.size())
			}
		} // End of bgParsingLevel Check
	}
	
	/* Recursively add scenarios from bg features */
	def addBackgrroundScenarios () {
		
	}

    def generateReport() {
        report.description = definition?.join('\\n')
        report.keyword = 'Feature'
		// Add background scenarios as well
		bgScenarios?.eachWithIndex { bgSc, index ->
			log.info("Generating Background Reports " + bgSc.type)
			bgSc?.generateReport(index+1)
		}
        scenarios?.eachWithIndex { it, index ->
			bgScenarios?.each { bgSc ->
				log.info("Adding Background before Scenario " )
				report?.elements?.add (bgSc.element);
			}
            report?.elements?.add(it?.generateReport(index+1))
        }
		log.info("Reports Elements Size: " + report?.elements?.size())
        report
    }

    def tokenize() {
        def commands = new ArrayList()
        scenarios?.eachWithIndex { scenario, index ->
            commands.addAll(scenario.tokenize())
        }
        commands
    }
}
