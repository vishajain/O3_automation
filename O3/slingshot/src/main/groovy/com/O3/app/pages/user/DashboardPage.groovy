package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

final class DashboardPage extends WebPage {

	//Override
	def submit = {browser, formKey, formData ->
		new DashboardForm().submit(browser, formData)
	}
	
	//To verify the updated transaction after adding amount
	def static amountUpdated = { browser, formData ->
		new DashboardForm().amountUpdated browser, formData
	}
	
	//To store the cash and Capital amount in Dashboard
	def static storedAmount = {browser, formData ->
		new DashboardForm().storedAmount browser, formData
	}
	
	//To update the cash and capital after the transactions from portfolio
	def static updateAmount = {browser, formData ->
		new DashboardForm().updateAmount browser, formData
	}

	static final class DashboardForm extends WebForm {

		//Dashboard elements
		private static final def REDEEM = ".//input[@value='Redeem']"
		
		private static final def CASH = ".//div[@class='full-width pull-left pb_10 border-bottom-light']/div[2]/span[2]"
		
		private static final def CAPITAL = ".//div[@class='full-width pull-left pb_10 border-bottom-light']/div[1]/span[2]"

		private static final def FIELDS = []

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"

		private static final def FIELD_ERROR = "//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]

		//error message map (Key-Value Pair)
		def static final dashboardPageErrorMessageMap = [
			no_balance:"Sorry Your Balance is Nil!"
		]

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, REDEEM, data, ERROR_MESSAGE_FIELDS
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, dashboardPageErrorMessageMap)
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
			List<String> actualMsg = []
			if(browser.isAlertPresent()){
				actualMsg = browser.alertMsg()
			}else{
				browser.click submitButton // submit the form.
				actualMsg = []
			}
			actualMsg
		}
		
		//To verify the updated transaction after adding amount
		def static final amountUpdated = { browser, formData ->
			def capital = Integer.parseInt((browser.gettext(CAPITAL)).split(" ")[1]);
			def netFund = Integer.parseInt((browser.gettext(CASH)).split(" ")[1]);
			println "capital "+capital+ "\ncapitalAmount "+O3CommonPage.newCapital
			println "netFund "+netFund+ "\ncashAmount "+O3CommonPage.newCash
			if(capital == O3CommonPage.newCapital && netFund == O3CommonPage.newCash){
				println "true.."
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "CapitalCashUpdateIssue", "Capital and Cash does not match.");
			}
		}
		
		//To store the cash and Capital amount in Dashboard
		def static final storedAmount = {browser, formData ->
			browser.delay(2000)
			if((browser.isDisplayed(CAPITAL))&&(browser.isDisplayed(CASH))){
				def currentCapitalAmount = browser.gettext(CAPITAL).split(" ")[1].replace(",", "");
				def currentNetFundAmount = browser.gettext(CASH).split(" ")[1].replace(",", "");
				O3CommonPage.newCapital = currentCapitalAmount;
				O3CommonPage.newCash = currentNetFundAmount;
				println "<--O3CommonPage.newCapital--> "+O3CommonPage.newCapital
				println "<--O3CommonPage.newCash--> "+O3CommonPage.newCash
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DashboardElementDisplayIssue", "Capital And Cash elements are not displayed");
			}
		}
		
		//To update the cash and capital after the transactions from portfolio
		def static final updateAmount = {browser, formData ->
			O3CommonPage.newCapital = Integer.parseInt(O3CommonPage.newCapital);
			O3CommonPage.newCapital = (O3CommonPage.newCapital + Integer.parseInt(O3CommonPage.transactionAmount));
			O3CommonPage.newCash = Integer.parseInt(O3CommonPage.newCash);
			return new SuccessOutcome()
		}
	}
}
