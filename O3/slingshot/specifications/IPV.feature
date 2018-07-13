Feature: Successful and Unsuccessful IPV verification to the application  
  As an user of the application
  I want to test IPV verification feature

Background: Login:loginSuccess,SuccessOnboarding:PanSuccess

Scenario: To verify the invalid IPV file upload with DATA IPVUpload_Failure
	Given I am ON IPV page
	And I SUBMIT the form
	Then I am ON IPV page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	