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

final class LoginPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new LoginForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in LoginPage")
		new LoginForm().submit(browser, formData)
	}	
	
	/*//To verify the logged in user is displayed
	def static correctUserLoggedIn = {browser, formData ->
		new LoginForm().correctUserLoggedIn browser, formData
	}*/
	
	//To logout from the application
	def static logout = { browser, formData ->
		new LoginForm().logout browser, formData
	}
	
	static final class LoginForm extends WebForm {

		//Login form elements
		private static final def MOBILE_NUMBER = ".//input[@id='username']" 

		private static final def PASSWORD = ".//input[@id='password']"  		

		private static final def SIGNIN = ".//button[.='Login']"
		
		private static final def CLICK_USER = ".//li[@class='dropdown']/a"
		
		private static final def LOGOUT = ".//a[.='Logout']"
		
		private static final def FIELDS = [MOBILE_NUMBER, PASSWORD]	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final loginPageErrorMessageMap = [
			mobile_req:"Please enter valid mobile number or email Id",
			pass_req:"Please enter valid password",
			user_not_registered:"Login failed, invalid username/password"
			]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("LoginFormm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(MOBILE_NUMBER)){
						O3CommonPage.mobileNumber = formData[i]
						println "O3CommonPage.mobileNumber " +O3CommonPage.mobileNumber
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, SIGNIN, WAIT_REQ_FIELDS)
				
			} 
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {			
			def actualValidationMsg = submitForm browser, FIELDS, SIGNIN, data, ERROR_MESSAGE_FIELDS	
			browser.delay(3000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, loginPageErrorMessageMap)			
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}
		
		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			
			browser.click submitButton // submit the form.
			browser.delay(15000)
			browser.getValidationMessages errFields // get the validation messages from the current page.	
		}
		
		//To logout from the application
		def static final logout = {browser, formData ->
			if(browser.isDisplayed(CLICK_USER)){
				browser.click CLICK_USER
				if(browser.isDisplayed(LOGOUT)){
					browser.click LOGOUT
					return new SuccessOutcome()
				}else{
					return O3CommonPage.returnFailureOutcome(browser, "LogoutDisplayIssue", "Logout is not displayed.")
				}
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "UserNameDisplayIssue", "Username is not displayed.")
			}
		}
	}	
}
