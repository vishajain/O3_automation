package com.O3.model

import java.util.List;


/**
 * @author sandhya
 * Date: 9/27/2015
 */
public class ExceptionOutcome extends Outcome{
	private Exception exception;
	
	public ExceptionOutcome (Exception exception) {
		super(OutcomeStatusType.EXCEPTION);
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
}
