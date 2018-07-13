package com.O3.app.pages.user

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage

/**
 * Created by Sandhya on 24/9/14
 */

final class UploadDocumentsPage extends WebPage {

	//Override
	def populateData = {browser, formKey, formData ->
		new UploadDocumentsForm().populateFields(browser, formData);
	}

	//Override
	def submit = {browser, formKey, formData ->
		new UploadDocumentsForm().submit(browser, formData)
	}

	//To close the alert pop up after closing digio
	def static closeAlert = {browser, formData ->
		new UploadDocumentsForm().closeAlert browser, formData
	}

	//To verify save upload docs by clicking save button
	def static savedDocuments = {browser, formData ->
		new UploadDocumentsForm().savedDocuments browser, formData
	}

	//To verify note present for non KRA verified users
	def static notePresent = {browser, formData ->
		new UploadDocumentsForm().notePresent browser, formData
	}

	static final class UploadDocumentsForm extends WebForm {

		//Login form elements
		private static final def BANK_STATEMENT = ".//div[@class='col-md-12 no-padding document document-upload']//input[@id='bankStatementFilePicker']" //".//div[@class='col-md-12 no-padding document document-upload']//div[@class='btn btn-white link-primary btn-block btn-md pos-r upload-container']"

		private static final def SIGNATURE = ".//input[@id='signatureFilePicker']"

		private static final def INCOME_CERTIFICATE = ".//div[@class='row pt_20 main-content ']/div/div[@class='col-md-12 no-padding document document-upload mt_10'][2]//input[@id='bankStatementFilePicker']"

		private static final def AADHAAR = ".//div[@class='col-md-12 no-padding document document-upload mt_10'][3]//input[@id='bankStatementFilePicker']"

		private static final def PAN = ".//div[@class='row pt_20 main-content ']/div/div[@class='col-md-12 no-padding document document-upload mt_10'][4]//input[@id='bankStatementFilePicker']"

		private static final def SUBMIT = ".//button[.='All done!']"

		private static final def SAVE = ".//button[contains(text(),'Save')]"

		private static final def ALL_DONE = ".//button[.='All done!']"

		private static final def VIEW = ".//a[.='View']"

		private static final def NOTE = ".//div[@class='fw_300 fs_14 pb_10']"

		private static final def SKIP_ESIGN = ".//label[@class='checkbox fw_normal']/span"

		private static final def FIELDS = [BANK_STATEMENT, SIGNATURE, INCOME_CERTIFICATE, PAN, AADHAAR]

		private static final def FIELDS_2 = [BANK_STATEMENT, SIGNATURE, INCOME_CERTIFICATE, AADHAAR]

		// the error fields.
		private static final def FORM_ERROR = "//div[@class='alert alert-danger toast']"

		private static final def FIELD_ERROR = "//small[@class='error']"

		private static final def ERROR_MESSAGE_FIELDS = [FORM_ERROR, FIELD_ERROR]

		//error message map (Key-Value Pair)
		def static final uploadDocumentsPageErrorMessageMap = [
			invalid_size:"The file size must not exceed 10MB"
		]

		//To enter data
		def static final populateFields = { browser, formData ->
			println ("PanForm.populateFields - data: " + formData)
			O3CommonPage.savedDocumentsList = []
			if(browser.isDisplayed(SKIP_ESIGN)){
				for(int i=0;i<FIELDS_2.size()-1;i++){
					O3CommonPage.savedDocumentsList.add(formData[i])
					browser.uploadFile(FIELDS_2[i], formData[i].trim())
					browser.delay(1000)
				}
			}else{
				for(int i=0;i<FIELDS.size()-1;i++){
					O3CommonPage.savedDocumentsList.add(formData[i])
					browser.uploadFile(FIELDS[i], formData[i].trim())
					browser.delay(1000)
				}
			}
			browser.scrollToElement2(SAVE)
			browser.delay(1000)
			return new SuccessOutcome()
		}


		def final submit(browser, data) {
			def outcome
			if(browser.isDisplayed(SKIP_ESIGN)){
				def actualValidationMsg = submitForm browser, FIELDS_2, data, ERROR_MESSAGE_FIELDS
				def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, uploadDocumentsPageErrorMessageMap)
				outcome = new SuccessOutcome();
				outcome.setResultData(actualValidationMsgKeys)
			}else{
				def actualValidationMsg = submitForm browser, FIELDS, data, ERROR_MESSAGE_FIELDS
				def actualValidationMsgKeys = getActualErrorMessageKeys(actualValidationMsg, uploadDocumentsPageErrorMessageMap)
				outcome = new SuccessOutcome();
				outcome.setResultData(actualValidationMsgKeys)
			}
			return outcome
		}

		//override submitForm
		def static submitForm = {browser, formFields, data, errFields ->
			def file = data
			def errorList = []
			for(int i=0;i<formFields.size();i++){
				browser.uploadFile(formFields[i], file[i].trim())
				browser.delay(3000)
				if(browser.isAlertPresent()){
					errorList.add(browser.addAlertMsgToList())
				}else{
					errorList = []
				}
			}
			return errorList
		}

		//To close the alert pop up after closing digio
		def static final closeAlert = {browser, formData ->
			if(browser.isAlertPresent()){
				browser.addAlertMsgToList()
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "AlertPresentIssue", "Alert is not present.")
			}
		}

		//To verify save upload docs by clicking save button
		def static final savedDocuments = {browser, formData ->
			def viewList = browser.getLists(VIEW)
			if((O3CommonPage.savedDocumentsList).size() == viewList.size()){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "SizeMismatchIssue", "Size of uploaded documents is not matching with that of view options")
			}
		}

		//To verify note present for non KRA verified users
		def static final notePresent ={browser, formData ->
			if((browser.isDisplayed(NOTE)) && (browser.gettext(NOTE).equals("Note: Upload self-attested copies of documents"))){
				return new SuccessOutcome()
			}else{
				return O3CommonPage.returnFailureOutcome(browser, "NoteDisplayIssue", "Note for non KRA verified users is not displayed")
			}
		}
	}
}