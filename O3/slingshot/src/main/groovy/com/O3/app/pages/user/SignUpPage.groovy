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

final class SignUpPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new SignUpForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in SignUpPage")
		new SignUpForm().submit(browser, formData)
	}
	
	static final class SignUpForm extends WebForm {

		//SignUp form elements
		
		private static final def FULL_NAME = "//input[@name='name']"
		
		private static final def MOBILE_NUMBER = "//input[@name='mobileNumber']"
		
		private static final def EMAIL = ".//input[@name='email']"
		
		private static final def PASSWORD = ".//input[@name='password']" 
		
		private static final def CONFRIM_PASSWORD = ".//input[@name='confirmpassword']"
		
		private static final def TERMS_AND_CONDITION = ".//label[@class='text-muted small-checkbox']"
		
		private static final def GENERATE_OTP = ".//button[@class='btn btn-primary btn-block btn-md'][.='Generate OTP']"
		
		private static final def FIELDS = [FULL_NAME, MOBILE_NUMBER, EMAIL, PASSWORD, CONFRIM_PASSWORD, TERMS_AND_CONDITION]	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final SignUpPageErrorMessageMap = [
			full_name_req:"Please enter your full name",
			mobile_req:"Please enter valid mobile number",
			email_req:"Please enter valid email",
			pass_invalid:"Please enter valid password",
			conf_pass_invalid:"Please re-enter password",
			pass_mismatch:"Password doesn't match!",
			duplicate_email_pass:"Given phone number or email already exists!"
			]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("SignUpForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(MOBILE_NUMBER)){
						O3CommonPage.mobileNumber = formData[i]
						println "O3CommonPage.mobileNumber " +O3CommonPage.mobileNumber
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, GENERATE_OTP, WAIT_REQ_FIELDS)
			} 
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, GENERATE_OTP, data, ERROR_MESSAGE_FIELDS
			browser.delay(3000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, SignUpPageErrorMessageMap)			
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
