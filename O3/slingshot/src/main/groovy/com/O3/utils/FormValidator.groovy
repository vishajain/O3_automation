package com.O3.utils

import org.testng.Assert

/**
 * Created by sandhya on 1/26/14.
 */
class FormValidator {
	private def msg = {
		expected, errors -> """
            Expected :: ${expected}
            Errors   :: ${errors}
        """  }.memoize()

	def assertFormErrors = {expected, errors ->		
		
		// if NO errors expected but errors are present.
		if (!expected && (errors?.size() > 0)) {
			Assert.fail msg(expected, errors)
		}

		// conversely, if Errors expected but no errors are present.
		if (expected && (errors == null || errors?.size() == 0)) {
			Assert.fail msg(expected, errors)
		}

		// if both the corners are covered, then validate for each expectation.
		// TODO - still doesn't validate left overs properly - check the algo ...
		/*expected.each {
		 if (!(errors.find(it))) {
		 Assert.fail(msg)
		 } else {
		 errors.remove(it)
		 }
		 }*/
		else {
			//here we compare keys in expected(from xlsx) with the keys from page
			validateMessages(errors, expected)
		}
	}

	/**
	 * To validate the form and field validation messages
	 * @param actualValidationMsg   array containing the actual validation messages
	 * @param expected              array containing expected messages
	 * @param expectedMsgMap        expected message error map
	 */

	 def static final validateMessages(def actualValidationMsgKeys, def expected){

		def expectedValidationMsgKeys = []
		def expectedValidationMsgKeyString = expected[0]

		if(expectedValidationMsgKeyString.indexOf(',') != -1) {
			expectedValidationMsgKeys = expectedValidationMsgKeyString.split(',')
		} else {
			expectedValidationMsgKeys.add(expectedValidationMsgKeyString)
		}

		boolean matched = false
		if(expectedValidationMsgKeys.size() == actualValidationMsgKeys.size()) {
			a:	for(actualKey in actualValidationMsgKeys) {
				matched = false
				for(expectedKey in expectedValidationMsgKeys) {
					if(expectedKey == actualKey) {
						matched = true
						break
					} else {
						if(expectedKey == expectedValidationMsgKeys[expectedValidationMsgKeys.size()-1]) {
							break a
						}
					}
				}
			}
		}
		if(!matched){
			println "Expected Messages are............. :"
			for(expectedKeys in expectedValidationMsgKeys){
				println expectedKeys
			}
			println "Actual Messages are..............."
			for(actualKeys in actualValidationMsgKeys){
				println actualKeys
			}
		}
	}
}
