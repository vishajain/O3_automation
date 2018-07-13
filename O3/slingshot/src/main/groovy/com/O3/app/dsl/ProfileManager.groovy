package com.O3.app.dsl

import org.codehaus.groovy.control.customizers.ImportCustomizer.Import;

import com.O3.app.pages.userProfile.ProfilePage

class ProfileManager {
	
	//To verify that the personal details displayed in the profile matches the entered values
	def personalDetailsDisplayed = { browser, formData ->
		ProfilePage.personalDetailsDisplayed browser, formData
	}
	
	//To verify the bank details displayed in the profile page matches the entered values
	def bankDetailsDisplayed = { browser, formData ->
		ProfilePage.bankDetailsDisplayed browser, formData
	}
	
	
}
