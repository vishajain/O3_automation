package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

/**
 * Created by Sandhya on 24/9/14
 */

final class ResetPasswordPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new ResetPasswordForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in ResetPasswordPage")
		new ResetPasswordForm().submit(browser, formData)
	}	
	
	static final class ResetPasswordForm extends WebForm {

		//ResetPasswordPage form elements
		private static final def MOBILE_NUMBER = ".//*[@id='mobileNumber']" 

		private static final def GET_OTP = ".//button[.='Get OTP'][@class='btn btn-primary btn-block btn-md']"
		
		private static final def FIELDS = [MOBILE_NUMBER]	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = "//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final ResetPasswordPageErrorMessageMap = [
			mobile_invalid:"Please enter valid mobile number",
			user_not_registred:"User does not exist"
			]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("ResetPasswordPageForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(MOBILE_NUMBER)){
						O3CommonPage.mobileNumber = formData[i]
						println "O3CommonPage.mobileNumber " +O3CommonPage.mobileNumber
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, GET_OTP, WAIT_REQ_FIELDS)
				
			} 
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {			
			def actualValidationMsg = submitForm browser, FIELDS, GET_OTP, data, ERROR_MESSAGE_FIELDS	
			browser.delay(2000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, ResetPasswordPageErrorMessageMap)			
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
