package com.O3.app.pages

import groovy.transform.ToString;

/* Class definition for PageDefEntry */
@ToString
class PageDefEntry {
	String key, name, className;
	public PageDefEntry (String key, String name, String className) {
		this.key = key;
		this.name = name;
		this.className = className;
	}
}