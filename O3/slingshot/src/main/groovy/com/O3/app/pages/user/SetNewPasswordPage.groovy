package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage


final class SetNewPasswordPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new SetNewPasswordForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in SetNewPasswordPage")
		new SetNewPasswordForm().submit(browser, formData)
	}
	
	static final class SetNewPasswordForm extends WebForm {

		//SetNewPassword form elements
		private static final def PASSWORD = ".//*[@id='password']" 

		private static final def CONFIRM_PASSWORD = ".//*[@id='confirmpassword']"  		

		private static final def SET_PASSWORD = ".//button[@type='submit'][.='Set Password']"
		
		private static final def FIELDS = [PASSWORD, CONFIRM_PASSWORD]	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = "//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final SetNewPasswordPageErrorMessageMap = [
			pass_req:"Please enter valid password",
			conf_pass_req:"Please re-enter password",
			pass_not_matching:"Password doesn't match!"
			]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("SetNewPasswordForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				outcome = WebForm.enterData(browser, formData, FIELDS, SET_PASSWORD, WAIT_REQ_FIELDS)	
			} 
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {			
			def actualValidationMsg = submitForm browser, FIELDS, SET_PASSWORD, data, ERROR_MESSAGE_FIELDS	
			browser.delay(2000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, SetNewPasswordPageErrorMessageMap)			
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
