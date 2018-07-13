package com.O3.web

/**
 * Created by sandhya on 12/10/13.
 */
class WebBase {
    def dom = [:]

    def click(def adapter, def element) {
        adapter.click(dom[element])
    }
}
