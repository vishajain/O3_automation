package com.O3.slingshot.parser

import java.util.ArrayList;
import java.util.List;

import groovy.util.logging.Log4j;

import com.O3.slingshot.model.Annotation

@Log4j
class AnnotationParser {

	public List<Annotation> parse (String annotationStr) {
		log.info("Parsing Annotation ${annotationStr}")
		List<Annotation> annotations = new ArrayList<Annotation>();
		for(String anno: getAnnotations(annotationStr)) {
			log.info("Parsing " + anno);
			Annotation annotationObj = parseAnnotation(anno);
			log.info(annotationObj);
			annotations.add(annotationObj)
		}
		return annotations;
	}
	
	
	private static List<String> getAnnotations (String input) {
		List<String> annotations = new ArrayList<String>();
		String temp = input;
		while(temp.indexOf("@") >-1 ) {
			int start = temp.lastIndexOf("@");
			String anno = temp.substring(start);
			temp = temp.substring(0, start);
			if(anno == null) continue;
			anno = anno.trim();
			anno = anno.replaceAll(" +"," ");
			annotations.add(anno);
		}
		return annotations;
	}
	
	private static Annotation parseAnnotation (String inputAnnotation) {
		if(inputAnnotation == null) return null;
		Annotation annotation = new Annotation();
		String temp = inputAnnotation;
		if(temp.startsWith("@")) {
			if(temp.indexOf("(")>-1) {
				String annoName = temp.substring(1,temp.indexOf("("));
				temp = temp.substring(temp.indexOf("("));
				annoName = annoName?.trim();
				annotation.annotationName = annoName;
				
				String values = temp.substring(temp.indexOf("(") + 1);
				if(values.indexOf(")") > -1) values = values.substring(0, values.indexOf(")"));
				String[] valuesArr = values.split(",");
				for(String str : valuesArr) {
					str = str.trim();
					annotation.values.add(str);
				}
			} else {
				annotation.annotationName = temp.substring(1);
			}
		}
		return annotation;
	}
}
