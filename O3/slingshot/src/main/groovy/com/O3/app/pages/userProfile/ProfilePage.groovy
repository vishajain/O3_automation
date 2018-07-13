package com.O3.app.pages.userProfile

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

/**
 * Created by Sandhya on 24/9/14
 */

final class ProfilePage extends WebPage {
	
	//To verify that the personal details displayed in the profile matches the entered values
	def static personalDetailsDisplayed = { browser, formData ->
		new ProfileForm().personalDetailsDisplayed browser, formData
	}
	
	def static getPersonalDetailsInProfile = {browser, formData ->
		new ProfileForm().getPersonalDetailsInProfile browser, formData
	}
	
	//To verify the bank details displayed in the profile page matches the entered values
	def static bankDetailsDisplayed = { browser, formData ->
		new ProfileForm().bankDetailsDisplayed browser, formData
	}
	
	def static final getBankDetailsInProfile = {browser, formData ->
		new ProfileForm().getBankDetailsInProfile browser, formData
	}
	
	static final class ProfileForm extends WebForm {

		//Profile form elements
		private static final def USER_NAME = "//div[@class='pull-left full-width fs_14'][.='Name']/../div[2]" 

		private static final def PHONE = "//div[@class='pull-left full-width fs_14'][.='Phone']/../div[2]"  		

		private static final def PAN = "//div[@class='pull-left full-width fs_14 uppercase'][.='Pan']/../div[2]"
		
		private static final def DATE_OF_BIRTH = "//div[@class='pull-left full-width fs_14'][.='Date of birth']/../div[2]"
		
		private static final def PERSONAL_DETAILS_TITLE = ".//h3[@class='pull-left ml_5 document-type-header mt_0 mb_0'][.='Personal details']"
		
		private static final def GENDER = "//div[@class='pull-left full-width fs_14'][.='Gender']/../div[2]"

		private static final def MARITAL_STATUS = "//div[@class='pull-left full-width fs_14'][.='Marital status']/../div[2]"
		
		private static final def FATHERS_NAME = "//div[@class='pull-left full-width fs_14'][contains(text(),'Father')]/../div[2]"
		
		private static final def MOTHERS_NAME = "//div[@class='pull-left full-width fs_14'][contains(text(),'Mother')]/../div[2]"
		
		private static final def ANNUAL_INCOME = "//div[@class='pull-left full-width fs_14'][contains(text(),'Annual income')]/../div[2]"
				
		private static final def OCCUPATION = "//div[@class='pull-left full-width fs_14'][.='Occupation']/../div[2]"
		
		//Fields for personal details
		private static final def FIELDS = [USER_NAME, PHONE, PAN, DATE_OF_BIRTH, GENDER, MARITAL_STATUS, OCCUPATION, FATHERS_NAME, MOTHERS_NAME, ANNUAL_INCOME]
		
		//Fields for bank details
		private static final def BANK_DETAILS_TITLE = ".//h3[@class='pull-left ml_5 document-type-header mt_0 mb_0'][.='Bank Details']"
		
		private static final def ACCOUNT_NUMBER = "//div[@class='pull-left full-width fs_14'][.='Account number']/../div[2]"
		
		private static final def IFSC = "//div[@class='pull-left full-width fs_14'][.='IFSC code']/../div[2]"
		
		//Fields for personal details
		private static final def FIELDS2 = [ACCOUNT_NUMBER, IFSC]
		
		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"
		
		private static final def FIELD_ERROR = ".//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]
		
		//error message map (Key-Value Pair)
		def static final ProfilePageErrorMessageMap = []
		
		//list of data for personal details in profile page
		def static final getPersonalDetailsInProfile = {browser, formData ->
			def profilePersonalDetails = []
			for(int i = 0; i < FIELDS.size(); i++){
				profilePersonalDetails.add(browser.gettext(FIELDS[i]))
			}
			return profilePersonalDetails
		}
		
		//list of data for bank details in profile page
		def static final getBankDetailsInProfile = {browser, formData ->
			def profileBankDetails = []
			for(int i = 0; i < FIELDS2.size(); i++){
				profileBankDetails.add(browser.gettext(FIELDS2[i]))
			}
			return profileBankDetails
		}
		
