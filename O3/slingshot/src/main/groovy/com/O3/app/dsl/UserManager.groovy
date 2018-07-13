package com.O3.app.dsl

import org.codehaus.groovy.control.customizers.ImportCustomizer.Import;

import com.O3.app.pages.user.GenerateOTPPage
import com.O3.app.pages.user.LoginPage
import com.O3.app.pages.user.UserDetailsPage
import com.O3.app.pages.user.UploadDocumentsPage
import com.O3.app.pages.user.DocumentVerificationPage


/**
 * Created by Samir on 17/11/2017
 */
class UserManager {

	//To verify the Generated OTP
	def getOTPFromDB = { browser, formData ->
		GenerateOTPPage.getOTPFromDB browser, formData
	}

	//To delete a user after registration
	def deleteUserFromDB = { browser, formData ->
		GenerateOTPPage.deleteUserFromDB browser, formData
	}

	//To logout from the application
	def logout = { browser, formData ->
		LoginPage.logout browser, formData
	}

	//To make the step back to PAN page
	def stepBackToPAN = { browser, formData ->
		GenerateOTPPage.stepBackToPAN browser, formData
	}

	//For verifying  ther users mobile number  matches the entered values
	def verifyDOBPhone = { browser, formData ->
		UserDetailsPage.verifyDOBPhone browser, formData
	}

	//For verify saved user basic details are displayed after loggin in again
	def basicDetailsDisplayed =  { browser, formData ->
		UserDetailsPage.basicDetailsDisplayed browser, formData
	}

	//To verify the parents details match after saving
	def parentsDetailsDisplayed = {browser, formData ->
		UserDetailsPage.parentsDetailsDisplayed browser, formData
	}

	//To verify the bank details match after saving
	def bankDetailsDisplayed = {browser, formData ->
		UserDetailsPage.bankDetailsDisplayed browser, formData
	}

	//To verify the personal details match after saving
	def personalDetailsDisplayed = {browser, formData ->
		UserDetailsPage.bankDetailsDisplayed browser, formData
	}

	//To delete the PAN number from db
	def deletePAN = {browser, formData ->
		GenerateOTPPage.deletePAN browser, formData
	}

	//To close the alert pop up after closing digio
	def closeAlert = {browser, formData ->
		UploadDocumentsPage.closeAlert browser, formData
	}

	//To verify that the continue esign button is enabled in document verification page
	def esignButtonEnabled = {browser, formData ->
		DocumentVerificationPage.esignButtonEnabled browser, formData
	}

	//To give access to user to navigate to dashboard from document verification through db
	def navigateUserToDashboard = { browser, formData ->
		DocumentVerificationPage.navigateUserToDashboard browser, formData
	}

	//To verify save upload docs by clicking save button
	def savedDocuments = { browser, formData ->
		UploadDocumentsPage.savedDocuments browser, formData
	}

	//To verify note present for non KRA verified users
	def notePresent = { browser, formData ->
		UploadDocumentsPage.notePresent browser, formData
	}

	//To make the step back to Upload Documents page
	def stepBackToUploadDocuments = { browser, formData ->
		GenerateOTPPage.stepBackToUploadDocuments browser, formData
	}

	//to delete uploaded documents from database
	def deleteDocuments = { browser, formData ->
		GenerateOTPPage.deleteDocuments browser, formData
	}
    
	//To make the step back to document verification page
	def stepBackToVerification = { browser, formData ->
		GenerateOTPPage.stepBackToVerification browser, formData
	}

	
	/*// To make user eSigned by clicking on confirm button on alert
	def eSignConfirmation = { browser, formData ->
		println "inside eSignConfirmation of manager"
		DocumentVerificationPage.eSignConfirmation browser, formData
	}*/

}
