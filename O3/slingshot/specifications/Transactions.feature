Feature: To test successful and unsuccessful scenarios of transaction
  As a User of the application
  I want to test Transactions functionality

  Background: Login:loginSuccess,Addfund:FundSuccess

  Scenario: To verify deposit filter in transaction page with DATA UpdateStatus_Success
    Given I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I VERIFY newTransaction
    And I PERFORM updateStatus with DATA UpdateStatus_Success
    When I CLICK backToDashboard
    Then I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I ENTER data with DATA DepositFilter_Success
    And I PERFORM filterTransactions
    Then I am ON transactions page

  @Group(KRA)
  Scenario: To verify withdrawal filter in transaction page with DATA ChequeWithdrawl_Success
    Given I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I VERIFY newTransaction
    And I PERFORM updateCashNCapital with DATA ChequeWithdrawl_Success
    And I PERFORM approveFundRequest with DATA ChequeWithdrawl_Success
    When I CLICK backToDashboard
    Then I am ON dashboard
    When I CLICK redeemFund button
    Then I am ON redeemFunds screen
    And I ENTER valid data with DATA RedeemForWithdrawl_Success
    And I SUBMIT form
    Then I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I VERIFY newTransaction
    And I PERFORM updateStatus with DATA UpdateStatus_Success
    When I CLICK backToDashboard
    Then I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I ENTER data with DATA WithDrawalFilter_Success
    And I PERFORM filterTransactions
    Then I am ON transactions page

  Scenario: To verify reset button functionality on transaction page with DATA DepositFilter_Success
    Given I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I ENTER  valid data with DATA DepositFilter_Success
    And I CLICK reset button
    When I PERFORM verifyReset
    Then I am ON transactions page

  Scenario: To verify pagination on transactions page
    Given I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I PERFORM pagination
    Then I am ON transactions page

  Scenario: To verify filter based on amount parameter
    Given I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I PERFORM amountBasedSort
    Then I am ON transactions page
    And I PERFORM deleteTransactions
    Then I am ON transactions page
    And I PERFORM logout action
    Then I am ON login page
