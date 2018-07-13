package com.O3.model

import java.util.List;


/**
 * @author sandhya
 * Date: 9/27/2015
 */
public class FailureOutcome extends Outcome{
	private String failureMessage;

	public FailureOutcome(String failureMessage) {
		super(OutcomeStatusType.FAILURE);
		this.setFailureMessage(failureMessage);
	}
	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
}
