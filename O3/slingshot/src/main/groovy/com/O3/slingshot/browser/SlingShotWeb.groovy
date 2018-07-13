package com.O3.slingshot.browser

import groovy.util.logging.Log4j;

import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities

import org.openqa.selenium.safari.SafariOptions
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.UnexpectedAlertBehaviour
import org.openqa.selenium.remote.CapabilityType

@Log4j
class SlingShotWeb {

    def browser
	
	def browserName
	
	def appUrl
	
    public SlingShotWeb() {
		
        def props = new Properties()
        new File("configuration/Application_config.properties").withInputStream { stream ->
            props.load(stream)
        }
		
		browserName = "$props.browserName"
		//For running on local machine
		if(browserName.equalsIgnoreCase("chrome")){
			String currentDirectory = System.getProperty("user.dir")
			String driverPath = currentDirectory.split("slingshot").first()+"\\Drivers\\chromedriver.exe"
			System.setProperty("webdriver.chrome.driver", driverPath);
			
			//System.setProperty("webdriver.chrome.driver", "/O3/Drivers/chromedriver.exe");			
		}else if(browserName.equalsIgnoreCase("internetExplorer")){
			System.setProperty("webdriver.ie.driver", "/O3/Drivers/InternetExplorerDriver.exe");		
		}
		browser = new com.O3.tools.Browser(browserName, [maximize: true])		
		
		appUrl = "$props.APP_URL"
    }
	
	public initialize() {
		browser.openUrl(appUrl)
	}
	
	public reset() {
		log.info("Resetting browser to " + appUrl);
		browser.deleteCookies()
		browser.openUrl(appUrl)
	}	
	
	// To take screenshot
	public takeScreenShot(fileName){
		browser.takeScreenShot(fileName)
	}
	//To close browser
	public close(){
		log.info("Closing the browser")
		browser.close()
	}

}
