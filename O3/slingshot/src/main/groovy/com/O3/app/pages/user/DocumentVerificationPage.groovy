package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage
import com.O3.dbConnection.DBConnect

final class DocumentVerificationPage extends WebPage {

	/*//Override	
	def populateData = {browser, formKey, formData ->
		new DocumentVerificationForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in DocumentVerificationPage")
		new DocumentVerificationForm().submit(browser, formData)
	}
	*/
	//To verify that the continue esign button is enabled in document verification page
	def static esignButtonEnabled = { browser, formData ->
		new DocumentVerificationForm().esignButtonEnabled browser, formData
	}
	
	//To give access to user to navigate to dashboard from document verification through db
	def static navigateUserToDashboard = { browser, formData ->
		new DocumentVerificationForm().navigateUserToDashboard browser, formData
	}
	
	static final class DocumentVerificationForm extends WebForm {

		//DocumentVerification form elements
		private static final def CONTINUE_ESIGN = ".//a[@class='btn btn-primary btn-md-custom']" 
		
//		private static final def CONFIRM = ".//a[.='Continue']"
		
//		private static final def CANCEL = ".//a[.='Cancel']"
		
		private static final def FIELDS = []	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final DocumentVerificationPageErrorMessageMap = [
		]
		
		//To logout from the application
		def static final esignButtonEnabled = {browser, formData ->
			if(browser.isDisplayed(CONTINUE_ESIGN)){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "ContinueESignDisplayIssue", "Continue ESign button is not displayed.")
			}
		}
		
		//To give access to user to navigate to dashboard from document verification through db
		def static final navigateUserToDashboard = { browser, formData ->
			//update user to dashboard
			println "mobile number : "+O3CommonPage.mobileNumber
			
			String query = "select id from user where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			def getUserID = DBConnect.returnQuery(query)
			println "getUserID : "+getUserID[0]
			
			String query2 = "update user set step = 99 where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			String query3 = "update user_document set status = 1 where userID = "+getUserID[0]+";"
			//String query4 = "update user set isEsigned = 1 where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			String query5 = "update user set isUserVerified = 1 where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			
			def userUpdated = DBConnect.returnQuery(query2)
			browser.delay(2000)
			def approveUserDocument = DBConnect.returnQuery(query3)
			//browser.delay(3000)
			//def setEsigned = DBConnect.returnQuery(query4)
			def setUserVerified = DBConnect.returnQuery(query5)
			
			browser.delay(3000)
			return new SuccessOutcome()
		}
		
	}	
}
