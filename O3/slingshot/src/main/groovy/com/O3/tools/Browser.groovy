package com.O3.tools

import com.gargoylesoftware.htmlunit.ElementNotFoundException

//import com.O3.app.Page
import groovy.transform.Immutable
import org.openqa.selenium.By
import org.openqa.selenium.Capabilities
import org.openqa.selenium.Cookie
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.Keys
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.interactions.HasInputDevices
import org.openqa.selenium.interactions.Mouse
import org.openqa.selenium.internal.Locatable
import org.openqa.selenium.remote.Augmenter
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.OutputType
import org.openqa.selenium.support.ui.Select
import org.apache.commons.io.FileUtils
import org.openqa.selenium.remote.DesiredCapabilities
import org.testng.Assert;
import com.O3.utils.*

import java.text.DateFormat;
import java.text.SimpleDateFormat
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit
import org.openqa.selenium.Alert;
import org.openqa.selenium.UnhandledAlertException

import org.openqa.selenium.JavascriptExecutor

import static java.lang.Thread.sleep as wait

import org.openqa.selenium.safari.SafariOptions;

import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.concurrent.TimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.FluentWait;
//import org.openqa.selenium.Key

/**
 * Created by sandhya on 12/9/13.
 */
public final class Browser {

	//private WebDriver driver

	private final static def DEF_GRID_URL = ""
	private final static def DEF_CAPABILITY = null
	public static final def XPATH = "xpath"

	private static def capabality

	/*
	 private final def capabilities
	 private final def gridUrl
	 private final def props
	 */
	private final def WebDriver driver

	/*public Browser(def capabilities,
	 def gridUrl,
	 def props) {
	 capabality = capabilities.toString()
	 driver = new RemoteWebDriver(new URL(gridUrl),capabilities)	
	 // driver = new ChromeDriver() 
	 if (props.timeout) {
	 driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS)
	 }
	 if (props.maximize) {
	 driver.manage().window().maximize()
	 }
	 }*/	

	public Browser(def browserName, def props) {

		//driver = new RemoteWebDriver(new URL(gridUrl),capabilities)
		if(browserName.equalsIgnoreCase("chrome")){
			driver = new ChromeDriver()
		}else if(browserName.equalsIgnoreCase("firefox")){
			driver = new FirefoxDriver()
		}else if(browserName.equalsIgnoreCase("internetExplorer")){
			driver = new InternetExplorerDriver()
		}

		if (props.timeout) {
			driver.manage().timeouts().pageLoadTimeout(70, TimeUnit.SECONDS)
		}

		if (props.maximize) {
			driver.manage().window().maximize()
		}
	}

	//Open Application Url
	public void openUrl(def url) {
		driver.get(url)
		delay(2000)
	}

	//To get Page title
	public getTitle() {
		def Title = driver.getTitle()
		Title
	}

	//To close the browser
	public void close() {
		driver.quit()
	}

	//To close current window
	public void closeWindow(){
		driver.close()
	}

	//To wait for certain time
	public void delay(def period) {
		wait period
	}

	//To get the element
	public void getLinks(def element){
		driver.get(element)
	}

	// Get URL for the current page
	public getCurrentUrl() {
		return driver.getCurrentUrl()
	}

	//On mouse over method
	public onMouseOver(String mainMenu, String subMenu){
		WebElement ele = getElement(XPATH, mainMenu)
		WebElement menuToClick = getElement(XPATH, subMenu)
		Actions action = new Actions(driver);
		action.moveToElement(ele).build().perform()
		delay(5000)
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", menuToClick);
		delay(6000)
	}

	//To click hidden element
	public void clickHideElement(def element){
		WebElement ele = getElement(XPATH, element)
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", ele);
	}

