package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

final class UserDetailsPage extends WebPage {

	//Override
	def populateData = {browser, formKey, formData ->
		if(formKey.equalsIgnoreCase("basicDetails")) {
			new BasicDetailsForm().populateFields(browser, formData)
		} else if(formKey.equalsIgnoreCase("parentsDetails")) {
			new ParentsDetailsForm().populateFields(browser, formData)
		}else if(formKey.equalsIgnoreCase("bankDetails")){
			new BankDetailsForm().populateFields(browser, formData)
		}else if(formKey.equalsIgnoreCase("personalDetails")){
			new PersonalDetailsForm().populateFields(browser, formData)
		}
	}

	//Override
	def submit = {browser, formKey, formData ->
		println ("Submit method in UserDetailsPage")
		if(formKey.equalsIgnoreCase("basicDetails")) {
			println "in basic details form"
			new BasicDetailsForm().submit(browser, formData)
		} else if(formKey.equalsIgnoreCase("parentsDetails")) {
			new ParentsDetailsForm().submit(browser, formData)
		}else if(formKey.equalsIgnoreCase("bankDetails")){
			new BankDetailsForm().submit(browser, formData)
		}else if(formKey.equalsIgnoreCase("personalDetails")){
			new PersonalDetailsForm().submit(browser, formData)
		}
	}

	//For verifying  ther users mobile number  matches the entered values
	def static verifyDOBPhone = { browser, formData ->
		new BasicDetailsForm().verifyDOBPhone(browser, formData)
	}

	//For verify saved user basic details are displayed after loggin in again
	def static basicDetailsDisplayed = { browser, formData ->
		new BasicDetailsForm().basicDetailsDisplayed(browser, formData)
	}

	//To verify the parents details match after saving
	def static parentsDetailsDisplayed = {browser, formData ->
		new ParentsDetailsForm().parentsDetailsDisplayed(browser, formData);
	}

	//To verify the bank details match after saving
	def static bankDetailsDisplayed = {browser, formData ->
		new BankDetailsForm().bankDetailsDisplayed(browser, formData);
	}

	//To verify the personal details match after saving
	def static personalDetailsDisplayed = {browser, formData ->
		new PersonalDetailsForm().personalDetailsDisplayed(browser, formData);
	}

	static final class BasicDetailsForm extends WebForm {

		//Login form elements
		private static final def TITLE = ".//select[@name='title']"

		private static final def FULL_NAME = ".//input[@id='kraName']"

		//		private static final def LAST_NAME = ".//*[@id='lastName']"

		private static final def EMAIL = ".//*[@id='email']"

		private static final def MOBILE_NUMBER = ".//*[@id='mobileNumber']"

		private static final def GENDER = ".//select[@name='gender']"

		private static final def MARITAL_STATUS = ".//select[@name='maritalStatus']"

		private static final def DOB = ".//*[@class='rw-widget-input rw-input']"

		private static final def SAVE_FORM = ".//button[.='Save form']"

		private static final def NEXT = ".//button[.='Next']"

		private static final def FIELDS = [TITLE, FULL_NAME, GENDER, MARITAL_STATUS]

		private static final def FIELDS2 = []

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger']"

		private static final def FIELD_ERROR_1 = ".//div[@class='pos-r input-blur']/small[contains(@class, 'error')]"

		private static final def FIELD_ERROR_2 = ".//small[@class='error dropdown-error display-block pull-left']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR_1, FIELD_ERROR_2]

