Feature: To test the succcessful and unsuccessful scenarios of Questionnaire functionality
	As an User of the application
	I want to test Questionnaire functionality
	
Background: Login: SuccessLogin

Scenario: To verify the successful navigation to questionnaire page
	Given I am ON startQuestionnaire page
	And I CLICK startQuestionnaire
	Then I am ON answerQuestionnaire page
	
Scenario: To verify the invalid inputs for questionnaire
	Given I am ON startQuestionnaire page
	And I CLICK startQuestionnaire
	Then I am ON answerQuestionnaire page
	And I ENTER invalid inputs with DATA Questionnaire_Failure
	Then I SUBMIT the form
	Then the error message is displayed