package com.O3.app.pages.funds

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage
import com.O3.app.dsl.ClickAction
import com.O3.tools.Browser
import com.O3.dbConnection.DBConnect
import com.O3.app.pages.O3CommonPage

/**
 * Created by Visha on 29/05/18
 */

final class RedeemFundPage extends WebPage {


	//Override
	def populateData = {browser, formKey, formData ->
		new RedeemForm().populateFields(browser, formData)
	}

	//Override
	def submit = {browser, formKey, formData ->
		new RedeemForm().submit(browser, formData)
	}


	static final class RedeemForm extends WebForm {

		//Pay By Cheque form elements

		//		REDEEM_FUND = ".//*[@class='hidden-xs hidden-sm portfolio-container document pull-right width-28 m_full_width']//div[3]/a"

		private static final def ENTER_AMOUNT = ".//input[@placeholder='Enter amount']"

		private static final def REDEEM = ".//input[@value='Redeem']"


		private static final def FIELDS = [ENTER_AMOUNT]

		// the error fields.
		private static final def FORM_ERROR = ".//div[@class='alert alert-danger toast']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR]

		//error message map (Key-Value Pair)
		def static final redeemPageErrorMessageMap = [
			amount_req: "Enter amount to proceed further!",
			amount_invalid: "Invalid data! Negative balance.",
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			browser.delay(3000)
			//O3CommonPage.newCash = Integer.parseInt(O3CommonPage.newCash)
			println " formData[0] "+formData[0]
			if(formData[0].equals("1")){
				formData[0] = O3CommonPage.generateAmountMoreThanCash(O3CommonPage.newCash)
			}else if(!formData[0].equals("0")){
				println " formData[1] "+formData[0]
				formData[0] = O3CommonPage.lessThanOrEqualToCashAmount(O3CommonPage.newCash)
			}
			println " formData[2] "+formData[0]
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				if(FIELDS[0].equals(ENTER_AMOUNT)){
					O3CommonPage.transactionAmount = formData[0]
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, REDEEM, WAIT_REQ_FIELDS)

			}
			return outcome
		}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, REDEEM, data, ERROR_MESSAGE_FIELDS	
			browser.delay(3000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, redeemPageErrorMessageMap)			
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