		//error message map (Key-Value Pair)
		def static final userDetailsPageErrorMessageMap = [
			field_req:"Please enter",
			fullName_req:"Please enter your full name"
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			println ("BasicDetailsForm.populateFields - data: " + formData)

			O3CommonPage.basicDetailsList = []
			def data = []
			for(int i = 0; i < FIELDS.size(); i++){
				if(FIELDS[i].equals(FULL_NAME)){
					O3CommonPage.basicDetailsList.add(browser.gettext(FULL_NAME, "value"))
				}else{
					O3CommonPage.basicDetailsList.add(formData[i])
					FIELDS2.add(FIELDS[i])
					data.add(formData[i])
				}
			}
			println "O3CommonPage.basicDetailsList : "+O3CommonPage.basicDetailsList
			println "basic details data: "+data
			println "FIELDS 2: "+FIELDS2

			def outcome = WebForm.checkFormFieldsData(data, FIELDS2)
			if(outcome.isSuccess()){
				outcome = WebForm.enterData(browser, data, FIELDS2, SAVE_FORM, WAIT_REQ_FIELDS)
			}
			return outcome
		}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, SAVE_FORM, data, ERROR_MESSAGE_FIELDS
			browser.delay(3000)
			println "actualValidationMsg "+actualValidationMsg
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, userDetailsPageErrorMessageMap)
			println "actualValidationMsgKeys "+actualValidationMsgKeys
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}

		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			browser.getValidationMessages errFields // get the validation messages from the current page.
		}

		def static final verifyDOBPhone = { browser, formData ->
			if(browser.isDisplayed(DOB) && browser.isDisplayed(MOBILE_NUMBER)){
				String dob = browser.gettext(DOB, "value")
				println "DOB : "+dob
				String mobileNumber = browser.gettext(MOBILE_NUMBER, "value")
				println "mobileNumb : "+mobileNumber
				if(O3CommonPage.mobileNumber.equalsIgnoreCase(mobileNumber) && O3CommonPage.DOB.equalsIgnoreCase(dob)){
					return new SuccessOutcome()
				}else{
					return O3CommonPage.returnFailureOutcome(browser, "MobileORDOBMatchIssue", "Mobile or DOB is not matching.")
				}
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "MobileDOBDisplayIssue", "Mobile or DOB is not displayed.")
			}
		}

		//For verify saved user basic details are displayed after loggin in again
		def static final basicDetailsDisplayed =  {browser, formData ->
			def fieldListValue = []
			browser.delay(5000)
			//println "O3CommonPage.basicDetailsList : "+O3CommonPage.basicDetailsList
			for(int i=0; i < FIELDS.size(); i++){
				if(FIELDS[i].equals(GENDER)){
					def valueAttribute = browser.gettext(FIELDS[i], "value")
					println "valueAttribute "+valueAttribute
					def path = ".//select[@name='gender']/option[@value='"+valueAttribute+"']"
					def text = browser.gettext(path)
					fieldListValue.add(text)
				}else{
					fieldListValue.add(browser.gettext(FIELDS[i], "value"))
				}
			}
			println "O3CommonPage.basicDetailsList : "+O3CommonPage.basicDetailsList+ "\nfieldListValue : "+fieldListValue
			if(O3CommonPage.basicDetailsList?.sort(false) == fieldListValue?.sort(false)){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DataMisMatchIssue", "Saved data is not matching with the prepopulated field data")
			}
		}
	}

	static final class ParentsDetailsForm extends WebForm {

		//Login form elements
		private static final def FATHERS_FIRST_NAME = ".//*[@id='fatherFirstName']"

		private static final def FATHERS_LAST_NAME = ".//*[@id='fatherLastName']"

		private static final def MOTHERS_FIRST_NAME = ".//*[@id='motherFirstName']"

		private static final def MOTHERS_LAST_NAME = ".//*[@id='motherLastName']"

		private static final def SAVE_FORM = ".//button[.='Save form']"

		private static final def FIELDS = [FATHERS_FIRST_NAME, FATHERS_LAST_NAME, MOTHERS_FIRST_NAME, MOTHERS_LAST_NAME]

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger']"

		private static final def FIELD_ERROR = ".//small[contains(@class, 'error')]"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]

		//error message map (Key-Value Pair)
		def static final parentsDetailsPageErrorMessageMap = [
			fathers_first_name_req:"Please enter your father's first name",
			fathers_last_name_req:"Please enter your father's last name",
			mothers_first_name_req:"Please enter your mother's first name",
			mothers_last_name_req:"Please enter your mother's last name",
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			println ("ParentsDetailsForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				O3CommonPage.parentDetailsList = []
				for(int i = 0; i < FIELDS.size(); i++){
					O3CommonPage.parentDetailsList.add(formData[i])
				}
				println "O3CommonPage.parentDetailsList : "+O3CommonPage.parentDetailsList
				outcome = WebForm.enterData(browser, formData, FIELDS, SAVE_FORM, WAIT_REQ_FIELDS)
			}
			return outcome
		}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, SAVE_FORM, data, ERROR_MESSAGE_FIELDS
			browser.delay(2000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, parentsDetailsPageErrorMessageMap)
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}

		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			browser.getValidationMessages errFields // get the validation messages from the current page.
		}

		//To verify the parents details match after saving
		def static final parentsDetailsDisplayed = {browser, formData ->
			def fieldListValue = []
			browser.delay(5000)
			for(int i=0; i < FIELDS.size(); i++){
				fieldListValue.add(browser.gettext(FIELDS[i], "value"))
			}
			println "O3CommonPage.parentDetailsList : "+O3CommonPage.parentDetailsList+ "\nfieldListValue : "+fieldListValue
			if(O3CommonPage.parentDetailsList?.sort(false) == fieldListValue?.sort(false)){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DataMisMatchIssue", "Saved parents data is not matching with the prepopulated field data")
			}
		}
	}

	static final class BankDetailsForm extends WebForm {

		//Login form elements
		private static final def IFSC = ".//*[@id='ifscCode']"

		private static final def ACCOUNT_NUMBER = ".//*[@id='accountNumber']"

		private static final def CONFIRM_ACCOUNT_NUMBER = ".//*[@id='confirmAccountNumber']"

		private static final def SAVE_FORM = ".//button[.='Save form']"

		private static final def FIELDS = [IFSC, ACCOUNT_NUMBER, CONFIRM_ACCOUNT_NUMBER]

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger']"

		private static final def FIELD_ERROR = "//small[@class='error']"

		private static final def MISMATCH_ERROR = "//span[@class='error lh_52']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR, MISMATCH_ERROR]

		//error message map (Key-Value Pair)
		def static final bankDetailsPageErrorMessageMap = [
			ifsc_req:"Please enter valid branch IFSC",
			account_req:"Please enter valid account number",
			conf_account_req:"Please re-enter valid account number",
			ifsc_invalid:"Invalid IFSC Code!",
			account_number_mismatch:"Account number doesn't match!"
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			println ("BankDetailsForm.populateFields - data: " + formData)
			def outcome = WebForm.checkFormFieldsData(formData, FIELDS)
			if(outcome.isSuccess()){
				O3CommonPage.bankDetailsList = []
				for(int i=0; i < FIELDS.size(); i++){
					O3CommonPage.bankDetailsList.add(formData[i])
				}
				println "O3CommonPage.bankDetailsList : "+O3CommonPage.bankDetailsList
				outcome = WebForm.enterData(browser, formData, FIELDS, SAVE_FORM, WAIT_REQ_FIELDS)

			}
			return outcome
		}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, SAVE_FORM, data, ERROR_MESSAGE_FIELDS
			browser.delay(2000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, bankDetailsPageErrorMessageMap)
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}

		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			browser.click submitButton
			browser.getValidationMessages errFields // get the validation messages from the current page.
		}

		//To verify the bank details match after saving
		def static final bankDetailsDisplayed = {browser, formData ->
			def fieldListValue = []
			browser.delay(5000)
			for(int i=0; i < FIELDS.size(); i++){
				fieldListValue.add(browser.gettext(FIELDS[i], "value"))
			}
			println "O3CommonPage.bankDetailsList : "+O3CommonPage.bankDetailsList+ "\nfieldListValue : "+fieldListValue
			if(O3CommonPage.bankDetailsList?.sort(false) == fieldListValue?.sort(false)){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DataMisMatchIssue", "Saved bank data is not matching with the prepopulated field data")
			}
		}
	}


	static final class PersonalDetailsForm extends WebForm {
		//Login form elements
		private static final def STREET_ADDRESS = ".//*[@id='streetAddress']"

		private static final def CITY = ".//*[@id='city']"

		private static final def STATE = ".//*[@id='state']"

		private static final def PINCODE = ".//*[@id='pincode']"

		private static final def ANNUAL_INCOME = ".//select[@name='annualIncome']"

		private static final def OCCUPATION = ".//select[@name='occupation']"

		private static final def POLITICAL_EXPOSED = ".//select[@name='isPoliticallyExposed']"

		private static final def TERMS = ".//div[@class='col-xs-12 col-md-12']"

		private static final def SUBMIT_FORM = ".//button[.=contains(text(),'Submit form')]"

		private static final def FIELDS = [STREET_ADDRESS, CITY, STATE, PINCODE, ANNUAL_INCOME, OCCUPATION, POLITICAL_EXPOSED]

		private static final def FIELDS2 = []

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"

		private static final def FIELD_ERROR1 = "//small[@class='error']"

		private static final def FIELD_ERROR2 = "//small[@class='error dropdown-error display-block pull-left']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR1, FIELD_ERROR2]

		//error message map (Key-Value Pair)
		def static final personalDetailsPageErrorMessageMap = [
			street_add_req:"Please enter your street address",
			city_req:"Please enter your city",
			state_req:"Please enter your state",
			pincode_invalid:"Please enter valid pincode",
			field_req:"Please enter"
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			println ("PersonalDetailsForm.populateFields - data: " + formData)
			O3CommonPage.personalDetailsList = []
			def data = []

			for(int i=0; i < FIELDS.size(); i++){
				if(!(browser.gettext(FIELDS[i], "value")).equals("") && !FIELDS[i].equals(POLITICAL_EXPOSED)){
					O3CommonPage.personalDetailsList.add(browser.gettext(FIELDS[i], "value"))
				}else if(FIELDS[i].equals(POLITICAL_EXPOSED)){
					println "politically exposed field"
				}else{
					println "in else part..."
					FIELDS2.add(FIELDS[i])
					O3CommonPage.personalDetailsList.add(formData[i])
					data.add(formData[i])
				}
			}
			println "O3CommonPage.personalDetailsList:: "+O3CommonPage.personalDetailsList
			println "personal details data: "+data
			println "FIELDS 2: "+FIELDS2
			def outcome = WebForm.checkFormFieldsData(data, FIELDS2)
			if(outcome.isSuccess()){
				outcome = WebForm.enterData(browser, data, FIELDS2, SUBMIT_FORM, WAIT_REQ_FIELDS)
				browser.click(TERMS)
				browser.delay(1000)
			}
			return outcome
		}

		/**
		 * To submit the form
		 * @param browser browser instance
		 * @param data  array containing test data
		 */
		def final submit(browser, data) {
			def actualValidationMsg = submitForm browser, FIELDS, SUBMIT_FORM, data, ERROR_MESSAGE_FIELDS
			browser.delay(2000)
			def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, personalDetailsPageErrorMessageMap)
			def outcome = new SuccessOutcome();
			outcome.setResultData(actualValidationMsgKeys)
			return outcome
		}

		//override submitForm
		def static submitForm = {browser, formFields, submitButton, data, errFields ->
			browser.click submitButton // submit the form.
			browser.getValidationMessages errFields // get the validation messages from the current page.
		}

		//To verify the personal details match after saving
		def static final personalDetailsDisplayed = {browser, formData ->
			def fieldListValue = []
			browser.delay(5000)
			for(int i=0; i < FIELDS.size(); i++){
				fieldListValue.add(browser.gettext(FIELDS[i], "value"))
			}
			println "O3CommonPage.personalDetailsList : "+O3CommonPage.personalDetailsList+ "\nfieldListValue : "+fieldListValue
			if(O3CommonPage.personalDetailsList?.sort(false) == fieldListValue?.sort(false)){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "DataMisMatchIssue", "Saved bank data is not matching with the prepopulated field data")
			}
		}
	}
}
