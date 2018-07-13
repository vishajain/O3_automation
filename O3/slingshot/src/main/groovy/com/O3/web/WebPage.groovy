package com.O3.web

/**
 * Created by sandhya on 12/10/13.
 */
class WebPage extends WebBase {
    def pageData = [:]
	def pageKey;
	
	
	def populateData = {browser, formKey, formData ->
		println("Default populateData in WebPage")
	}
	
	/*
	 * Default submit(..) method
	 * Sub-classes need to override this to receive submit actions
	 */
	def submit = {browser, formKey, formData ->
		println ("Default submit method in WebPage")
	}
}
