package com.O3.slingshot

import com.O3.slingshot.aggregator.report.Element
import com.O3.slingshot.aggregator.report.Report
import com.O3.slingshot.aggregator.report.Step
import groovy.util.logging.Log4j
import org.junit.Test

@Log4j
class SlingshotReportTest {

    //@Test
    def void jsonTest() throws Exception {
        Report report = new Report()
        report.id = UUID.randomUUID().toString()
        report.keyword = 'Feature'
        report.name = "Account Feature"
        report.description = "As a Account Holder\\nI want to withdraw cash from an ATM\\nSo that I can get money when the bank is closed"

        Element element = new Element()
        element.keyword = 'Scenario'
        element.description = ''
        element.name = 'Account has no funds'

        Step step = new Step()
        step.name = "I have a new credit card"
        step.keyword = "Given"
        step.result.duration = 1000000
        step.result.status = "passed"

        element.steps.add(step)
        step = new Step()
        step.name = "I have a new credit card"
        step.keyword = "Given"
        step.result.duration = 1000000
        step.result.status = "passed"

        element.steps.add(step)
        step = new Step()
        step.name = "I have a new credit card"
        step.keyword = "Given"
        step.result.duration = 1000000
        step.result.status = "passed"

        element.steps.add(step)
        step = new Step()
        step.name = "I have a new credit card"
        step.keyword = "Given"
        step.result.duration = 1000000
        step.result.status = "skipped"

        element.steps.add(step)
        step = new Step()
        step.name = "I have a new credit card"
        step.keyword = "Given"
        step.result.duration = 1000000
        step.result.status = "failed"

        element.steps.add(step)

        report.elements.add(element)

        log.info report.json()
        assert report.json()
    }

}
