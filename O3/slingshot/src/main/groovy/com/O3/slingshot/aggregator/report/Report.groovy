package com.O3.slingshot.aggregator.report

import groovy.util.logging.Log4j

@Log4j
class Report {

    def id = UUID.randomUUID().toString()

    def description

    String name

    def keyword = 'Feature'

    def elements = []

    def json() {
        StringBuilder featureBuilder = new StringBuilder()
        featureBuilder
                .append('{')
                .append('"id":"').append(id).append('",')
                .append('"description":"').append(description).append('",')
                .append('"name":"').append(name).append('",')
                .append('"keyword":"').append(keyword).append('",')
                .append('"elements":[')
        elements?.eachWithIndex { element, index ->
			featureBuilder.append(element.json())
            if (index < elements.size() - 1) {
                featureBuilder.append(',')
            }
        }
        featureBuilder.append('],')
        featureBuilder.append('"uri": "com/O3/slingshot/')
                .append(name?.replaceAll(' ', '_'))
                .append('.feature"')
        featureBuilder.append('}')
    }
}
