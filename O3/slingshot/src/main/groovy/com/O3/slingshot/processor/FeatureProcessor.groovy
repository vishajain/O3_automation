package com.O3.slingshot.processor

import groovy.util.logging.Log4j

@Log4j
class FeatureProcessor {

    def features

    def reportGenerator

    def featureParser

    def featureValidator

    def featureTokenizer

    def featureExecutor	

	def scanFeatures () {
		log.info("Scanning Features")
		featureParser.scanFeatures(features);
	}
	
    def process() {
        def reports = []
		
		log.info("Executable Features: ${featureParser.executableFeatures.size()}");
		featureParser.executableFeatures.each { feature ->
			log.info("Executable Feature: ${feature.definition}")
			feature.scenarios.each {sc ->
				log.info ("Executable Scenario Name: " +sc.scenarioName + ", Groups: " +sc.groups+ ", Steps:" + sc.steps.size());
				log.info ("Scenario Data is: " +sc.data)
				sc.steps.each { step -> 
					log.info("Executable Step, Data is : " + step.data)
				}
			}
			try {
				// send feature to execute
				featureExecutor.execute(feature)
				
				// Generate report for the executed feature
				reports << feature?.generateReport()
			} catch (Exception e) {
				log.error(e, e)
			}
		}
		reportGenerator.generateReport(reports)
		return;
    }

    def process(def features) {
        this.features = features
        process()
    }
	
	def close(){
		featureExecutor.close()
	}

}
