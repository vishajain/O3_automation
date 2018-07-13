Feature: Successful and Unsuccessful Aadhaar verification of user  
  As an user of the application
  I want to test Aadhaar verification feature
  
Background: Login:loginSuccess,SuccessOnboarding:PanSuccess,SuccessOnboarding:IPVSuccess

Scenario: To verify the invalid aadhaar details with DATA Aadhaar_Failure
	Given I am ON aadhaarInfo page
	And I ENTER invalid aadhaar details with DATA Aadhaar_Failure
	When I SUBMIT the form
	Then I am ON aadhaarInfo page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
	