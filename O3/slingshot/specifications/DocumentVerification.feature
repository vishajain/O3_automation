Feature: Unsuccessful file Upload
	As an User of the application
	I want to test the Upload file functionality

Background: Login:loginSuccess,SuccessOnboarding:PanSuccess,SuccessOnboarding:IPVSuccess,SuccessOnboarding:UserDetails,SuccessOnboarding:uploadDocumentSuccess

Scenario: To verify the continue esign button is enabled
	Given I am ON documentVerification page
	And I VERIFY esignButtonEnabled
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	