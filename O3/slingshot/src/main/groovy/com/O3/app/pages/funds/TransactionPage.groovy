package com.O3.app.pages.funds

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage
import com.O3.dbConnection.DBConnect
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Visha on 29/05/18
 */

final class TransactionPage extends WebPage {

	//To verify the new transaction in Transactions is displayed
	def static newTransaction = {browser, formData ->
		new TransactionForm().newTransaction browser, formData
	}

	//To verify the added fund on dashboard
	def static updatedFund = {browser, formData ->
		new TransactionForm().updatedFund browser, formData
	}

	//To verify the recent transaction on dashboard
	def static recentTransactions = {browser, formData ->
		new TransactionForm().recentTransactions browser, formData
	}

	//To append added amount into current amount
	def static updateCashNCapital = {browser, formData ->
		new TransactionForm().updateCashNCapital browser, formData
	}

	//To update status of fund request
	def static updateStatus = {browser, formData ->
		new TransactionForm().updateStatus browser, formData
	}

	//Override
	def populateData = {browser, formKey, formData ->
		new TransactionForm().populateFields(browser, formData)
	}

	//To filter the transactions in Transaction page
	def static filterTransactions = {browser, formData ->
		new TransactionForm().filterTransactions browser, formData
	}

	//To sort transactions based on amount
	def static amountBasedSort = {browser, formData ->
		new TransactionForm().amountBasedSort browser, formData
	}

	// TO verify reset button functionality
	def static verifyReset = {browser, formData ->
		new TransactionForm().verifyReset browser, formData
	}

	//To verify pagination on the page
	def static pagination = {browser, formData ->
		new TransactionForm().pagination browser, formData
	}

	static final class TransactionForm extends WebForm {

		//Pay By Cheque form elements
		private static final def AMOUNT = ".//div[@class='row no-margin col-container']//div[2]//div[3]"

		private static final def AMOUNT_LIST = ".//div[@class='row no-margin fs_14 border-bottom-light m_transaction_card']/div[3]"

		private static final def RECENT_TRANS_AMOUNT = ".//div[@class='col-md-12 side-table-height no-padding']/div[2]/div[4]"

		private static final def TRANSACTION_ID = ".//div[@class='row no-margin col-container']/div[2]/div[1]/div[2]/span[2]"

		private static final def CAPITAL = ".//div[@class='full-width pull-left pb_10 border-bottom-light']/div[1]/span[2]"

		private static final def NET_FUND = ".//div[@class='full-width pull-left pb_10 border-bottom-light']/div[2]/span[2]"

		private static final def PAYMENT_MODE = ".//div[@class='row no-margin col-container']/div[2]/div[1]/div[2]/span[1]"

		private static final def FROM_DATE = ".//div[@class='mb_10'][1]//input"

		private static final def TO_DATE = ".//div[@class='mb_10'][2]//input"

		private static final def BY_TRANSACTIONS = ".//select[@name='transactionType']"

		private static final def BY_STATUS = ".//select[@name='status']"

		private static final def TRANSACTION_IDS = ".//div[@class='col-xs-10 pl_5 pr_0 transaction-details']/span[2]"

		private static final def RESET = ".//a[contains(text(),'Reset')]"

//		private static final def TRANSACTION_STATUS = ".//div[@class='hidden-xs hidden-sm col-md-3 pt_10 pb_10 pr_10 pl_10']/span[1]"
		
		private static final def TRANSACTION_STATUS = ".//*[.='Txn ID txID']/../../../div[4]/span[1]"

		private static final def AMOUNT_PARAMETER = ".//div[@class='hidden-xs hidden-sm row pt_10 pb_10 border-bottom-light']/div[3]"

		private static final def PAGELIST = ".//ul[@class='pagination pull-right']/li/a"

		private static final def FIELDS = [FROM_DATE, TO_DATE, BY_TRANSACTIONS, BY_STATUS]

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"

		//		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR]

		//error message map (Key-Value Pair)
		def static final transactionPageErrorMessageMap = [
			invalid_size:"Please upload a file smaller than 10 MB"
		]

		//To verify the newly created fund transaction
		def static final newTransaction = {browser, formData ->
			def Amount = (browser.gettext(AMOUNT)).split(" ")[1].replace(",","");
			if(Amount.equals(O3CommonPage.transactionAmount)){
				O3CommonPage.transactionId = browser.gettext(TRANSACTION_ID).split("ID")[1].trim();
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "TransactionDisplayIssue", "Newly created transaction is not available");
			}
		}

