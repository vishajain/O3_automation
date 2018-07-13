package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

/**
 * Created by Samir on 19/12/2017
 */

final class IPVPage extends WebPage {

	//Override
	def populateData = {browser, formKey, formData ->
		new IPVForm().populateFields(browser, formData);
	}

	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in IPVForm")
		new IPVForm().submit(browser, formData)
	}

	static final class IPVForm extends WebForm {

		//ChatNow form elements
		private static final def UPLOAD = ".//*[@id='ipvFilePicker']"

		private static final def FIELDS = [UPLOAD]

		// The Form error fields.
		private static final def FORM_ERROR = ".//*[@class='error_message']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR]

		//error message map (Key-Value Pair)
		def static final IPVPageErrorMessageMap = [invalidETFfile_uploaded: "You have uploaded wrong File. Please upload correct ETF file",
			invalid_size: "Please upload a file smaller than 10 MB"]

		//To enter data
		def static final populateFields = {}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, UPLOAD, data, ERROR_MESSAGE_FIELDS
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, IPVPageErrorMessageMap)
			def outcome
			if(actualValidationMsgKeys.size() != 0){
				outcome = new SuccessOutcome();
				outcome.setResultData(actualValidationMsgKeys)
			}else{
				browser.delay(5000)
				outcome = new SuccessOutcome()
			}
			return outcome
		}

		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			println "Data[0] "+data[0]
			def file = data[0]
			List<String> actualMsg = []
			browser.uploadFile(UPLOAD, file.trim())
			//Get the error messages
			browser.delay(3000)
			if(browser.isAlertPresent()){
				actualMsg = browser.alertMsg()
			}else{
				actualMsg = []
			}
			actualMsg
		}
	}
}