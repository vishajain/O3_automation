Feature: To test the succcessful and unsuccessful scenarios of Redeem Fund
  As an User of the application
  I want to test Redeem Fund functionality

  Background: Login: SuccessLogin, Addfund: AddFundSuccess

  Scenario: To verify redeem fund functionality by clicking on Redeem Fund button
    Given I am ON dashboard
    When I CLICK redeemFund button
    Then I am ON redeemFunds screen
    When I CLICK cross button
    Then I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I VERIFY newTransaction
    And I PERFORM deleteTransactions
    Then I am ON transactions

  Scenario: To verify redeem amount from cash by giving invalid data with DATA Redeem_Failure
    Given I am ON dashboard
    And I PERFORM storedAmount
    When I CLICK redeemFund button
    Then I am ON redeemFunds screen
    And I ENTER invalid data
    And I SUBMIT form
    Then I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I PERFORM deleteTransactions
    Then I am ON transactions

@Group(RedeemFund)
  Scenario: To redeem amount from cash by giving valid data with DATA Redeem_Success
    Given I am ON dashboard
    When I CLICK redeemFund button
    Then I am ON redeemFunds screen
    And I ENTER valid data
    And I SUBMIT form
    Then I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I VERIFY newTransaction
    And I PERFORM deleteTransactions
    Then I am ON transactions

@Group(RedeemFund)
  Scenario: To verify the redeem fund request in transactions page with DATA Redeem_Success
    Given I am ON dashboard
    When I CLICK redeemFund button
    Then I am ON redeemFunds screen
    And I ENTER valid data with DATA Redeem_Success
    And I SUBMIT form
    Then I am ON dashboard
    And I CLICK userOption
    When I CLICK transactions
    Then I am ON transactions page
    And I VERIFY newTransaction
    And I PERFORM updateCashNCapital
    And I PERFORM approveFundRequest
    When I CLICK backToDashboard
    Then I am ON dashboard
    And I VERIFY amountUpdated
    And I PERFORM deleteTransactions
    Then I am ON dashboard
    

