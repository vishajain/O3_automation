package com.O3.slingshot.reader

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory


final class XLSReader {
    private def static final String FAILURE = "_Failure"

        // TBD - I don't think, this worx. need to look at it and fix where needed...
        def static Object[][] read(def fileName, def sheetName) {
            //def failureCol = sheetName.contains(FAILURE)
			def failureCol = true;
			
            def workbook = WorkbookFactory.create(new FileInputStream((String) fileName))
            def sheet = workbook.getSheet(sheetName)
			if(sheet==null) return;
            def rowLength = sheet.getLastRowNum()
            def colLength = sheet.getRow(0).getPhysicalNumberOfCells()

            def paramsCols = failureCol ? 2 : 1
            def params = new Object[rowLength][paramsCols]

            (1..rowLength).each {i -> // started with 1 because the 1st row is the headerrow.
                def paramList = []
                def errList = []

                (0..colLength - 1).each {j ->
                    def row = sheet.getRow(i)
                    def cellValue = ""
					if(row.getCell(j))
						cellValue = row.getCell(j).getStringCellValue()

                    if (failureCol && (j == colLength - 1)) {
                       errList.add(cellValue) // need to make a list of cell values and add them to errList.
                    } else {
                       paramList.add(cellValue)
                    }
                }

                params [i - 1] [0] = paramList

                if (failureCol & errList?.size() > 0) {
                    params [i - 1] [1] = errList
                }
            }
			println "Params  :" +params
            //listData
            params
        }
}
