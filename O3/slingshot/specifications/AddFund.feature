Feature: To test the succcessful and unsuccessful scenarios of Add Fund
	As an User of the application
	I want to test Add Fund functionality
	
Background: Login: SuccessLogin

Scenario: To verify add fund functionality by clicking on Add Fund button
	Given I am ON dashboard
	When I CLICK addFund
	Then I am ON payOnline page
	When I CLICK cross button
	Then I am ON dashboard
	
Scenario: To verify Pay By Cheque functionality by clicking on PayByCheque link
	Given I am ON dashboard
	When I CLICK addFund
	Then I am ON payOnline page
	When I CLICK payByCheque link
	Then I am ON payByCheque screen
	And I CLICK cross button
	Then I am ON dashboard
	
Scenario: To verify the Redeem Fund functionality in case of zero balance with DATA ZeroBalanceRedeem_Failure
	Given I am ON dashboard
	And I CLICK redeemFund
	When I SUBMIT the form
	Then I am ON dashboard

Scenario: To verify add fund functionality using Pay By Cheque option with invalid details with DATA ChequePayment_Failure
	Given I am ON dashboard
	When I CLICK addFund
	Then I am ON payOnline page
	When I CLICK payByCheque link
	Then I am ON payByCheque screen
	And I ENTER payByChequeAmount invalid data with DATA ChequePayment_Failure
	And I SUBMIT payByChequeAmount form
	Then I am ON payByCheque screen
	
Scenario: To verify add fund functionality using Pay Online option with invalid details with DATA OnlinePayment_Failure
	Given I am ON dashboard
	When I CLICK addFund
	Then I am ON payOnline page
	And I ENTER onlineAmount invalid data with DATA OnlinePayment_Failure
	And I SUBMIT onlineAmount form
	
@Name(FundSuccess)
Scenario: To verify add fund functionality using Pay By Cheque option with valid details with DATA ChequePayment_Success
	Given I am ON dashboard
	And I PERFORM storedAmount
	When I CLICK addFund
	Then I am ON payOnline page
	When I CLICK payByCheque link
	Then I am ON payByCheque screen
	And I ENTER payByChequeAmount valid data with DATA ChequePayment_Success
	And I SUBMIT payByChequeAmount form
	And I CLICK cropNSave
	Then I am ON dashboard
	
Scenario: To verify the added fund is being reflected in transactions list with DATA ChequePayment_Success
	Given I am ON dashboard
	And I PERFORM storedAmount
	When I CLICK addFund
	Then I am ON payOnline page
	When I CLICK payByCheque link
	Then I am ON payByCheque screen
	And I ENTER payByChequeAmount valid data with DATA ChequePayment_Success
	And I SUBMIT payByChequeAmount form
	And I CLICK cropNSave
	Then I am ON dashboard
	And I CLICK userOption
	When I CLICK transactions
	Then I am ON transactions page
	And I VERIFY newTransaction
	And I PERFORM deleteTransactions
	Then I am ON transactions page

@Group(deleteTest)
Scenario: To verify the transaction request in recent transactions on dashboard with DATA ChequePayment_Success
	Given I am ON dashboard
	And I PERFORM storedAmount
	When I CLICK addFund
	Then I am ON payOnline page
	When I CLICK payByCheque link
	And I ENTER payByChequeAmount valid data with DATA ChequePayment_Success
	And I SUBMIT payByChequeAmount form
	And I CLICK cropNSave
	Then I am ON dashboard
	And I CLICK userOption
	When I CLICK transactions
	Then I am ON transactions page
	And I VERIFY newTransaction
	And I PERFORM updateCashNCapital
	And I PERFORM approveFundRequest
	When I CLICK backToDashboard
	Then I am ON dashboard
	Then I VERIFY recentTransactions
	And I PERFORM deleteTransactions

@Name(AddFundSuccess)
Scenario: To successfully add fund to the application with DATA ChequePayment_Success
	Given I am ON dashboard
	And I PERFORM storedAmount
	Then I am ON dashboard
	When I CLICK addFund
	Then I am ON payOnline page
	When I CLICK payByCheque link
	And I ENTER payByChequeAmount valid data with DATA ChequePayment_Success
	And I SUBMIT payByChequeAmount form
	And I CLICK cropNSave
	Then I am ON dashboard
	And I CLICK userOption
	When I CLICK transactions
	Then I am ON transactions page
	And I VERIFY newTransaction
	And I PERFORM updateCashNCapital
	And I PERFORM approveFundRequest
	When I CLICK backToDashboard
	Then I am ON dashboard
