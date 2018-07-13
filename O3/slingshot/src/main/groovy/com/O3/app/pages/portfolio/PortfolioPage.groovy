package com.O3.app.pages.portfolio

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

final class PortfolioPage extends WebPage {

	//Override	
	def populateData = {browser, formKey, formData ->
		new PortfolioForm().populateFields(browser, formData);
	}	
	
	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in PortfolioPage")
		new PortfolioForm().submit(browser, formData)
	}	
	
	/*//To logout from the application
	def static logout = { browser, formData ->
		new PortfolioForm().logout browser, formData
	}*/
	
	static final class PortfolioForm extends WebForm {

		//Portfolio form elements
		private static final def AMOUNT_TO_INVEST = "html/body/div[2]/div/div[2]/div/div/div[2]/div[2]/div[1]/input"
		
		private static final def INVEST = "//button[@class='btn btn-primary btn-md-custom block-custom'][.='Invest']"
		
		private static final def MINIMUM_AMOUNT = ".//*[@id='root']//div[1]/div/div/div[2]/div[2]/div[3]/a/../../div[1]/div[2]/span"
		
		private static final def PORTFOLIO_NAME = ".//*[@id='root']//div[1]/div/div/div[2]/div[2]/div[3]/a/../../../div[1]/div/a"
		
		private static final def FIELDS = [AMOUNT_TO_INVEST]
		
		private static final def INVEST_BUTTON = ".//*[@id='root']/div/div/div[1]/div/div/div/div[2]/div[2]/div/div[1]/div/div/div[2]/div[2]/div[3]/a"	
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		
		//error message map (Key-Value Pair)
		def static final PortfolioPageErrorMessageMap = [
			min_amount_req:"Minimum Amount to invest is rs"]

		//To enter data
		def static final populateFields = { browser, formData ->			
			println ("PortfolioFormm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				if(browser.isDisplayed(PORTFOLIO_NAME)){
				O3CommonPage.portfolioName = browser.gettext(PORTFOLIO_NAME)
				browser.click INVEST_BUTTON
				browser.delay(3000)
				O3CommonPage.minAmount = browser.gettext(MINIMUM_AMOUNT).split(" ")[1].replace(",","")
				PortfolioPageErrorMessageMap.replace("min_amount_req", "Minimum Amount to invest is "+O3CommonPage.minAmount)
				for(int i = 0; i < FIELDS.size(); i++){
					if(FIELDS[i].equals(AMOUNT_TO_INVEST)){
						if(Integer.parseInt(O3CommonPage.newCash) > Integer.parseInt(O3CommonPage.minAmount)){
							println "cash : "+O3CommonPage.newCash
							formData[i] = O3CommonPage.newCash.toString()
						}else{
							println "minAmount : "+O3CommonPage.minAmount
							formData[i] = O3CommonPage.minAmount
							O3CommonPage.amountToInvest = formData[i]
						}
					}
				}
				outcome = WebForm.enterData(browser, formData, FIELDS, INVEST, WAIT_REQ_FIELDS)
				}
			} 
			return outcome
		}
		
		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {			
			def actualValidationMsg = submitForm browser, FIELDS, INVEST, data, ERROR_MESSAGE_FIELDS	
			browser.delay(3000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, PortfolioPageErrorMessageMap)			
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}
		
		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			//Get the error messages
			List<String> actualMsg = []
			browser.delay(3000)
			if(browser.isAlertPresent()){
				actualMsg = browser.alertMsg()
			}else{
				actualMsg = []
			}
			actualMsg
		}
	
	}	
}
