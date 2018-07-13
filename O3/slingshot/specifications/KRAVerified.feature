Feature: To test the KRA verified feature functionality
	As an User of the application
	I want to test the functionality of KRA verified  users

Scenario: To verify the signup and generated OTP providing valid details with DATA SignUpKRAUser_Success
	Given I am ON login page
	And I CLICK signUp button
	Then I am ON registration page
	And I ENTER valid user details with DATA SignUpKRAUser_Success
	And I SUBMIT the form
	Then I am ON generateOtp page
	And I PERFORM getOTPFromDB with DATA RegistrationType_Success
	And I ENTER valid OTP details with DATA ValidOTP_Success
	And I SUBMIT the form
	Then I am ON panDetails page
	When I PERFORM logout
	Then I am ON login page
	
Scenario: To verify the valid KRA verified PAN details navigates to aadhaar details with DATA KRA_Verified_PAN_Success
	Given I am ON login page
	And I ENTER valid data with DATA LoginKRAUser_Success
	And I SUBMIT form
	Then I am ON panDetails page	
	And I PERFORM deletePAN
	When I ENTER valid pan details with DATA KRA_Verified_PAN_Success
	And I SUBMIT the form
	Then I am ON userDetails page	
	And I PERFORM logout action
	Then I am ON login page


Scenario: To verify prepopulated user details from KRA verified pan with DATA BasicDetails_Success
  Given I am ON login page
	And I ENTER valid data with DATA LoginKRAUser_Success
	And I SUBMIT form
	Then I am ON userDetails page
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
	Then I am ON userDetails page
	And I ENTER personalDetails valid with DATA OtherUserInfo_Success
	And I SUBMIT personalDetails
	Then I am ON uploadDocs page
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify save uploaded documents functionality with DATA KRA_UploadDocuments_Success
	Given I am ON login page
	And I ENTER valid data with DATA LoginKRAUser_Success
	And I SUBMIT form
	Then I am ON uploadDocs page
	And I ENTER form with DATA KRA_UploadDocuments_Success
	Then I CLICK saveDocuments button
	Then I am ON uploadDocs page
	And I VERIFY savedDocuments
	Then I am ON uploadDocs page
	And I PERFORM deleteDocuments 
	Then I am ON uploadDocs page
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify upload documents and skip Esign functionality for KRA verified pan user with DATA KRA_UploadDocuments_Success
	Given I am ON login page
	And I ENTER valid data with DATA LoginKRAUser_Success
	And I SUBMIT form
	Then I am ON uploadDocs page
	And I SUBMIT form with DATA KRA_UploadDocuments_Success
	And I CLICK skipESign
	When I CLICK allDone
	Then I am ON documentVerification page
	And I PERFORM stepBackToUploadDocuments
	And I PERFORM logout action
	Then I am ON login page
	
Scenario: To verify eSign functionality after Upload Documents page with DATA KRA_UploadDocuments_Success
	Given I am ON login page
	And I ENTER valid data with DATA LoginKRAUser_Success
	And I SUBMIT form
	Then I am ON uploadDocs page
	And I SUBMIT form with DATA KRA_UploadDocuments_Success
	When I CLICK allDone
	Then I am ON eSign page
	And I CLICK closeDigioWindow
	And I PERFORM closeAlert
	Then I am ON documentVerification page
	And I VERIFY esignButtonEnabled
	Then I am ON documentVerification page

Scenario: To verify skip eSign On Document Verification page with DATA KRA_UploadDocuments_Success
	Given I am ON login page
	And I ENTER valid data with DATA LoginKRAUser_Success
	And I SUBMIT form
	Then I am ON documentVerification page
	And I VERIFY esignButtonEnabled
	Then I am ON documentVerification page
	And I PERFORM navigateUserToDashboard
	And I CLICK skipESign
	Then I am ON confirmRequest page
	And I CLICK continue
	Then I am ON dashboard
	And I PERFORM stepBackToVerification page
	And I PERFORM logout action
	Then I am ON login page
	
@Group(KRAL)
Scenario: To verify Continue ESign on Document Verification page with DATA KRA_UploadDocuments_Success
	Given I am ON login page
	And I ENTER valid data with DATA LoginKRAUser_Success
	And I SUBMIT form
	Then I am ON documentVerification page
	And I VERIFY esignButtonEnabled
	And I CLICK continueESign
	Then I am ON eSign page
	And I CLICK closeDigioWindow
	And I PERFORM closeAlert
	Then I am ON documentVerification page
	And I PERFORM logout action
	Then I am ON login page
