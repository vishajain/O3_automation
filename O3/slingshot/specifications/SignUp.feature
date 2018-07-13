Feature: Successful and Unsuccessful SignUp to the application  
  As an user of the application
  I want to test SignUp feature
  
Scenario: To verify the sign up with invalid details with DATA SignUp_Failure
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER invalid user details with DATA SignUp_Failure
	And I SUBMIT the form
	Then I am ON registration page
	
Scenario: To verify the sign up with valid details with DATA SignUp_Success
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER valid user details with DATA SignUp_Success
	And I SUBMIT the form
	Then I am ON generateOtp page
	
Scenario: To verify the generated OTP with invalid details with DATA SignUpNew_Success
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER valid user details with DATA SignUpNew_Success
	And I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA RegistrationType_Success
	And I ENTER invalid OTP details with DATA InvalidOTP_Failure
	And I SUBMIT the form
	Then I am ON generateOtp page
	
@Name(SignUpSuccess), @Group(signUpPermanent)
Scenario: To verify the generated OTP with valid details with DATA SignUpPermanentUser_Success
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER valid user details with DATA SignUpPermanentUser_Success
	And I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA RegistrationType_Success
	And I ENTER valid OTP details with DATA ValidOTP_Success
	And I SUBMIT the form
	Then I am ON panDetails page
	When I PERFORM logout
	Then I am ON login page
	
Scenario: To verify the duplicate sign up with DATA DuplicateSignUp_Failure
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER duplicate user details with DATA DuplicateSignUp_Failure
	And I SUBMIT the form
	Then I am ON registration page

Scenario: To verify editing the details in Sign Up page and verifying OTP with DATA SignUp_Success
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER valid user details with DATA SignUp_Success
	And I SUBMIT the form
	Then I am ON generateOtp page
	And I CLICK backButton
	Then I am ON registration page
	And I ENTER valid details with edited value with DATA SignUpAgain_Success
	And I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA RegistrationType_Success
	And I ENTER valid OTP details with DATA ValidOTP_Success
	And I SUBMIT the form
	Then I am ON panDetails page
	When I PERFORM logout
	Then I am ON login page
	And I PERFORM deleteUserFromDB
	Then the user is deleted
	
Scenario: To resend the OTP and verify the generated OTP with DATA SignUp_Success
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER valid user details with DATA SignUp_Success
	And I SUBMIT the form
	Then I am ON generateOtp page
	And I CLICK resendOTP link
	And I PERFORM getOTPFromDB with DATA RegistrationType_Success
	And I ENTER valid OTP details with DATA ValidOTP_Success
	And I SUBMIT the form
	Then I am ON panDetails page
	When I PERFORM logout
	Then I am ON login page
	And I PERFORM deleteUserFromDB
	Then the user is deleted
