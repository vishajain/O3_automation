package com.O3.slingshot

import net.masterthought.cucumber.ReportBuilder
import org.junit.Test

class ReportGenerationTest {

    @Test
    def void reportGenerateTest() throws Exception {
        File rd = new File('reports/slingshot')
        List<String> jsonReports = new ArrayList<String>()
        jsonReports.add('reports/project.json')
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, true, true, false, "", false)
        reportBuilder.generateReports()
    }

}
