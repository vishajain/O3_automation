Feature: Successful and Unsuccessful Basic details of user
	As an User of the application
	I want to test the basic details of the user

Background: Login:SuccessLogin,SuccessOnboarding:PanSuccess,SuccessOnboarding:IPVSuccess

Scenario: To verify the details displayed in user details page matches the entered details
	Given I am ON userDetails page
	And I VERIFY verifyDOBPhone
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify the invalid inputs in basic details with DATA BasicDetails_Failure
	Given I am ON userDetails page
	And I ENTER basicDetails invalid with DATA BasicDetails_Failure
	And I SUBMIT basicDetails
	Then I am ON userDetails page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify the valid inputs in basic details with DATA BasicDetails_Success
	Given I am ON userDetails page
	And I ENTER basicDetails valid with DATA BasicDetails_Success
	And I SUBMIT basicDetails
	Then I am ON userDetails page
	And I PERFORM logout action
	Then I am ON login page
	And I ENTER valid login details with DATA Login_Success 
	And I SUBMIT the form
	Then I am ON userDetails page
	And I VERIFY basicDetailsDisplayed
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify the invalid inputs in Parents details with DATA ParentsDetails_Failure
	Given I am ON userDetails page
	And I ENTER basicDetails valid with DATA BasicDetailsMultiple_Success
	And I SUBMIT basicDetails
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER parentsDetails invalid with DATA ParentsDetails_Failure
	And I SUBMIT parentsDetails
	Then I am ON userDetails page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify the valid inputs in Parents details with DATA ParentsDetails_Success
	Given I am ON userDetails page
	And I ENTER basicDetails valid with DATA BasicDetails_Success
	And I SUBMIT basicDetails
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER parentsDetails valid with DATA ParentsDetails_Success
	And I SUBMIT parentsDetails
	Then I am ON userDetails page
	And I PERFORM logout action
	Then I am ON login page
	And I ENTER valid login details with DATA Login_Success 
	And I SUBMIT the form
	Then I am ON userDetails page
	And I CLICK parentsTab
	And I VERIFY parentsDetailsDisplayed
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify the invalid inputs in bank details with DATA BankDetails_Failure
	Given I am ON userDetails page
	And I ENTER basicDetails valid with DATA BasicDetailsMultiple_Success
	And I SUBMIT basicDetails form
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER parentsDetails valid with DATA ParentsDetailsMultiple_Success
	And I SUBMIT parentsDetails
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER bankDetails invalid with DATA BankDetails_Failure
	And I SUBMIT bankDetails
	Then I am ON userDetails page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page

@Group(scena)
Scenario: To verify the valid inputs in bank details with DATA BankDetails_Success
	Given I am ON userDetails page
	And I ENTER basicDetails valid with DATA BasicDetails_Success
	And I SUBMIT basicDetails page
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER parentsDetails valid with DATA ParentsDetails_Success
	And I SUBMIT parentsDetails form
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER bankDetails valid with DATA BankDetails_Success
	And I SUBMIT bankDetails
	Then I am ON userDetails page
	And I PERFORM logout action
	Then I am ON login page
	And I ENTER valid login details with DATA Login_Success 
	And I SUBMIT the form
	Then I am ON userDetails page
	And I CLICK bankTab
	And I VERIFY bankDetailsDisplayed
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page


Scenario: To verify the invalid inputs in Other details with DATA OtherUserInfo_Failure
	Given I am ON userDetails page
	And I ENTER basicDetails valid with DATA BasicDetailsMultiple_Success
	And I SUBMIT basicDetails form
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER parentsDetails valid with DATA ParentsDetailsMultiple_Success
	And I SUBMIT parentsDetails
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER bankDetails valid with DATA BankDetailsMultiple_Success
	And I SUBMIT bankDetails
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER personalDetails invalid with DATA OtherUserInfo_Failure
	And I SUBMIT personalDetails
	Then I am ON userDetails page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
	@Group(BasicDetailsSuccess)
Scenario: To verify the valid inputs in personal details with DATA BankDetails_Success
	Given I am ON userDetails page
	And I ENTER basicDetails valid with DATA BasicDetails_Success
	And I SUBMIT basicDetails page
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER parentsDetails valid with DATA ParentsDetails_Success
	And I SUBMIT parentsDetails form
	Then I am ON userDetails page
	And I CLICK nextButton
	Then I am ON userDetails page
	And I ENTER bankDetails valid with DATA BankDetails_Success
	And I SUBMIT bankDetails
	Then I am ON userDetails page
	And I CLICK nextButton
	And I ENTER personalDetails valid with DATA OtherUserInfo_Success
	And I SUBMIT personalDetails
	Then I am ON uploadDocs page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page

