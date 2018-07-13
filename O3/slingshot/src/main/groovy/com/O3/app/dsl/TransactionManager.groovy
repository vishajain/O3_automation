package com.O3.app.dsl

import org.codehaus.groovy.control.customizers.ImportCustomizer.Import;

import com.O3.app.pages.funds.TransactionPage
import com.O3.app.pages.funds.AddFundPage
import com.O3.app.pages.user.DashboardPage

/**
 * Created by visha on 29/05/2018
 */
class TransactionManager {

	
	//To verify the added fund in transaction list
	def newTransaction = { browser, formData ->
		TransactionPage.newTransaction browser, formData
	}
	
	//To approve the added fund request through backend
	def approveFundRequest = { browser, formData ->
		AddFundPage.approveFundRequest browser, formData
	}
	
	/*//To verify the added fund on dashboard
	def updatedFund = { browser, formData ->
		TransactionPage.updatedFund browser, formData
	}*/
	
	//To get error message incase of zero net fund amount
	def deleteTransactions = { browser, formData ->
		println "::inside deleteTransactions of transacation manager::"
		AddFundPage.deleteTransactions browser, formData
	}
	//To append the added amount to the previous amount
	def updateCashNCapital = { browser, formData ->
		println "<--inside updateCashNCapital() of manager-->"
		TransactionPage.updateCashNCapital browser, formData
	}
	
	//To verify the recent transaction on dashboard
	def recentTransactions = { browser, formData ->
		TransactionPage.recentTransactions browser, formData
	}
	
	//To verify the updated transaction after adding amount
	def amountUpdated = { browser, formData ->
		DashboardPage.amountUpdated browser, formData
	}
	
	//To store the cash and Capital amount in Dashboard
	def storedAmount = {browser, formData ->
		DashboardPage.storedAmount browser, formData
	}
	
	//To update the cash and capital after the transactions from portfolio
	def updateAmount = {browser, formData ->
		DashboardPage.updateAmount browser, formData
	}
	
	//To filter the transactions in Transaction page
	def filterTransactions = {browser, formData ->
		TransactionPage.filterTransactions browser, formData
	}
	
	//To update status of fund request
	def updateStatus = {browser, formData ->
		TransactionPage.updateStatus browser, formData
	}
		
	//To sort transactions based on amount
	def amountBasedSort = {browser, formData ->
		TransactionPage.amountBasedSort browser, formData
	}
	
	//To verify pagination on transaction page
	def pagination = {browser, formData ->
		TransactionPage.pagination browser, formData
	}
	
	//To verify reset button functionality on transactions page
	def verifyReset = {browser, formData ->
		TransactionPage.verifyReset browser, formData
	}
}
