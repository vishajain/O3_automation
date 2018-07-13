Feature: Successful and Unsuccessful Reset Password to the application  
  As an user of the application
  I want to test reset password feature
  
Scenario: Verify clicking on forgot password link from login page navigates to reset password page
	Given I am ON login page
	And I CLICK resetPassword link
	Then I am ON resetPassword page
	
Scenario: Verify Reset Password with invalid mobile inputs with DATA ResetPassMobileNum_Failure
	Given I am ON login page
	And I CLICK resetPassword link
	Then I am ON resetPassword page
	And I ENTER invalid mobile details with DATA ResetPassMobileNum_Failure
	When I SUBMIT the form
	Then the error message is displayed
	
Scenario: Verify Reset Password with valid mobile input redirects to generate otp page with DATA ResetPassMobileNum_Success
	Given I am ON login page
	And I CLICK resetPassword link
	Then I am ON resetPassword page
	And I ENTER valid mobile details with DATA ResetPassMobileNum_Success
	When I SUBMIT the form
	Then I am ON generateOtp page
	
Scenario: Verify Reset Password with invalid generated OTP with DATA ResetPassswordOTP_Failure
	Given I am ON login page
	And I CLICK resetPassword link
	Then I am ON resetPassword page
	And I ENTER valid mobile details with DATA ResetPassMltplMobileNum_Success
	When I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA PasswordType_Success
	And I ENTER invalid OTP details with DATA ResetPassswordOTP_Failure
	And I SUBMIT the form
	Then I am ON generateOtp page
	
Scenario: Verify Reset Password with valid generated OTP with DATA ResetPassswordOTP_Success
	Given I am ON login page
	And I CLICK resetPassword link
	Then I am ON resetPassword page
	And I ENTER valid mobile details with DATA ResetPassMltplMobileNum_Success
	When I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA PasswordType_Success
	And I ENTER valid OTP details with DATA ResetPassswordOTP_Success
	And I SUBMIT the form
	Then I am ON setNewPassword page
	
Scenario: Verify Set new Password with invalid inputs with DATA SetNewPassword_Failure
	Given I am ON login page
	And I CLICK resetPassword link
	Then I am ON resetPassword page
	And I ENTER valid mobile details with DATA ResetPassMltplMobileNum_Success
	When I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA PasswordType_Success
	And I ENTER valid OTP details with DATA ResetPassOTPMultiple_Success
	And I SUBMIT the form
	Then I am ON setNewPassword page
	And I ENTER invalid inputs for setting new password with DATA SetNewPassword_Failure
	And I SUBMIT the form
	Then the error messages are displayed
	
Scenario: Verify set new password with valid inputs with DATA SetNewPassword_Success
	Given I am ON login page
	And I CLICK resetPassword link
	Then I am ON resetPassword page
	And I ENTER valid mobile details with DATA ResetPassMltplMobileNum_Success
	When I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA PasswordType_Success
	And I ENTER valid OTP details with DATA ResetPassswordOTP_Success
	And I SUBMIT the form
	Then I am ON setNewPassword page
	And I ENTER valid inputs for setting new password with DATA SetNewPassword_Success
	And I SUBMIT the form
	Then I am ON login page
	
