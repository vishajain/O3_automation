package com.O3.app.pages

import groovy.transform.ToString;

import com.O3.tools.Browser
import com.O3.app.pages.O3CommonPage

class PageDefs {
	
	private static List<PageDefEntry> pageDefEntries = new ArrayList();
	static {
		// Define the Pages available...  (page-key, page-name, page-className)		
		//User	
		pageDefEntries.add( new PageDefEntry("registration", "Sign Up", "com.O3.app.pages.user.SignUpPage"))
		pageDefEntries.add( new PageDefEntry("home", "", null))
		pageDefEntries.add( new PageDefEntry("login", "Login", "com.O3.app.pages.user.LoginPage"))
		pageDefEntries.add( new PageDefEntry("generateOtp", "OTP", "com.O3.app.pages.user.GenerateOTPPage"))
		pageDefEntries.add( new PageDefEntry("panDetails", "PAN", "com.O3.app.pages.user.PanPage"))
		pageDefEntries.add( new PageDefEntry("resetPassword", "Reset Password", "com.O3.app.pages.user.ResetPasswordPage"))
		pageDefEntries.add( new PageDefEntry("setNewPassword", "Set New Password", "com.O3.app.pages.user.SetNewPasswordPage"))
		pageDefEntries.add( new PageDefEntry("IPV", "IPV", "com.O3.app.pages.user.IPVPage"))
		pageDefEntries.add( new PageDefEntry("aadhaarInfo", "Aadhaar Verification", "com.O3.app.pages.user.AadhaarPage"))
		pageDefEntries.add( new PageDefEntry("userDetails", "User Details", "com.O3.app.pages.user.UserDetailsPage"))
		pageDefEntries.add( new PageDefEntry("uploadDocs", "Upload Docs", "com.O3.app.pages.user.UploadDocumentsPage"))
		pageDefEntries.add( new PageDefEntry("eSign", "Esign Page", null))
		pageDefEntries.add( new PageDefEntry("documentVerification", "Document Verification", null))
		pageDefEntries.add( new PageDefEntry("dashboard", "Dashboard", "com.O3.app.pages.user.DashboardPage"))
		pageDefEntries.add( new PageDefEntry("userProfile", "Profile", "com.O3.app.pages.userProfile.ProfilePage"))
		pageDefEntries.add( new PageDefEntry("portfolio", "Portfolio list", "com.O3.app.pages.portfolio.PortfolioPage"))
		pageDefEntries.add( new PageDefEntry("confirmRequest", "Confirm Request Page", null))
		
		//Add funds
		pageDefEntries.add( new PageDefEntry("payByCheque", "Pay By Cheque", "com.O3.app.pages.funds.AddFundPage"))
		pageDefEntries.add( new PageDefEntry("payOnline", "Pay Online", "com.O3.app.pages.funds.AddFundPage"))
		pageDefEntries.add( new PageDefEntry("transactions", "Transactions", "com.O3.app.pages.funds.TransactionPage"))
		pageDefEntries.add( new PageDefEntry("redeemFunds", "Redeem Funds", "com.O3.app.pages.funds.RedeemFundPage"))
	
		//Questionnaire
		pageDefEntries.add( new PageDefEntry("startQuestionnaire", "Start risk assessment", null))
		pageDefEntries.add( new PageDefEntry("answerQuestionnaire", "Answer Questionnaire", null))
		pageDefEntries.add( new PageDefEntry("successQuestionnaire", "Success Questionnaire", null))
	}
	
	//Get Key for the current page
	public static getCurrentPageKey(def browser){
		def pageName
		
		pageName = browser.getTitle()		
		
		//Esign page
		if(browser.getElement(Browser.XPATH, ".//*[contains(@id,'parentdigio-ifm')]/span/span/a") != null){
			println "pageName : "+pageName
			if(browser.isDisplayed(".//*[contains(@id,'parentdigio-ifm')]/span/span/a")){
				pageName = "Esign Page"
			}
		}
		
		//confirm request page
		if(browser.getElement(Browser.XPATH, ".//div[@class='modal-header']/b") != null){
			println "pageName : "+pageName
			if(browser.isDisplayed(".//div[@class='modal-header']/b")){
				pageName = "Confirm Request Page"
			}
		}
		
		//Set New Password
		if(browser.getElement(Browser.XPATH, ".//button[@type='submit'][.='Set Password']") != null){
			if(browser.isDisplayed(".//button[@type='submit'][.='Set Password']")){
				pageName = "Set New Password"
			}
		}
		
		//Pay By Cheque
		if(browser.getElement(Browser.XPATH, ".//div[@class='col-md-12']/span[contains(text(),'Pay by cheque')]") != null){
			if(browser.isDisplayed(".//div[@class='col-md-12']/span[contains(text(),'Pay by cheque')]")){
				pageName = "Pay By Cheque"
			}
		}

		//Pay Online
		if(browser.getElement(Browser.XPATH, ".//div[@class='col-md-12']/span[contains(text(),'Pay online')]") != null){
			if(browser.isDisplayed(".//div[@class='col-md-12']/span[contains(text(),'Pay online')]")){
				println "pageName : "+pageName
				pageName = "Pay Online"
			}
		}

		//Redeem Fund
		if(browser.getElement(Browser.XPATH, ".//div[@class='col-md-12']/span[contains(text(),'Redeem funds')]") != null){
			if(browser.isDisplayed(".//div[@class='col-md-12']/span[contains(text(),'Redeem funds')]")){
				pageName = "Redeem Funds"
			}
		}
		
		//Start Questionnaire
		if(browser.getElement(Browser.XPATH, ".//div[@class='btn btn-primary btn-block'][contains(text(),'Start the risk assessment')]") != null){
			if(browser.isDisplayed(".//div[@class='btn btn-primary btn-block'][contains(text(),'Start the risk assessment')]")){
				pageName = "Start risk assessment"
			}
		}
		
		//Answer Questionnaire
		if(browser.getElement(Browser.XPATH, ".//div[@class='btn btn-primary btn-block'][contains(text(),'Start the risk assessment')]") != null){
			if(browser.isDisplayed(".//div[@class='btn btn-primary btn-block'][contains(text(),'Start the risk assessment')]")){
				pageName = "Answer Questionnaire"
			}
		}
		
		//Success Questionnaire
		if(browser.getElement(Browser.XPATH, ".//div[@class='btn btn-primary btn-block'][contains(text(),'Go to PAN verification')]") != null){
			if(browser.isDisplayed(".//div[@class='btn btn-primary btn-block'][contains(text(),'Go to PAN verification')]")){
				pageName = "Success Questionnaire"
			}
		}
		
		println "Page Name :" +pageName
		return findKeyByPageName(pageName.trim())
	}	
	
	
	/* Get PageDefEntry object for specified key */
	public static PageDefEntry getPageDefEntry (def pageKey) {
		for(PageDefEntry pageDefEntry : pageDefEntries) {
			if(pageDefEntry.key.equalsIgnoreCase(pageKey))
				return pageDefEntry;
		}
		return null;
	}
	
	private static findKeyByPageName(String pageName){		
		for(PageDefEntry pageDefEntry : pageDefEntries) {
			if(pageDefEntry.name.equalsIgnoreCase(pageName))
				return pageDefEntry.key;
		}
		return null;		
	}
}