		//To verify the added fund in Capital and Net Fund amount on dashboard
		def static final updatedFund = {browser,formData ->
			def capital = (browser.gettext(CAPITAL)).split(" ")[1];
			def netFund = (browser.gettext(NET_FUND)).split(" ")[1];

			if((capital.equals(O3CommonPage.transactionAmount)) && (netFund.equals(O3CommonPage.transactionAmount))){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "CapitalAndNetFundDisplayIssue", "Newly created transaction is not updated");
			}
		}

		//To verify the recent transaction on dashboard
		def static final recentTransactions = {browser,formData ->
			def Amount = (browser.gettext(RECENT_TRANS_AMOUNT)).split(" ")[1].replace(",","");
			if(Amount.equals(O3CommonPage.transactionAmount)){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "RecentTransactionDisplayIssue", "Recent transaction is not available");
			}
		}

		//To append the new amount in the current amount
		def static final updateCashNCapital = {browser,formData ->
			if(O3CommonPage.newCapital!=null && O3CommonPage.newCash!=null){
				O3CommonPage.newCapital = Integer.parseInt(O3CommonPage.newCapital);
				O3CommonPage.newCash = Integer.parseInt(O3CommonPage.newCash);
				if(browser.gettext(PAYMENT_MODE).equalsIgnoreCase("Cheque deposit")){
					O3CommonPage.newCapital = (O3CommonPage.newCapital + Integer.parseInt(O3CommonPage.transactionAmount)).toString();
					O3CommonPage.newCash = (O3CommonPage.newCash + Integer.parseInt(O3CommonPage.transactionAmount)).toString();
				}else if(browser.gettext(PAYMENT_MODE).equalsIgnoreCase("Online withdrawal")){
					O3CommonPage.newCapital = (O3CommonPage.newCapital - Integer.parseInt(O3CommonPage.transactionAmount)).toString();
					O3CommonPage.newCash = (O3CommonPage.newCash - Integer.parseInt(O3CommonPage.transactionAmount)).toString();
				}
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "UpdateAmountIssue", "Capital And Cash amount not updated successfully");
			}

		}

		//To update status of fund request
		def static final updateStatus = {browser, formData ->
			if((O3CommonPage.mobileNumber)!= null){
				String query1 = "update user_payment_details set status = "+formData[0]+" where transactionId = '"+O3CommonPage.transactionId+"';"
				println "query1: "+ query1
				def updateStatus = DBConnect.returnQuery(query1)
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DatabaseIssue", "update status query execution failed");
			}
		}

		//To populate data in TRansaction page
		def static final populateFields = { browser, formData ->
			browser.delay(3000)
			println ("TransactionForm.populateFields - data: " + formData)

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dateWithoutTime = sdf.format(date)

			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				for(int i=0;i<FIELDS.size();i++){
					if((FIELDS[i].equals(FROM_DATE)) || (FIELDS[i].equals(TO_DATE))){
						if(formData[i].equals("")){
							formData[i] = dateWithoutTime
						}
					}else if(FIELDS[i].equals(BY_STATUS)){
						O3CommonPage.status = formData[i]
						println " O3CommonPage.status:: "+O3CommonPage.status
					}

				}
				outcome = WebForm.enterData(browser, formData, FIELDS, RESET, WAIT_REQ_FIELDS)
			}
			return outcome
		}

		/*//To filter the transactions in Transaction page
		def static final filterTransactions = {browser, formData ->
			browser.delay(2000)
			if((browser.gettext(TRANSACTION_STATUS)).equalsIgnoreCase(O3CommonPage.status)){
				println "success"
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "TransactionFilterIssue", "Filtered transaction ID status is not available in the transactions list");
			}
		}*/
		
		
		//To filter the transactions in Transaction page
		def static final filterTransactions = {browser, formData ->
		browser.delay(2000)
		String transactionStatus = TRANSACTION_STATUS.replace("txID",O3CommonPage.transactionId)
		if((browser.gettext(transactionStatus)).equalsIgnoreCase(O3CommonPage.status)){
		println "success"
		return new SuccessOutcome()
		}else{
		return O3CommonPage.returnFailureOutcome(browser, "TransactionFilterIssue", "Filtered transaction ID status is not available in the transactions list");
		}
		}
		

		//To sort transactions based on amount
		def static final amountBasedSort = {browser, formData ->
			def amountList = []
			browser.delay(1000)
			if(browser.isDisplayed(AMOUNT_PARAMETER)){
				browser.click(AMOUNT_PARAMETER)

				amountList = browser.getListElements(AMOUNT_LIST)
				for(int i=0;i<amountList.size();i++){
					def firstAmount = Integer.parseInt((browser.getTexts(amountList[i])).split(" ")[1].replace(",",""))
					def nextAmount = Integer.parseInt((browser.getTexts(amountList[i+1])).split(" ")[1].replace(",",""))

					if(firstAmount >= nextAmount){
						return new SuccessOutcome()
					}else{
						return O3CommonPage.returnFailureOutcome(browser, "AmountComparisonIssue", "Unable to compare amount of the transactions");
					}
					break;
				}
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "AmountBasedFilterIssue", "Filtered transaction ID based on amount filter is not available in the transactions list");
			}
		}

		///To verify reset button functionality
		def static final verifyReset = {browser, formData->
			for(int i=0;i<FIELDS.size();i++){
				if((FIELDS[i].equals(FROM_DATE)) || (FIELDS[i].equals(TO_DATE))){
					if((browser.gettext(FIELDS[i],"value")).equals("")){
						return new SuccessOutcome()
					}else{
						return O3CommonPage.returnFailureOutcome(browser, "VerifyResetIssue", "Filter reset got failed");
					}
					return new SuccessOutcome()
				}else{
					return O3CommonPage.returnFailureOutcome(browser, "FieldComparisonIssue", "Unable to find fields");
				}
			}
		}

		//To verify pagination
		def static final pagination = {browser, formData ->
			def txnIDText
			def transactionIdList
			def pageList = browser.getListElements(PAGELIST)
			def pageListText = browser.getLists(PAGELIST)
			for(int i=1;i<pageList.size()-1;i++){
				browser.scrollToElement(pageList.get(i))
				def pageNumber = browser.getTexts(pageList[i])
				transactionIdList = browser.getListElements(TRANSACTION_IDS)
				txnIDText = browser.getLists(TRANSACTION_IDS)
				browser.clickElement(pageList[i+1])
			}
			return new SuccessOutcome()
		}
	}
}
