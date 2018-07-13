package com.O3.slingshot.reader

import groovy.transform.Immutable

/**
 * Created by neeraj on 12/26/13.
 */
class DataReader {
    //def static DATA_FILE = "TestData/InputData.xlsx"
	
	def inputDataFile;
	
    public Object[][] read(def testName) {
		XLSReader.read(inputDataFile, testName)
    }
}