		//To verify that the personal details displayed in the profile matches the entered values
		def static final personalDetailsDisplayed = {browser, formData ->
			if(browser.isDisplayed(PERSONAL_DETAILS_TITLE)){
				def personalDetailsStoredList = []
				//for basic details
				for(int i = 0; i < O3CommonPage.basicDetailsList.size(); i++){
					if(i == 0){
						def fullName = O3CommonPage.basicDetailsList[0]+" "+O3CommonPage.basicDetailsList[1]+" "+O3CommonPage.basicDetailsList[2]
						personalDetailsStoredList.add(fullName)
					}
					if(i == 3){
						def gender = O3CommonPage.basicDetailsList[i]
						personalDetailsStoredList.add(gender)
					}
					if(i == 4){
						def maritalStatus = O3CommonPage.basicDetailsList[i]
						personalDetailsStoredList.add(maritalStatus)
					}
				}
				//for parents details
				for(int j = 0; j < O3CommonPage.parentDetailsList.size(); j++){
					if(j == 0){
						def fathersName = O3CommonPage.parentDetailsList[0]+ " " +O3CommonPage.parentDetailsList[1]
						personalDetailsStoredList.add(fathersName)
					}
					if(j == 2){
						def mothersName = O3CommonPage.parentDetailsList[2]+ " " +O3CommonPage.parentDetailsList[3]
						personalDetailsStoredList.add(mothersName)
					}
				}
				personalDetailsStoredList.add(O3CommonPage.pan)
				personalDetailsStoredList.add(O3CommonPage.DOB)
				personalDetailsStoredList.add(O3CommonPage.mobileNumber)
				//for address details
				for(int k = 0; k < O3CommonPage.personalDetailsList.size(); k++){
					/*if(k == 0){
						def fullAddress = O3CommonPage.personalDetailsList[0] + " " +O3CommonPage.personalDetailsList[1] +
						" " + O3CommonPage.personalDetailsList[2] + " " + O3CommonPage.personalDetailsList[3] + "-"+ O3CommonPage.personalDetailsList[4]
						println "fullAddress : "+fullAddress
						personalDetailsStoredList.add(fullAddress)
					}*/
					if(k == 5){
						def annualIncome = O3CommonPage.personalDetailsList[5]
						personalDetailsStoredList.add(annualIncome)
					}
					if(k == 6){
						def occupation = O3CommonPage.personalDetailsList[6]
						personalDetailsStoredList.add(occupation)
					}
				}
				def newpersonalDetailsStoredList = personalDetailsStoredList.collect{ it.toLowerCase() }
				
				def displayedPersonalDetailList = ProfilePage.getPersonalDetailsInProfile(browser, formData)
				def newdisplayedPersonalDetailList = displayedPersonalDetailList.collect{ it.toLowerCase() }
				
				println "newdisplayedPersonalDetailList : "+newdisplayedPersonalDetailList+ "\nnewpersonalDetailsStoredListt : "+newpersonalDetailsStoredList
				if(newdisplayedPersonalDetailList?.sort(false) == newpersonalDetailsStoredList?.sort(false)){
					return new SuccessOutcome()
				}else{
					return O3CommonPage.returnFailureOutcome(browser, "DataMisMatchIssue", "Both the details does not match.")
				}
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "PersonalDetailsDisplayIssue", "Personal details is not displayed.")
			}
		}
		
		//To verify the bank details displayed in the profile page matches the entered values
		def static final bankDetailsDisplayed = { browser, formData ->
			if(browser.isDisplayed(BANK_DETAILS_TITLE)){
				def bankDetailsStoredList = []
				for(int i = 0; i < O3CommonPage.bankDetailsList.size(); i++){
					if(i == 0){
						def ifsc = O3CommonPage.bankDetailsList[0]
						bankDetailsStoredList.add(ifsc)
					}
					if(i == 1){
						def accountNumber = O3CommonPage.bankDetailsList[1]
						bankDetailsStoredList.add(accountNumber)
					}
				}
				def newBankDetailsStoredList = bankDetailsStoredList.collect{ it.toLowerCase() }

				def displayedBankDetails = ProfilePage.getBankDetailsInProfile(browser, formData)
				def newdisplayedBankDetails = displayedBankDetails.collect{ it.toLowerCase() }

				println "newBankDetailsStoredList : "+newBankDetailsStoredList+ "\nnewdisplayedBankDetails : "+newdisplayedBankDetails
				if(newBankDetailsStoredList?.sort(false) == newdisplayedBankDetails?.sort(false)){
					return new SuccessOutcome()
				}else{
					return O3CommonPage.returnFailureOutcome(browser, "DataMisMatchIssue", "Both the details does not match.")
				}
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "BankDetailsDisplayIssue", "Bank details is not displayed.")
			}
		}
	}	
}
