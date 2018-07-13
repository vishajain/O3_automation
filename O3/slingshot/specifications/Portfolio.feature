Feature: Portfolio scenarios for the user  
  As an user of the application
  I want to test portfolio feature
  
Background: Login:SuccessLogin

Scenario: To verify user navigates to portfolio page
	Given I am ON dashboard
	And I CLICK userOption
	And I CLICK portfolio
	Then I am ON portfolio page
	
Scenario: To verify user navigates to dashboard on clicking back to dashboard link
	Given I am ON dashboard
	And I CLICK userOption
	And I CLICK portfolio
	Then I am ON portfolio page
	And I CLICK backToDashboard link
	Then I am ON dashboard
	
Scenario: To verify user navigates to portfolio on clicking close in amount to invest popup link with DATA AmountToInvest_Failure
	Given I am ON dashboard
	And I CLICK userOption
	And I CLICK portfolio
	Then I am ON portfolio page
	And I ENTER invalid amount with DATA AmountToInvest_Failure
	And I CLICK closeInvestPopUp
	Then I am ON portfolio page
	
Scenario: To verify the failure cases for amount to be invested for a portfolio with DATA AmountToInvest_Failure
	Given I am ON dashboard
	And I CLICK userOption
	And I CLICK portfolio
	Then I am ON portfolio page
	And I ENTER invalid amount with DATA AmountToInvest_Failure
	When I SUBMIT the form
	Then I am ON portfolio page
	
Scenario: To verify the failure case in Add fund after entering amount to be invested with AddFundOnline_Failure
	Given I am ON dashboard
	When I VERIFY storedAmount
	And I CLICK userOption
	And I CLICK portfolio
	Then I am ON portfolio page
	And I ENTER valid amount with DATA AmountToInvest_Success
	When I SUBMIT the form
	Then I am ON payOnline page
	And I ENTER onlineAmount invalid with DATA AddFundOnline_Failure
	And I SUBMIT onlineAmount form
	Then the error message is displayed
	
@Group(investFailure)
Scenario: To verify the successful investment through cheque for a portfolio with DATA AmountToInvest_Success
	Given I am ON dashboard
	When I VERIFY storedAmount
	And I CLICK userOption
	And I CLICK portfolio
	Then I am ON portfolio page
	And I ENTER valid amount with DATA AmountToInvest_Success
	When I SUBMIT the form
	Then I am ON payOnline page
	When I CLICK payByCheque link
	Then I am ON payByCheque screen
	And I ENTER payByChequeAmount with DATA ChequePayment_Success
	And I SUBMIT payByChequeAmount
	And I CLICK cropNSave
	Then I am ON portfolio
	And I CLICK userOption
	When I CLICK transactions
	Then I am ON transactions page
	And I VERIFY newTransaction
	When I PERFORM updateAmount
	And I PERFORM approveFundRequest
	And I CLICK backToDashboard link
	Then I am ON dashboard	
	Then I VERIFY amountUpdated
	Then the updated amount is displayed in dashboard
	
