package com.O3.slingshot.builder

import com.O3.slingshot.model.Scenario
import groovy.util.logging.Log4j

@Log4j
class ScenarioAndStepBuilder {

    def build(def featureText) {
        boolean parseScenario = false
        log.info "Scenario parsing started with data ${featureText}"
        def scenarios = new ArrayList<Scenario>()
        def scenario = new Scenario()
        featureText?.eachLine { line ->
            // when the line starts with scenario, keep parsing
            // rest of the file should be completely scenarios only
            if (line?.trim()?.startsWith('Scenario:')) {
                log.info "Received a scenario"
                parseScenario = true
                scenario = new Scenario()
            }

            // keep parsing the data
            if (parseScenario) {
                scenario.definition << line
                scenario.parse(line)
                scenarios << scenario
            }

        }
        log.info "Scenario parsing completed"
        scenarios
    }

}
