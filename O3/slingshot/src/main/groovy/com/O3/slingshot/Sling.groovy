package com.O3.slingshot

import com.O3.slingshot.hook.ShutdownHook
import com.O3.slingshot.spring.SpringContextManager
import groovy.util.logging.Log4j

@Log4j
class Sling {

    public static void main(String[] args) {
        log.info "Started executing the specs"

        // initialize spring context
        SpringContextManager.init()

        // add shutdown hook in case some one kills the execution
        ShutdownHook.addShutdownHook(new ShutdownHandler())

        // start execution of the features
        //SpringContextManager.getBean('featureProcessor').process()
		
		def featureProcessor = SpringContextManager.getBean('featureProcessor')
		featureProcessor.scanFeatures();
		featureProcessor.process();
		
        log.info "Completed executing the specs"
		featureProcessor.close();
    }

    static class ShutdownHandler extends Thread {

        public void run() {
            log.info "Shutting down the test execution started"

            // #TODO add some clean up code here

            log.info "Shutting down the text execution completed"
        }

    }

}
