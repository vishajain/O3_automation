package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

final class PanPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new PanForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in PanPage")
		new PanForm().submit(browser, formData)
	}
	
	static final class PanForm extends WebForm {

		//Pan form elements
		private static final def PAN_NUMBER = ".//*[@id='panNumber']" 

		private static final def DATE_OF_BIRTH = ".//*[@id='rw_1_input']"  		

		private static final def VERIFY_PAN = ".//button[@class='btn btn-primary btn-block btn-md']"
												
		private static final def FIELDS = [PAN_NUMBER, DATE_OF_BIRTH]	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final PanPageErrorMessageMap = [
			pan_req:"Please enter valid PAN",
			duplicate_pan:"Given PAN already exists!",
			pan_num_invalid:"Invalid PAN"
			]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("PanForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(PAN_NUMBER)){
						O3CommonPage.pan = formData[i]
					}
					if(FIELDS[i].equals(DATE_OF_BIRTH)){
						O3CommonPage.DOB = formData[i]
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, VERIFY_PAN, WAIT_REQ_FIELDS)
			} 
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, VERIFY_PAN, data, ERROR_MESSAGE_FIELDS
			browser.delay(50000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, PanPageErrorMessageMap)			
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}
		
		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			browser.getValidationMessages errFields // get the validation messages from the current page.			
		}	
		
		/*//To verify the logged in user is displayed
		def static final correctUserLoggedIn = {browser, formData ->
			if(browser.isDisplayed(LOGGEDIN_EMAIL)){
				println "Email displayed : " +browser.gettext(LOGGEDIN_EMAIL, "value")
				if(O3CommonPage.userName.equalsIgnoreCase(browser.gettext(LOGGEDIN_EMAIL, "value"))){
					println "Success"
					return new SuccessOutcome()
				}else{
					return O3CommonPage.returnFailureOutcome(browser, "UserNotMatching", "Logged in user is not matching the displayed user.")
				}
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "UserDisplayIssue", "User is not displayed.")
			}
		}*/
	}	
}
