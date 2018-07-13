package com.O3.slingshot.builder

import com.O3.slingshot.model.Feature
import groovy.util.logging.Log4j

@Log4j
class FeatureBuilder {

    def scenarioAndStepBuilder

    def backgroundBuilder

    def backgroundParser

    def build(def featureText) {
        boolean parseFeature = false
        log.info "Feature file data is ${featureText}"
        def backgroundFiles
        Feature feature = new Feature()
        featureText?.eachLine { line ->
            // when the line starts with feature, keep parsing until a background
            // or scenario is encountered
            if (line?.trim()?.startsWith('Feature:')) {
                log.info "Received a feature"
                parseFeature = true
            }

            // as soon as you receive a Scenario or Background, stop parsing, feature parsing is complete
            else if (line?.trim()?.startsWith('Scenario:')) {
                parseFeature = false
            }

            // keep parsing the data
            if (parseFeature) {
                feature.definition << line
            }
        }
        feature.scenarios = scenarioAndStepBuilder.build(featureText)
        feature.backgrounds = backgroundBuilder.build(featureText)
        log.info "Feature building completed"
        feature
    }

}
