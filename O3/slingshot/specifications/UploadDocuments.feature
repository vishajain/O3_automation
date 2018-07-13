Feature: Successful and Unsuccessful file Upload
	As an User of the application
	I want to test the Upload file functionality

Background: Login:SuccessLogin,SuccessOnboarding:PanSuccess,SuccessOnboarding:IPVSuccess,SuccessOnboarding:UserDetails

@Group(uploadDocumentFailure)
Scenario: To verify the error messages by uploading invalid size of documents with DATA UploadDocuments_Failure
	Given I am ON uploadDocs page
	And I SUBMIT form with wrong file sizes
	Then I am ON uploadDocs page
	And I PERFORM stepBackToPAN
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify Save button functionality on Upload Documents page with DATA UploadDocuments_Success
	Given I am ON uploadDocs page
	And I ENTER form with DATA UploadDocuments_Success
	Then I CLICK saveDocuments button
	And I VERIFY savedDocuments
	Then I am ON uploadDocs page
		
			@Group(uploadDocs)
Scenario: To verify the note present for non KRA verified PAN users
	Given I am ON uploadDocs page
	And I VERIFY notePresent
	Then I am ON uploadDocs page
	
	