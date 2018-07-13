package com.O3.slingshot.parser

import groovy.io.FileType
import groovy.util.logging.Log4j

import com.O3.slingshot.model.Feature
import com.O3.slingshot.spring.SpringContextManager

@Log4j
class FeatureParser {

    def folder

    def extension
	
	def bgParsingLevel = 0;
	
	def executableFeatures = new ArrayList<Feature>();
	
	def dataReader ;//= SpringContextManager.getBean('dataReader')
	
	def groups;
	
	
	/** Scans the specification folder to get information about Features available */
	def scanFeatures (def specificFeatures) {
		
		def featureFilesList = []
		def specifiedFeatureArray = specificFeatures?.split(',');
		specifiedFeatureArray.each { fileName ->
			fileName=fileName?.trim();
			if(fileName.length()==0) return;
			featureFilesList.add(new File(folder + fileName + extension))
		}
		
		def dir = new File(folder)
		dir.eachFile (FileType.FILES) { file ->
			if(! file.name.endsWith(extension)) return;
			log.info ("Found file: " + file.getName())
			def featureName = file.getName().substring(0, file.getName().indexOf("."))
			if(specifiedFeatureArray.contains(featureName)) return;
			featureFilesList << file
		}
		
		log.info("Groups specified ${groups}")
		def groupsToExecute = [];
		if(groups!=null) {
			groupsToExecute = groups?.split(',');
		} else {
			
		}
		
		log.info("Groups To Execute " + groupsToExecute);
		log.info("Total Feature files: " + featureFilesList.size())
		featureFilesList.eachWithIndex { file,index -> 
			//if(index>4) return; // TODO: Remove later
			log.info("Parsing Feature: " + file.getName())
			def feature = parse (file);
			log.info "Scenarios Found: " + feature.scenarios.size()
			def explicitlySpecifiedFeature = false;
			def featureName = file.getName().substring(0, file.getName().indexOf("."))
			if(specifiedFeatureArray.contains(featureName)) {
				explicitlySpecifiedFeature = true;
			}
			Feature executableFeature = new Feature();
			executableFeature.definition = feature.definition;
			executableFeature.report = feature.report;
			
			feature.bgScenarios.each { bgSc->
				log.info ("BG Scenario Name: " +bgSc.scenarioName + ", Steps:" + bgSc.steps.size());
				log.info ("BG Process Data for Sheet: " + bgSc.sheetName)
				if(bgSc.sheetName!=null && bgSc.sheetName.length()>0) {
					def data = dataReader.read(bgSc.sheetName)
					if(data!=null && data.length>0) {
						bgSc.data = data // TODO: Process each data 
						bgSc.steps.each {step ->
							// TODO: Process each data in each step-level
							log.info("Processing BG Step " +step);
							if(step.sheetName!=null) {
								def stepData = dataReader.read(step.sheetName)
								if(stepData!=null && stepData.length>=0) {
									step.data = stepData[0]
								}
								log.info("Step data is ${step.data}")
							}
						}
					}
				}
			}
			feature.scenarios.each {sc->
				log.info ("Scenario Name: " +sc.scenarioName + ", Groups: " +sc.groups+ ", Steps:" + sc.steps.size());
				if(sc.ignore) {
					// This scenario is marked for ignore
					log.info("Ignoring scenario : " +sc.scenarioName);
					return;
				}
				def processThisScenario = false;
				for(def i=0;i<groupsToExecute.length;i++) {
					if(groupsToExecute[i].equalsIgnoreCase("ALL")) {
						processThisScenario = true
						break;
					}
					processThisScenario = sc.groups.contains(groupsToExecute[i])
					if(processThisScenario) break; 
				}
				log.info("processThisScenario " + processThisScenario + ", explicitlySpecifiedFeature " + explicitlySpecifiedFeature);
				
				//Check if scenario belongs to any group that needs to be executed, OR if feature is explicitly specified to be executed	
				if(processThisScenario || explicitlySpecifiedFeature) {
					// This scenario needs to be executed. Fetch data-set and decide how many times scenario needs to be executed.
					log.info("Process Data for Sheet: " + sc.sheetName)
					if(sc.sheetName!=null && sc.sheetName.length()>0) {
						def data = dataReader.read(sc.sheetName)
						log.info("Scenario Data found is " + data)
						if(data!=null && data.length>0) {
							data.eachWithIndex {dataEntry, dataIndex ->
								log.info("Create Scenario for data: " + dataEntry );
								def clonedSc = sc.cloneMe();
								clonedSc.data = [] //TODO: Make it as data-array for now - need to change in feature-executor
								clonedSc.data[0] = dataEntry;
								// Process each step in scenario and set the right data for execution
								log.info("Steps in scenario: "+clonedSc.steps.size());
								clonedSc.steps.each {step -> 
									//log.info("Process step: " +step.definition + " with data in " + step.sheetName)
									if(step.sheetName!=null) {
										def stepData = dataReader.read(step.sheetName)
										log.info("Use data of index ${dataIndex} for step, stepData is ${stepData.length}")
										if(stepData!=null && stepData.length>dataIndex) {
											step.data = stepData[dataIndex]	
										}
										log.info("Step data is ${step.data}")
									}
								}
								executableFeature.scenarios.add(clonedSc);
							}
						}
					} else {
						// No data found for scenario - add just one scenario entry
						executableFeature.scenarios.add(sc);
					}
				} // End of condition to check if scenario belongs to group
			}
			if(executableFeature.scenarios.size()>0) {
				// Add Background Scenarios to the feature as well
				executableFeature.bgScenarios = feature.bgScenarios;
				executableFeatures.add(executableFeature);
			}
		} // end of loop for parsing feature files
		
		return executableFeatures;
	}
	
	
	
	def parse(def String featureFileName) {
		log.info("Processing file name ${featureFileName}")
		parse(new File(folder + featureFileName + extension));
	}
    def parse(def File featureFile) {	
        log.info "Parsing all the feature in ${featureFile} from specification folder"
        //def featureText = new File(folder + featureFile + extension)?.text
		def featureText = featureFile?.text
        log.info "Contents is :: "
        log.info featureText

        Feature feature = new Feature()
		feature.bgParsingLevel = bgParsingLevel;

        if (featureText) {
            featureText?.eachLine { line ->
                feature.parse(line)
            }
        }
        //log.info "Feature is :: "
        //log.info feature
        feature
    }

}
