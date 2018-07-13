package com.O3.slingshot.commands

import groovy.transform.ToString;

@ToString
class CommandData {

    def dataMap = [:]

    def put(def key, def value) {
        dataMap.put(key, value)
    }

    def get(def key) {
        dataMap.get(key)
    }
}
