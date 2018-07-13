package com.O3.app.dsl

import com.O3.model.SuccessOutcome
import com.O3.model.FailureOutcome
import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage


/* Class to take care of operations related to all click actions */
class ClickAction {

	private static Map<String, String> xPathMapping;

	private static final def LINK = "linkname"

	static {

		xPathMapping = new HashMap<String, String>();

		//List of buttons/links with its XPath in the path

		//Onboarding
		xPathMapping.put("login", "//a[.='Sign In']");
		xPathMapping.put("backButton", "//a[.='Back']");
		xPathMapping.put("resendOTP", ".//small[.='Resend OTP']");
		xPathMapping.put("resetPassword", "//a[.='Forgot password?']");
		xPathMapping.put("signUp", ".//*[@id='bs-example-navbar-collapse-1']/div/a[2]");
		xPathMapping.put("cropNSave", "//button[.=' Crop & Save']");
		xPathMapping.put("dontHaveAadhaar", ".//a[@href='/aadhaardetails']");
		xPathMapping.put("nextButton", ".//button[.='Next']");
		xPathMapping.put("parentsTab", ".//div[@class='pos-r'][.='Parents ']");
		xPathMapping.put("bankTab", "//div[@class='pos-r'][.='Bank ']");
		xPathMapping.put("stepOver", ".//a[@class='link-primary pointer']");
		xPathMapping.put("submitButton", ".//button[@class='btn btn-primary pull-right']");
		xPathMapping.put("allDone", ".//button[.='All done!']");
		xPathMapping.put("continueESign", ".//a[contains(text(),'Continue eSign')]");
		xPathMapping.put("closeDigioWindow", ".//*[contains(@id,'parentdigio-ifm')]/img");
		xPathMapping.put("userOption", ".//li[@class='dropdown']/a");
		xPathMapping.put("profile", ".//*[@id='bs-example-navbar-collapse-1']/div/ul/li/ul/li[1]/a");
		xPathMapping.put("bankDetails", ".//div[@class='pos-r'][.='Bank details ']");

		//portfolio
		xPathMapping.put("portfolio", ".//*[@id='bs-example-navbar-collapse-1']/div/ul/li/ul/li[3]/a");
		xPathMapping.put("backToDashboard", ".//span[contains(text(),'Back to dashboard')]");
		xPathMapping.put("closeInvestPopUp", "html/body/div[2]/div/div[2]/div/div/div[1]/button");

		//Add funds
		xPathMapping.put("addFund", ".//div[@class='hidden-xs hidden-sm portfolio-container document pull-right width-28 m_full_width']//div[2]/a");
		xPathMapping.put("payOnline", ".//a[contains(text(),'Pay online?')]");
		xPathMapping.put("payByCheque", ".//a[contains(text(),'Pay by cheque?')]");
		xPathMapping.put("cross", ".//button[@class='close']");
		xPathMapping.put("transactions", ".//a[contains(text(),'Transactions')]");
		xPathMapping.put("redeemFund", ".//*[@class='hidden-xs hidden-sm portfolio-container document pull-right width-28 m_full_width']//div[3]/a");

		//Transactions
		xPathMapping.put("reset", ".//a[contains(text(),'Reset')]");

		//Upload Documents
		xPathMapping.put("saveDocuments", ".//button[contains(text(),'Save')]");
		xPathMapping.put("skipESign", ".//label[@class='checkbox fw_normal']/span");

		//document verification
		xPathMapping.put("continue", ".//a[.='Continue']");
		xPathMapping.put("cancel", ".//a[.='Cancel']");
	}

	// Main function to take care of click actions
	def static performClick (def browser, def clickKey) {

		println ("Performing Click for key: " + clickKey);
		if(clickKey ==null) return;

		def xpath = xPathMapping.get(clickKey);
		println ("xpath for click is " + xpath);

		if(browser.getElement(Browser.XPATH, xpath) == null || !browser.checkEnabled(xpath)){
			def message = clickKey + "  not found/NotEnabled"
			println "Message   :" +message
			def fileName = clickKey+"Issue"
			return O3CommonPage.returnFailureOutcome(browser, fileName, message)
		}else{
			waitBeforeClick(browser, xpath)
			//				browser.scrollToElement(browser.getElement(Browser.XPATH, xpath))
			browser.click xpath
			waitAfterClick(browser, xpath)
			return new SuccessOutcome();
		}
	}


	//Delay before click
	def static waitBeforeClick(def browser, def xpath){
		browser.delay(2000)

//		browser.fluentWait(".//*[@id='root']/div/div/div[2]/div/div[3]/div/form/div/div[1]/small",5);
		browser.fluentWait(xpath,90);
		//browser.fluentWait(".//*[contains(@id,'parentdigio-ifm')]/img",8);
		//browser.fluentWait(".//button[contains(text(),'Save')]",2);
		

		/*if(xpath.equals(".//*[@id='root']/div/div/div[2]/div/div[3]/div/form/div/div[1]/small")){
		 browser.delay(4000)
		 }*/
		/* if(xpath.equals(".//*[@id='bs-example-navbar-collapse-1']/div/a[2]")){
		 browser.delay(2000)
		 }
		 if(xpath.equals(".//*[contains(@id,'parentdigio-ifm')]/img")){
		 browser.delay(8000)
		 }
		 if(xpath.equals(".//button[contains(text(),'Save')]")){
		 browser.delay(2000)
		 }*/
	}

	//Delay after click
	def static waitAfterClick(def browser, def xpath){
		browser.delay(3000)

		browser.fluentWait(xpath, 90);
		/*browser.fluentWait(".//*[contains(@id,'parentdigio-ifm')]/img",10);
		browser.fluentWait(".//a[contains(text(),'Pay by cheque?')]",5);
		browser.fluentWait(".//a[contains(text(),'Transactions')]",10);
		browser.fluentWait(".//div[@class='hidden-xs hidden-sm portfolio-container document pull-right width-28 m_full_width']//div[2]/a",2,5);
		browser.fluentWait(".//*[@class='hidden-xs hidden-sm portfolio-container document pull-right width-28 m_full_width']//div[3]/a",3,5);
		browser.fluentWait(".//button[contains(text(),'Save')]",5);
		browser.fluentWait(".//a[.='Continue']",4);
		browser.fluentWait(".//button[.='All done!']",12);
*/

		/*if(xpath.equals("//button[.=' Crop & Save']")){2
		 browser.delay(25000)
		 }
		 if(xpath.equals(".//*[contains(@id,'parentdigio-ifm')]/img")){
		 browser.delay(10000)
		 }
		 if(xpath.equals(".//a[contains(text(),'Pay by cheque?')]")){
		 browser.delay(2000)
		 }
		 if(xpath.equals(".//a[contains(text(),'Transactions')]")){
		 browser.delay(10000)
		 }
		 if(xpath.equals(".//div[@class='hidden-xs hidden-sm portfolio-container document pull-right width-28 m_full_width']//div[2]/a")){
		 browser.delay(2000)
		 }
		 if(xpath.equals(".//*[@class='hidden-xs hidden-sm portfolio-container document pull-right width-28 m_full_width']//div[3]/a")){
		 browser.delay(3000)
		 }
		 if(xpath.equals(".//button[contains(text(),'Save')]")){
		 browser.delay(5000)
		 }
		 if(xpath.equals(".//a[.='Continue']")){
		 browser.delay(9000)
		 }
		 if(xpath.equals(".//button[.='All done!']")){
		 browser.delay(15000)
		 }*/
		 }
	}
