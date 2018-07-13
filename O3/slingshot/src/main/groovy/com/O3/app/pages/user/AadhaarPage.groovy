package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

final class AadhaarPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new AadhaarForm().populateFields(browser, formData);
	}
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in AadhaarPage")
		new AadhaarForm().submit(browser, formData)
	}
	
	static final class AadhaarForm extends WebForm {

		//Aadhaar form elements
		private static final def AADHAAR_NUMBER = ".//*[@id='aadhaarNumber']" 

		private static final def VERIFY_AADHAAR = "//button[@type='submit'][.='Verify']"
		
		private static final def DONT_HAVE_AADHAAR = ".//a[@href='/aadhaardetails']"
		
		private static final def FIELDS = [AADHAAR_NUMBER]
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = "//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final AadhaarPageErrorMessageMap = [
			aadhaar_req:"Please enter valid aadhaar number",
			aadhaar_dont_exist:"X106: Invalid Aadhaar number specified",
			duplicate_aadhaar:"Given Aadhar Number already exists"
			]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("AadhaarForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				outcome = WebForm.enterData(browser, formData, FIELDS, VERIFY_AADHAAR, WAIT_REQ_FIELDS)
			} 
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {			
			def actualValidationMsg = submitForm browser, FIELDS, VERIFY_AADHAAR, data, ERROR_MESSAGE_FIELDS
			browser.delay(2000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, AadhaarPageErrorMessageMap)			
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}
		
		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			browser.getValidationMessages errFields // get the validation messages from the current page.			
		}
	}	
}
