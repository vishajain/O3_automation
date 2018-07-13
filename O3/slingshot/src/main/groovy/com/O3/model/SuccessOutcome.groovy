package com.O3.model

import groovy.transform.ToString;

import java.util.List;


/**
 * @author sandhya
 * Date: 9/27/2015
 */
@ToString
public  class SuccessOutcome extends Outcome{
	public SuccessOutcome() {
		super(OutcomeStatusType.SUCCESS);
	}
}