	/**
	 * To get the tagName of an element
	 * @param element
	 * @return tagName
	 */
	public getTagName(String element) {
		def tagname
		WebElement ele = getElement(XPATH, element)
		if(ele != null){
			tagname = getElement(XPATH, element).tagName
			if(tagname.equals("input")){
				tagname = getElement(XPATH, element).getAttribute("type")
			}
		}else{
			tagname = "NotFound"
		}
		tagname
	}

	/** Takes snapshot
	 * @param filename(String) filename of the snapshot
	 */
	public void takeScreenShot(String filename) {

		File srcFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE)
		try {
			FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir") + "\\Screenshots\\" + filename + System.currentTimeMillis() + ".png"))
		} catch (IOException e) {
			e.printStackTrace()
		}
	}

	//Upload a file
	public uploadFile(def element,def location){
		def currentDirectory = System.getProperty("user.dir").toString();
		def NewDirectory = currentDirectory+location
		println "currentDirectory 2---> "+NewDirectory
		WebElement fileInput = driver.findElement(By.xpath(element));
		fileInput.sendKeys(NewDirectory);
		println "<----uploaded file--->"
	}

	/**
	 * To enter tab
	 * @param element
	 * @return
	 */
	public pressTab(String element){
		WebElement ele = getElement(XPATH, element)
		ele.sendKeys(Keys.TAB)
	}

	//To press ENTER button
	public pressEnter(String element){
		WebElement ele = getElement(XPATH,element)
		ele.sendKeys(Keys.ENTER)
		delay(1000)
	}

	//To click an element
	public def clickElement(WebElement element){
		delay(2000)
		element.click()
	}

	//Get element text
	public def getTexts(WebElement element){
		String text = element.getText()
		text
	}

	//To click checkbox
	public void clickCheckBox(String element, String data){
		println "<---inside clickCheckBox--->"
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		def isSelected = getElement(XPATH, element).isSelected()
		if(data.trim().equals("1") && !isSelected){
			jse.executeScript("arguments[0].click();", getElement(XPATH, element));

		}else{
			if(data.trim().equals("0") && isSelected){
				jse.executeScript("arguments[0].click();", getElement(XPATH, element));
			}
		}
	}

	/**
	 * To Get the selected value from the dropdown
	 * @param browser browser instance
	 * @param element
	 */
	public getDropdownValue(String element){
		def selectedOption
		WebElement option = getElement(XPATH,element)
		if(option != null){
			selectedOption = new Select(option).getFirstSelectedOption().getText()
		}
		selectedOption
	}

	/**
	 * To select from radio button
	 */

	public void selectRadioButton(def element){
		//WebElement ele = getElement(XPATH, element)
		click element
	}

	/**
	 * To Delete Cookies before loging into the Application
	 */
	public void deleteCookies() {
		List <Cookie> cookie = driver.manage().deleteAllCookies()
		System.out.println("Deleted all cookies before login to the application")
	}

	/**
	 * To get current Date and convert to String
	 * @return String
	 */
	public def CurrentDate(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat SDF = new SimpleDateFormat("MM/DD/yyyy");
		def s = SDF.format(calendar.getTime());
		return s
	}

	//To scroll to an element
	public def scrollToElement(def element){
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	//To scroll to an element
	public def scrollToElement2(def element){
		WebElement ele = driver.findElement(By.xpath(element));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
	}

	//scrolldown window
	public void scrollDown(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,250)", "");
	}

	//To Switch to iframe
	public void switchToIframe(def frame){
		driver.switchTo().frame(frame)
	}

	//To get Number of windows
	public void switchTowindow(def winHandle){
		driver.switchTo().window(winHandle);
	}

	//To get Current window
	public def getWindowHandle(){
		driver.getWindowHandle()
	}

	public def getWindows(){
		driver.getWindowHandles()
	}

	public boolean isAlertPresent(){
		try
		{
			driver.switchTo().alert();
			return true;
		}   // try
		catch (Exception e)
		{
			return false;
		}
	}

	public void acceptOrDeclineAlert(String buttonName){
		Alert alert1 = driver.switchTo().alert();
		if(buttonName.equals("Accept")){
			println "accept is chosen."
			alert1.accept();
		}else if(buttonName.equals("Cancel")){
			println "cancel is chosen"
			alert1.decline();
		}
	}


	public List<String> alertMsg(){
		//get text of alert
		Alert alert1 = driver.switchTo().alert();
		String alert = driver.switchTo().alert().getText();
		List<String> alert2 = new ArrayList<String>();
		alert2.add(alert)
		alert1.accept();
		delay(2000)
		alert2
	}

	public String addAlertMsgToList(){
		//get text of alert
		Alert alert1 = driver.switchTo().alert();
		String alert = driver.switchTo().alert().getText();
		println "alert :"+alert
		alert1.accept();
		delay(2000)
		alert
	}

	/**
	 *
	 * @param locator   locator of the element(ie., id/xpath/....)
	 * @param element   element on the page
	 * @return  PageName
	 */
	public String gettext(String element){
		String text
		WebElement Element = getElement(XPATH, element)
		if(Element != null){
			text = Element.getText()
		}
		text
	}

	//To get text based on attribute(EX: id,src, value...)
	public String gettext(String element, String attribute){
		String value
		WebElement Element = getElement(XPATH, element)
		if(Element != null){
			value = Element.getAttribute(attribute)
		}
		value
	}

	//To get text based on attribute(EX: id,src, value...)
	public String gettexts(WebElement element, String attribute){
		String value
		//WebElement Element = getElement(XPATH, element)
		if(element != null){
			value = element.getAttribute(attribute)
		}
		value
	}


	/*
	 *//**
	 * Collect validation messages from error message fields
	 * @param fields array of error message fields
	 * @param locatorType locator for error message fields
	 *//*
	 public def getValidationMessages(def fields){
	 delay(1000)
	 def validationMessages = []
	 for(errMessageFields in fields) {
	 if(getElement(XPATH, errMessageFields) != null){
	 ArrayList<WebElement> validationMsgs = driver.findElements(By.xpath(errMessageFields))
	 for(int i =0;i<=validationMsgs.size()-1;i++){
	 //To capture error message which appears after mousehovering on tool tip
	 String msg = ""
	 if(errMessageFields.contains("error")) {
	 //mousemoveoverAction(validationMsgs.get(i),"Error Message")
	 delay(2000)
	 msg = validationMsgs.get(i).getText()
	 if(msg != null){
	 validationMessages.add(msg)
	 }
	 }else if(errMessageFields.contains("alert alert-danger toast")) {
	 delay(1000)
	 validationMessages.add(validationMsgs.get(i).getText())
	 }
	 }
	 }
	 }
	 println "validationMessages : "+validationMessages
	 return validationMessages
	 }*/

	/**
	 * Collect validation messages from error message fields
	 * @param fields array of error message fields
	 * @param locatorType locator for error message fields
	 */
	public def getValidationMessages(def fields){
		delay(1000)
		def validationMessages = []
		for(errMessageFields in fields) {
			if(getElement(XPATH, errMessageFields) != null){
				ArrayList<WebElement> validationMsgs = driver.findElements(By.xpath(errMessageFields))
				for(int i =0;i<=validationMsgs.size()-1;i++){
					if(validationMsgs.get(i).getText() != null && validationMsgs.get(i).getText() != "") {
						validationMessages.add(validationMsgs.get(i).getText())
					}
				}
			}
		}
		return validationMessages
	}

	/**
	 * Mouse over action
	 * @param WebElement
	 * @param String
	 */
	private void mousemoveoverAction(WebElement element, String appElement ) {
		if(element!=null){
			//new Augmenter().augment(driver)
			Actions builder = new Actions(driver)
			builder.moveToElement(element).build().perform()
			delay(500)
		} else {
			takeScreenShot(appElement+"issue")
			println "Element Not found in the page...."
		}
	}

	/**
	 * Selects from drop down based on value
	 * @param String
	 * @param String
	 */
	public void selectDropdownValue(String element, String value ){

		boolean flag = false
		try{
			WebElement dropDownListBox = getElement(XPATH, element)
			List<WebElement> lists = dropDownListBox.findElements(By.tagName("option"))
			delay(1000)
			for(int i=0; i<= lists.size()-1;i++){
				String dropdownValue = lists.get(i).getText().trim()
				if(value.equals(dropdownValue)){
					flag = true
					break
				}
			}
			if(!flag){
				value = "Select"
			}
			if(dropDownListBox != null){
				Select clickThis = new Select(dropDownListBox)
				clickThis.selectByVisibleText(value)
			}
		}catch (NoSuchElementException e){
			System.out.println("Unable to select list item from the dropdown");
		}
	}

	/**
	 * Selects from drop down based on index
	 * @param String
	 * @param String
	 */
	public void selectDropdownValue(String element){
		WebElement dropDownListBox = getElement(XPATH, element)
		ArrayList<String> npsTerminalValue

		if(dropDownListBox != null){
			npsTerminalValue = dropDownListBox.findElements(By.tagName("option"))
			Select clickThis = new Select(dropDownListBox)
			if(npsTerminalValue.size() > 1){
				clickThis.selectByIndex(1)
			}else{
				clickThis.selectByIndex(0)
			}
		}else{
			println "Element not found....."
			takeScreenShot("ElementNotFound")
		}
	}

	/**
	 * To select through index from a dropdown
	 */
	public void selectOptionFromDropdown(def element, def indexvalue){
		WebElement mySelectElm = getElement(XPATH, element)
		Select mySelect= new Select(mySelectElm);
		mySelect.selectByIndex(indexvalue);
	}

	//To get the list based on the tagname
	public def getListByTagName(String element, String tag){
		WebElement dropDownListBox = getElement(XPATH, element)
		List<WebElement> lists = dropDownListBox.findElements(By.tagName(tag))
		lists
	}

	//Get attribute name
	public String getAttribute(def element){
		String attributeValue = getElement(XPATH, element).getAttribute("value")
		return attributeValue
	}

	//To get all Links on the page
	public def getAllLinks(){
		delay(2000)
		def links = []
		def allLinks = driver.findElements(By.tagName("a"))

		for (WebElement myElement : allLinks){
			if(myElement.getText() != "")
				links.add(myElement.getText().trim())
		}
		links
	}

	//To verify element is enabled
	public checkEnabled(String element){
		def Element = getElement(XPATH, element)
		def isEnabled
		if(Element != null){
			isEnabled = Element.isEnabled()
		}
		isEnabled
	}

	/*//To verify if field is read only
	 public String isReadOnly(String element){
	 println "<---inside read only()--->"
	 // Element = getElement(XPATH, element)
	 WebElement elem = driver.findElement(By.xpath(element));
	 String isReadOnly
	 if(elem != null){
	 isReadOnly = elem.getAttribute("readonly");
	 }
	 println "isReadOnly--> "+isReadOnly
	 isReadOnly
	 }*/

	//To verify element is displayed
	public isDisplayed(String element){
		def Element = getElement(XPATH, element)
		def isDisplayed
		if(Element != null){
			isDisplayed = Element.isDisplayed()
		}
		isDisplayed
	}

	//To get the text of the lists
	public def getLists(String element){
		def list = []
		List<WebElement> lists = driver.findElements(By.xpath(element))
		for(int i=0;i<lists.size();i++){
			String text = lists.get(i).getText()
			list.add(text)
		}
		list
	}

	//To get the attribute values
	public def getLists(String element, String value){
		def list = []
		List<WebElement> lists = driver.findElements(By.xpath(element))
		for(int i=0;i<lists.size();i++){
			String text = lists.get(i).getAttribute(value)
			list.add(text)
		}
		list
	}

	//To get all elements from the list
	public def getListElements(String element){
		List<WebElement> elementList = driver.findElements(By.xpath(element))
		elementList
	}

	//To navigate back to original page
	public void navigateBack(){
		driver.navigate().back()
	}

	//Get current month
	public getCurrentMonth(){
		Calendar c = Calendar.getInstance();
		String month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		return month
	}

	//To click element
	public void click(element) {
		click(XPATH, element)
	}

	@Override
	public void click(String locator,String element) {
		clickAction(getElement(locator,element), element)
	}
	private void clickAction(WebElement element, String appElement ) {
		try {
			if(element!=null){
				Actions selAction = new Actions(driver)
				selAction.click(element).perform()
				driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS)
			} else {
				String message="Element not found in the  : "
				String show= appElement +" "+message + " " + " page "
			}
		} catch(ElementNotFoundException e){
			println "Unbale to click on the button"
		}
	}

	//To enter data
	public def populateField(element, value) {
		println """$element $value"""
		populateField(XPATH, element, value)
	}

	def populateField(def locator,def element, def value){
		try {
			this.inputField(this.getElement(locator,element), element,value)
		} catch(ElementNotFoundException e){
			System.out.println("Input element not found for element is" + element)
		}
	}


	//TODO Have to move the element check to page level
	def inputField(def element, def appElement,def value) {
		if(element!=null){
			WebElement field=element
			if((field.isDisplayed())& (field.isEnabled()))	{
				field.click()
				delay(500)
				field.clear()
				field.sendKeys(value)
			}
		}else{
			String message="Input Element not found in the  : "
			String show= appElement +" "+message + " " + " page "
			System.out.println(show)
		}
	}

	//To check element is present or not
	def WebElement getElement(def locator,def element ){
		By byElement
		WebElement query
		switch(locator){
			case "xpath":    			byElement = By.xpath(element)
				break
			case "id":	 				byElement = By.id(element)
				break
			case "name": 				byElement = By.name(element)
				break
			case "classname":			byElement = By.className(element)
				break
			case "linkname":			byElement = By.linkText(element)
				break
			case "paritallinkname":	 	byElement = By.partialLinkText(element)
				break
			case "tagname":		 		byElement = By.tagName(element)
				break
			case "css":		      		byElement = By.cssSelector(element)
				break
			default:		 	  		throw new RuntimeException("Invalid locator")
		}
		try {
			//driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS)
			query = driver.findElement(byElement)
			return query
		}catch(ElementNotFoundException e){
			return query = null
		}catch(NoSuchElementException e){
			return query = null
		}
	}

	def fluentWait(String element, int timeOut) throws NullPointerException{
		println "inside fluentWait() in browser::"
//		WebDriverWait wait = new WebDriverWait(driver, timeOut);

//		println "<<<-----11----->>"
//		FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(driver).withTimeout(timeOut, TimeUnit.SECONDS).pollingEvery(pollingtime, TimeUnit.SECONDS).ignoring(NoSuchElementException.class, TimeoutException.class);

		/*FluentWait fluentWait = new FluentWait<>(driver) {
			.withTimeout(30, TimeUnit.SECONDS)
			.pollingEvery(200, TimeUnit.MILLISECONDS);
		}*/
		
		
		FluentWait fWait = new FluentWait(driver)
		.withTimeout(timeOut, TimeUnit.SECONDS)
		.pollingEvery(200, TimeUnit.MILLISECONDS)
		.ignoring(NullPointerException.class);
		
		try{
			fWait.until(ExpectedConditions.visibilityOf(getElement(XPATH, element)));
		}catch(Exception e){
			System.out.println("Element Not found trying again");
			e.printStackTrace();
		}
		
		println "<<-----22----->>"
		
		
//		wait.until(ExpectedConditions.visibilityOfElementLocated(getElement(XPATH, element)));
//		println "<<-----22----->>"
		/*WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(getElement(XPATH, element)));
		println "webElement:::; "+webElement
		return webElement*/
	}

}
