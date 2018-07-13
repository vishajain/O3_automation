package com.O3.app.pages.funds

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser
import com.O3.dbConnection.DBConnect
import com.O3.app.pages.O3CommonPage

/**
 * Created by Visha on 29/05/18
 */

final class AddFundPage extends WebPage {

	//Override
	def populateData = {browser, formKey, formData ->

		if(formKey.equalsIgnoreCase("payByChequeAmount")) {
			new ChequePaymentForm().populateFields(browser, formData)
		} else if(formKey.equalsIgnoreCase("onlineAmount")) {
			new OnlinePaymentForm().populateFields(browser, formData)
		}
	}

	//Override
	def submit = {browser, formKey, formData ->

		if(formKey.equalsIgnoreCase("payByChequeAmount")) {
			new ChequePaymentForm().submit(browser, formData)
		} else if(formKey.equalsIgnoreCase("onlineAmount")) {
			new OnlinePaymentForm().submit(browser, formData)
		}
	}

	//To approve the fund request
	def static approveFundRequest = {browser, formData ->
		new ChequePaymentForm().approveFundRequest browser, formData
	}
	
	//To delete transactions and update NetFundAmount and CapitalAmount
	def static deleteTransactions = {browser, formData ->
	new ChequePaymentForm().deleteTransactions browser, formData
	}


	static final class ChequePaymentForm extends WebForm {

		//Pay By Cheque form elements
		private static final def CHEQUE_AMOUNT = ".//input[@placeholder='Enter amount']"

		private static final def CHEQUE_NUMBER = ".//input[@placeholder='Enter cheque number']"

		//private static final def CHEQUE_DATE = ".//input[@class='rw-widget-input rw-input']"

		private static final def UPLOAD_CHEQUE = ".//*[@id='chequeFilePicker']" //".//*[@id='chequeFilePicker']/.." //".//input[@id='chequeFilePicker']"
		
		private static final def FIELDS = [CHEQUE_AMOUNT, CHEQUE_NUMBER, UPLOAD_CHEQUE]

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"

		//		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR]

		//error message map (Key-Value Pair)
		def static final payByChequePageErrorMessageMap = [
			invalid_size:"Please upload a file smaller than 10 MB"
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			browser.delay(3000)
			println ("ChequePaymentForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(CHEQUE_AMOUNT)){
						if(O3CommonPage.minAmount == null){
							O3CommonPage.transactionAmount = formData[i]
						}else{
							println " O3CommonPage.minAmount : "+O3CommonPage.minAmount
							formData[i] = O3CommonPage.minAmount
							O3CommonPage.transactionAmount = formData[i]
						}
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, UPLOAD_CHEQUE, WAIT_REQ_FIELDS)

			}
			return outcome
		}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, UPLOAD_CHEQUE, data, ERROR_MESSAGE_FIELDS
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, payByChequePageErrorMessageMap)
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
			def file = data[2]
			List<String> actualMsg = []
			browser.uploadFile(UPLOAD_CHEQUE, file.trim())
			browser.delay(3000)
			if(browser.isAlertPresent()){
				actualMsg = browser.alertMsg()
			}else{
				actualMsg = []
			}
			actualMsg
		}

		def static final approveFundRequest = {browser,formData ->
			browser.delay(1000)
			if((O3CommonPage.mobileNumber)!= null){
			String query1 = "update user_payment_details set status = 3 where transactionId = '"+O3CommonPage.transactionId+"'"
			String query2 = "update user set netFundAmount = "+O3CommonPage.newCash+", capitalAmount = "+O3CommonPage.newCapital+" where mobileNumber = '"+O3CommonPage.mobileNumber+"';"

			println "query1: "+ query1
			println "query2: "+ query2

			def updateStatus = DBConnect.returnQuery(query1)
			def updateFund = DBConnect.returnQuery(query2)
			return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DatabaseIssue", "Reject query execution failed");
			}
		}
		
		def static final rejectFundRequest = {browser, formData ->
			if((O3CommonPage.mobileNumber)!= null){
				String query1 = "update user_payment_details set status = 3 where transactionId = '"+O3CommonPage.transactionId+"'"
				println "query1: "+ query1
				def updateStatus = DBConnect.returnQuery(query1)
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DatabaseIssue", "Rejection query execution failed");
			}
		}
		
		//To delete transactions and update NetFundAmount and CapitalAmount
		def static final deleteTransactions = {browser, formData ->
			if((O3CommonPage.mobileNumber)!=null){
				String query1 = "select id from user where mobileNumber = '"+O3CommonPage.mobileNumber+"';"
				def getUserID = DBConnect.returnQuery(query1)

				String query2 = "delete from user_payment_details where userId = "+getUserID[0]+";"
				String query3 = "update user set netFundAmount = 0, capitalAmount = 0 where mobileNumber = "+O3CommonPage.mobileNumber+";"
				String query4 = "delete from user_online_payment_dump where userPaymentDetailsTransactionID = '"+O3CommonPage.transactionId+"';"

				println "query4 "+query4+"\nquery2 "+query2+"\nquery3 "+query3
				def deleteOnlineTransaction = DBConnect.returnQuery(query4);
				def deleteChequeTransaction = DBConnect.returnQuery(query2);
				def zeroBalance = DBConnect.returnQuery(query3)

				println "<--balance turned to zero-->"
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DatabaseIssue", "Database query execution failed");
			}
		}
	}

	static final class OnlinePaymentForm extends WebForm {

		//Login form elements

		private static final def ENTER_AMOUNT = ".//input[@placeholder='Enter amount']"

		private static final def ADD_FUND = ".//input[@value='Add fund']"

		private static final def FIELDS = [ENTER_AMOUNT]

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"

		//		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR]

		//error message map (Key-Value Pair)
		def static final payOnlinePageErrorMessageMap = [
			enter_amount:"Enter amount to proceed further!",
			min_amount_req:"Enter minimumrs to Proceed further!"
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			browser.delay(3000)
			println ("OnlinePaymentForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				payOnlinePageErrorMessageMap.replace("min_amount_req" ,"rs",O3CommonPage.minAmount)
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(ENTER_AMOUNT)){
						O3CommonPage.transactionAmount = formData[i]
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, ADD_FUND, WAIT_REQ_FIELDS)

			}
			return outcome
		}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, ADD_FUND, data, ERROR_MESSAGE_FIELDS
			browser.delay(3000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, payOnlinePageErrorMessageMap)
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}

		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			//Get the error messages
			List<String> actualMsg = []
			browser.delay(1000)
			if(browser.isAlertPresent()){
				actualMsg = browser.alertMsg()
			}else{
				actualMsg = browser.getValidationMessages errFields
				println "actualMsg : "+actualMsg
			}
			actualMsg
		}
	}
}
