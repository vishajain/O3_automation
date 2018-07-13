Feature: Successful Onboarding process
  As an user of the application
  I want to test Successful Onboarding process

Background: Login:SuccessLogin
	
@Name(PanSuccess)
Scenario: To verify the valid NON-KRA verified PAN details navigates to IPV with DATA PAN_Success
	Given I am ON panDetails page
	And I PERFORM deletePAN
	And I ENTER valid pan details with DATA PAN_Success
	And I SUBMIT the form
	Then I am ON IPV page

@Name(IPVSuccess)
Scenario: To verify the valid IPV file upload with DATA IPVUpload_Success
	Given I am ON IPV page
	And I SUBMIT the form
	And I CLICK cropNSave
	Then I am ON userDetails page
	
@Name(UserDetails)
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
	
@Name(uploadDocumentSuccess)
Scenario: To verify the successful upload of documents with DATA UploadDocuments_Success
	Given I am ON uploadDocs page
	And I SUBMIT form
	Then I CLICK allDone button
	Then I am ON eSign page
	And I CLICK closeDigioWindow
	And I PERFORM closeAlert
	Then I am ON documentVerification page
	
Scenario: To verify successful navigation from document verification page to dashboard with DATA Login_Success
	Given I am ON documentVerification page
	And I PERFORM navigateUserToDashboard
	Then  I PERFORM logout action
	Then I am ON login page
	And I ENTER valid login credentials with DATA Login_Success
	And I SUBMIT the form
	Then I am ON dashboard page

Scenario: To verify the the personal details in profile matches with the entered data with DATA Login_Success
	Given I am ON dashboard page
	And I CLICK userOption
	And I CLICK profile option
	Then I am ON userProfile page
	And I VERIFY personalDetailsDisplayed
	Then all the personalDetails are matched

Scenario: To verify the the bank details in profile matches with the entered data with DATA Login_Success
	Given I am ON dashboard page
	And I CLICK userOption
	And I CLICK profile option
	Then I am ON userProfile page
	And I CLICK bankDetails
	When I VERIFY bankDetailsDisplayed
	Then all the personalDetails are matched
	