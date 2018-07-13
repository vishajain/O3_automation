
package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage
import com.O3.dbConnection.DBConnect

/**
 * Created by Sandhya on 24/9/14
 */

final class GenerateOTPPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new GenerateOTPForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in GenerateOTPPage")
		new GenerateOTPForm().submit(browser, formData)
	}	
	
	//To verify the Generated OTP
	def static getOTPFromDB = {browser, formData ->
		new GenerateOTPForm().getOTPFromDB browser, formData
	}
	
	//To delete a user after registration
	def static deleteUserFromDB = { browser, formData ->
		new GenerateOTPForm().deleteUserFromDB browser, formData
	}
	
	//To make the step back to PAN page
	def static stepBackToPAN = { browser, formData ->
		new GenerateOTPForm().stepBackToPAN browser, formData
	}
	
	//To delete the PAN number from db
	def static deletePAN = { browser, formData ->
		new GenerateOTPForm().deletePAN browser, formData
	}
	
	//To make the step back to Upload Documents page
	def static stepBackToUploadDocuments = { browser, formData ->
		new GenerateOTPForm().stepBackToUploadDocuments browser, formData
	}
	
	//to delete uploaded documents from database
	def static deleteDocuments = {browser, formData ->
		new GenerateOTPForm().deleteDocuments browser, formData
	}
	
	//to make the step back to document verification page
	def static stepBackToVerification = {browser, formData ->
		new GenerateOTPForm().stepBackToVerification browser, formData
	}
	
	static final class GenerateOTPForm extends WebForm {

		//GenerateOTP form elements
		private static final def OTP = ".//*[@id='otp']" 
		
		private static final def REGISTER = "//button[@type='submit'][@class='btn btn-primary btn-block btn-md']"
		
		private static final def FIELDS = [OTP]	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = "//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final GenerateOTPPageErrorMessageMap = [
			otp_veri_fail:"OTP verification failed, please try again",
			otp_invalid:"Please enter valid OTP"
			]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("GenerateOTPForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(OTP)){
						if(formData[i].equals(" ")){
							formData[i] = O3CommonPage.generatedOTP[0]
						}
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, REGISTER, WAIT_REQ_FIELDS)	
			}
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {			
			def actualValidationMsg = submitForm browser, FIELDS, REGISTER, data, ERROR_MESSAGE_FIELDS	
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, GenerateOTPPageErrorMessageMap)			
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}
		
		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			browser.delay(1500)
			browser.getValidationMessages errFields // get the validation messages from the current page.			
		}	
		
		//To verify the Generated OTP
		def static getOTPFromDB = {browser, formData ->
			String query = "select code from otp where mobileNumber = '"+O3CommonPage.mobileNumber+"' and type = "+formData[0]+""
			def otp = DBConnect.returnQuery(query)
			O3CommonPage.generatedOTP = otp
			println "O3CommonPage.generatedOTP : "+O3CommonPage.generatedOTP
			return new SuccessOutcome()
		}
		
		//To delete a user after registration
		def static deleteUserFromDB = {browser, formData ->
			String query = "delete from user where mobileNumber = '"+O3CommonPage.mobileNumber+"'"
			println "query :"+query
			def userDeleted = DBConnect.returnQuery(query)
			return new SuccessOutcome()
		}
		
		//To make the step back to PAN page
		def static stepBackToPAN = { browser, formData ->
			//update user set step = 0 where id = 235
			println "mobile number : "+O3CommonPage.mobileNumber
			String query = "update user set step = 100 where mobileNumber = '"+O3CommonPage.mobileNumber+"'"
			String query2 = "update user set pan = null where mobileNumber = '"+O3CommonPage.mobileNumber+"'"
			String query3 = "select id from user where mobileNumber = '"+O3CommonPage.mobileNumber+"'"
			
			def userUpdated = DBConnect.returnQuery(query)
			def setPANToNull = DBConnect.returnQuery(query2)
			def getUserID = DBConnect.returnQuery(query3)
			//String userID = getUserID.split("[")[1].split("]")[0]
			String query4 = "delete from user_details where userID = "+getUserID[0]
			String query5 = "delete from user_bank_details where userID = "+getUserID[0]
			String query6 = "delete from user_address where userID = "+getUserID[0]
			String query7 = "delete from user_document where userID = "+getUserID[0]
			println "query 4 "+query4
			def deleteUserDetails = DBConnect.returnQuery(query4)
			def deleteUserBankDetails = DBConnect.returnQuery(query5)
			def deleteUserAddress = DBConnect.returnQuery(query6)
			def deleteUserDocument = DBConnect.returnQuery(query7)
			return new SuccessOutcome()
		}
		
		//To delete the PAN number from db
		def static final deletePAN = { browser, formData ->
			String query = "update user set pan = null where pan = '"+formData[0]+"'"
			println "query :"+query
			def userDeleted = DBConnect.returnQuery(query)
			return new SuccessOutcome()
		}
		
		//To make the step back to Upload Documents page
		def static final stepBackToUploadDocuments = {browser, formData ->
			println "mobile number : "+O3CommonPage.mobileNumber
			String query = "select id from user where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			println "query :: "+query
			def getUserID = DBConnect.returnQuery(query)
			
			String query2 = "update user set step = 5 where mobileNumber = "+O3CommonPage.mobileNumber+";"
			String query3 = "update user set isEsigned = 0 where mobileNumber = "+O3CommonPage.mobileNumber+";"
			String query4 = " delete from user_document where documentTypeID IN (2,3,4,7) and userID = "+getUserID[0]+";"

			def backToUploadDocuments = DBConnect.returnQuery(query2)
			def updateESign = DBConnect.returnQuery(query3)
			def deleteDocument = DBConnect.returnQuery(query4)
			
			return new SuccessOutcome()
		}
		
		//to delete uploaded documents from database
		def static final deleteDocuments = {browser, formData ->
			println "mobile number : "+O3CommonPage.mobileNumber
			String query = "select id from user where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			def getUserID = DBConnect.returnQuery(query)
			
			String query4 = " delete from user_document where documentTypeID IN (2,3,4,7) and userID = "+getUserID[0]+";"
			
			def deleteDocument = DBConnect.returnQuery(query4)
			
			return new SuccessOutcome()
		}
		
		//to make the step back to document verification page
		def static final stepBackToVerification= {browser, formData ->
			println "mobile number : "+O3CommonPage.mobileNumber
			
			String query = "select id from user where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			println "query :"+query
			def getUserID = DBConnect.returnQuery(query)
			
			String query2 = "update user set isUserVerified = 0 where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			String query3 = "update user set isEsigned = 0 where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
			String query4 = "update user_document set status = 0 where documentTypeID IN (2,3,4,7) AND userID = "+getUserID[0]+";"
			
			def setUserVerified = DBConnect.returnQuery(query2)
			def setEsigned = DBConnect.returnQuery(query3)
			def changeDocumentStatus = DBConnect.returnQuery(query4)
			
			return new SuccessOutcome()
		}
	}	
}
