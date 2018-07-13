package com.O3.slingshot.report

import com.O3.slingshot.aggregator.report.Report
import groovy.util.logging.Log4j
import net.masterthought.cucumber.ReportBuilder

@Log4j
class ReportGenerator {

    def generateReport(List<Report> reports) {
        log.info "Generating the reports"
        if (reports) {
            def reportsAsJson = new StringBuilder()
            reportsAsJson.append('[')
            reports?.eachWithIndex { report, index ->
                reportsAsJson.append(report.json())
                if (index < reports.size() - 1) {
                    reportsAsJson.append(',')
                }
            }
            reportsAsJson.append(']')

            File reportJson = new File('reports/report.json')
            reportJson.write(reportsAsJson.toString())

            File rd = new File('reports/slingshot')
            rd.delete()
            List<String> jsonReports = new ArrayList<String>()
            jsonReports.add('reports/report.json')
            ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "", "SlingShot Reports", false, false, true, false, false, "", false)
            reportBuilder.generateReports()
        }
        log.info "Generating the reports completed"
    }
}
