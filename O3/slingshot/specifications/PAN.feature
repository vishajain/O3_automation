Feature: Successful and Unsuccessful PAN verification to the application  
  As an user of the application
  I want to test PAN verification feature

Background: Login:loginSuccess
	
Scenario: To verify the session management on logging out and logging again from PAN page with DATA Login_Success
	Given I am ON panDetails Page
	And I PERFORM logout from the application
	Then I am ON login page
	And I ENTER valid login details with DATA Login_Success
	And I SUBMIT the form
	Then I am ON panDetails page
	And I PERFORM logout from the application
	Then I am ON login page
	
Scenario: To verify the invalid PAN details with DATA PAN_Failure
	Given I am ON panDetails page
	And I ENTER invalid pan details with DATA PAN_Failure
	And I SUBMIT the form
	Then I am ON panDetails page
	And I PERFORM logout from the application
	Then I am ON login page
	
Scenario: To verify the valid KRA verified PAN details navigates to aadhaar with DATA KRA_Verified_PAN_Success
	Given I am ON panDetails page
	And I PERFORM deletePAN
	When I ENTER valid pan details with DATA KRA_Verified_PAN_Success
	And I SUBMIT the form
	Then I am ON userDetails page
	And I CLICK stepOver
	Then I am ON panDetails page
	And I PERFORM logout from the application
	Then I am ON login page