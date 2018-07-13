Feature: Successful and Unsuccessful Login to the application  
  As an user of the application
  I want to test Login feature 
  
Scenario: To verify the error messages with wrong login credentials with DATA Login_Failure
	Given I am ON login page
	And I ENTER invalid login credentials
	And I SUBMIT the form
	Then I am ON login page
	
@Name(loginSuccess)
Scenario: To verify the successful login with DATA LoginDashboard_Success
	Given I am ON login page
	And I ENTER valid login credentials with DATA LoginDashboard_Success
	And I SUBMIT the form
	
@Name(SuccessLogin)
Scenario: To verify the successful login with DATA Login_Success
	Given I am ON login page
	And I ENTER valid login credentials with DATA Login_Success
	And I SUBMIT the form