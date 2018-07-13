package com.O3.slingshot.aggregator.report

import groovy.util.logging.Log4j

@Log4j
class Element {

    def id = UUID.randomUUID().toString()

    def description

    def name

    def keyword
	
	def type
	
	def tags = []
	
    def steps = []

    def json() {
        def elementBuilder = new StringBuilder()
        elementBuilder
                .append('{')
                .append('"id":"').append(id).append('",')
                .append('"description":"').append(description).append('",')
                .append('"name":"').append(name).append('",')
                .append('"keyword":"').append(keyword).append('",')
				.append('"type":"').append(type).append('",')
		if(tags) {
			elementBuilder.append('"tags":[')
			tags?.each {tag->
				elementBuilder.append('{"name":"'+tag+'"}')
			}
			elementBuilder.append('],')
		}
        if (steps) {
            elementBuilder.append('"steps":[')
            steps?.eachWithIndex { step, index ->
				elementBuilder.append(step?.json())
                if (index < steps?.size() - 1) {
                    elementBuilder.append(',')
                }
            }
            elementBuilder.append(']')
        }
        //elementBuilder.append('"keyword":"').append(keyword?.toLowerCase()).append('"')
        elementBuilder.append('}')
        elementBuilder
    }
}
