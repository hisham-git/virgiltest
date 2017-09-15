package mt.siteportal.utils.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import mt.siteportal.controls.Calendar;
import mt.siteportal.controls.Control;
import mt.siteportal.controls.Dropdown;
import mt.siteportal.controls.Input;
import mt.siteportal.controls.TextArea;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
//import ru.yandex.qatools.htmlelements.element.Button;
/**
 * Helper class, a wrapper for underlying library(ies) calls. Provides unified approach to accessing UI components.
 */
public class UiHelper {
    private static final Logger logger = LoggerFactory.getLogger(UiHelper.class);

    //TODO: Make configurable.
    protected static final int WAITFOR_TIMEOUT = 30; // seconds

    private static final int DEFAULT_SLEEP_TIMEOUT = 5000; // milliseconds
    /**
    * Gets element attribute's value if the element can be found.
    *
    * @param by - element-to-find-and-get-attribute-value-from locator.
    * @param attributeName - attribute-to-use name.
    *
    * @return WebElement's attribute value.
    */
    
   public static String getAttributeValue(WebElement element, String attributeName) {
       if (null != element){
           return element.getAttribute(attributeName);
       }

       logger.error(String.format("Element(%s) Not Found, cannot get its attribute(%s) value.", element, attributeName));

       return "{NOT FOUND, NOTHING TO GET, SEE THE LOG.}";
   }
    /**
     * Pauses test execution for specified amount of time.
     *
     * @param milliseconds time to sleep, in milliseconds
     */
    public static void sleep(int milliseconds) {
        try {
            logger.debug("sleep() for" + milliseconds);
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.error("---> Thread terminated during sleep.", e);
        }
    }

    public static void clear(By by){
        findElement(by).clear();
    }
    
    /**
    * Gets element's text if the element can be found.
    *
    * @param by - element-to-find-and-derive-text-from locator.
    *
    * @return WebElement's text.
    */
   public static String getText(By by) {
       WebElement el = findElement(by);
       if (null != el){
           return el.getText();
       }

       logger.error(String.format("Element(%s) Not Found, cannot get its text.", by));

       return "{NOT FOUND, NOTHING TO GET, SEE THE LOG.}";
   }
   
   /**
    * Gets element's text if the element can be found.
    *
    * @param by - element-to-find-and-derive-text-from locator.
    *
    * @return WebElement's text.
    */
   public static String getText( WebElement el) {
       if (null != el){
           return el.getText().trim();
       }

       logger.error(String.format("Element(%s) Not Found, cannot get its text.", el));

       return "{NOT FOUND, NOTHING TO GET, SEE THE LOG.}";
   }

    /**
     * Sleep for default time specified in DEFAULT_SLEEP_TIMEOUT constant
     */
    public static void sleep() {
        sleep(DEFAULT_SLEEP_TIMEOUT);
    }

    /**
     * Waits for an element .
     * //TODO: Provide waitFor(By by, long timeout)
     *
     * @param by element's locator.
     * @return indicates if an element is found
     */
    public static boolean waitFor(By by) {
        logger.debug("waitFor() " + by);
        for (int sec = 0;sec<= WAITFOR_TIMEOUT; sec++){
        	if (isPresent(by)){
        		return true;        		
        	}
        	else{
	        	sleep();
	        	sec++;
        	}
        }
        return false;
    }
    
    /**
     * Waits for an element .
     *
     * @param element.
     * @return indicates if an element is found
     */
    public static boolean waitFor(WebElement element) {
        logger.debug("waitFor() " + element);
        for (int sec = 0;sec<= WAITFOR_TIMEOUT; sec++){
        	if (isPresent(element)){
        		return true;        		
        	}
        	else{
	        	sleep();
	        	sec++;
        	}
        }
        return false;
    }
    /**
     * Waits when an element disappeared.
     * 
     * @param by element's locator.
     * @return indicates if an element is not displayed more
     */
    
    public static boolean waitForElementDisappeared(By by){
    	waitFor(by);
    	int count = 0; 
    	while (true == isPresent(findElement(by))){
    		sleep();
    		count++;
    		if (count == WAITFOR_TIMEOUT){
    			Log.logError("The element hungs up and is not dissapeared");
    			break;
    		}
    		}
    	return true;
    	
    }
    /**
     * Yet another "waitFor". Just an example of own ExpectedCondition.
     *
     * @param xpath	XPATH that distinguish an element
     * @return indicates if an element has been found.
     */
    public static boolean waitFor(final String xpath) {
        logger.debug("waitFor() " + xpath);

        return (new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
                @Override
				public Boolean apply(WebDriver d) {
                    WebElement element = Browser.getDriver().findElement(By.xpath(xpath));
                    return element.isDisplayed();
                }
        });
    }
    
   
    /**
     * Simple wrapper around driver.findElement().
     * Simplifies logging and extending.
     *
     * @param by - element-to-find locator.
     *
     * @return WebElement found.
     */
	public static WebElement findElement(By by) {
		logger.debug("findElement() By: " + by.toString());
		try {
			return Browser.getDriver().findElement(by);
		} catch (NoSuchElementException nse) {
			return null;
		}
	}
    
    /**
     * Simple wrapper around driver.findElements().
     * Simplifies logging and extending.
     *
     * @param by - element-to-find locator.
     *
     * @return List of WebElements found.
     */
    public static List<WebElement> findElements(By by) {
    	logger.debug("findElement() By: " + by.toString());

    	return Browser.getDriver().findElements(by);
    }
    
    
    public static Control getControl(WebElement element){
    	
    	if(element.findElements(new By.ByXPath(Control.DROPDOWNXPATH)).size()!=0){
    		System.out.println("drop");
    		return new Dropdown(element);
    	}
    	else if(element.findElements(new By.ByXPath(Control.INPUTXPATH)).size()!=0){
    		System.out.println("input");
    		return new Input(element);
    	}
    	else if(element.findElements(new By.ByXPath(Control.TEXTAREAXPATH)).size()!=0){
    		System.out.println("textearea");
    		return new TextArea(element);
    	}
    	else if(element.findElements(new By.ByXPath(Control.CALENDARXPATH)).size()!=0){
    		System.out.println("calendar");
    		return new Calendar(element);
    	}
    	else {
    		throw new NoSuchElementException("The no one control was not found"); 
    	}
    	
    }
    
    /**
     * Simple wrapper around driver.findElement().
     * Simplifies logging and extending.
     *
     * @param by - xpath to-find locator.
     *
     * @return WebElement found.
     */
    public static WebElement findElementByXpath(String xpath) {
        logger.debug("findElement() ByXpath: " + xpath);

        return Browser.getDriver().findElement(new By.ByXPath(xpath));
    }

    
    public static String getCurrentUrl() {
        String url = Browser.getDriver().getCurrentUrl(); 
    	logger.debug("the url is: "+url);
        return url;
    }
    
    /**
     * Finds element by the locator specified and left-mouse clicks it.
     *
     * @param by - element's locator.
     */
	public static void click(By by) {
		logger.debug("click() element By locator: [" + by.toString() + "]");
		WebDriverWait wait = new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
			WebElement element = findElement(by);
			element.click();
		} catch (WebDriverException e) {
			Log.logException("Element not found or ready for clicking", e);
		}
		checkPendingRequests(Browser.getDriver());
	}
	
	/**
     * Tries to raise submit on the element.
     * @param element - the target element.
     */
	public static void click(WebElement element) {
		logger.debug("click() element By WebElement: [" + element + "]");
		WebDriverWait wait = new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			element.click();
		} catch (WebDriverException e) {
			Log.logWarning("Element not found or ready for clicking");
		}
		checkPendingRequests(Browser.getDriver());
	}

	public static void clickAndWait(WebElement element) {
		logger.debug("Clicking " + element);
		WebDriverWait wait = new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT);
		WebElement element1 = wait.until(ExpectedConditions.elementToBeClickable(element));
		element1.click();
		checkPendingRequests(Browser.getDriver());
	}

    public static void clickAndWait(By by) {
        WebElement element = findElement(by);
        //Wait until the element is clickable with Timeout=10sec
        WebDriverWait wait = new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(by));
        element.click();
        checkPendingRequests(Browser.getDriver());
    }

    /**
     * Left-clicks in the middle of the given element and holds mouse button down.
     *
     * @param by - element's locator.
     */
    public static void clickAndHold(By by) {
        WebElement element = findElement(by);

        logger.debug("Clicking and holding element By: " + by.toString());
        getWebDriverActions().clickAndHold(element).perform();
    }

    /**
     * Checks whether element specified by the locator is present on current page.
     *
     * @param by - locator of the element.
     *
     * @return True if the element present on the page.
     */
	public static boolean isPresent(By by) {
		/*
		 * logger.debug("isPresent() By: " + by.toString());
		 * 
		 * return Browser.getDriver().findElements(by).size() > 0;
		 */
		WebElement element = findElement(by);
		if (null != element)
			return element.isDisplayed();
		return false;
	}
    
    /**
     * Checks whether element specified by the element is present on current page.
     *
     * @param element - element.
     *
     * @return True if the element present on the page.
     */
	public static boolean isPresent(WebElement element) {
		try {
			if (element == null)
				System.out.println("ELEMENT WAS NULL");
			logger.debug("isPresent() By: " + element.toString());
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

    /**
     * Sends keys to the element specified.
     *
     * @param element - target element.
     * @param value string to be typed.
     */
    public static void sendKeys(WebElement element, CharSequence value) {
        logger.debug(String.format("Sending Keys '%s' to %s ",value.toString(), element.toString()));
        element.sendKeys(value);
        checkPendingRequests(Browser.getDriver());
    }

    /**
     * Finds and sends keys to the element specified by locator.
     *
     * @param by - target element's locator.
     * @param value string to be typed.
     */
    public static void sendKeys(By by, CharSequence value) {
        sendKeys(findElement(by), value);
    }

    public static void selectInDropdownBtn (WebElement dropdown, String value) {
        logger.debug("Selecting in dropdown " + value);
        click(dropdown);
        click(new By.ByXPath("//span[text()='  "+value+"']/.."));
    }
    
    public static void selectInDropdownBtn (By dropdown, String value) {
        logger.debug("Selecting in dropdown " + value);
        click(dropdown);
        click(new By.ByXPath("//span[text()='  "+value+"']/.."));
    }
  
 
    /**
     * Switches to another tab of browser.
     */
    public static void switchToTab(WebDriver driver) {
    	logger.debug("Switching to another opened tab");
    	ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
    	int count = tabs.size();
    	try{
    	driver.switchTo().window(tabs.get(count-1));
    	}
    	catch(IndexOutOfBoundsException e){
    		logger.error("Only one tab is opened in current browser");
    	}
    	checkPendingRequests(Browser.getDriver());
    	
    }
    
    
    /**
     * Get number of active browser windows.
     */
    public static int getActiveWindows(WebDriver driver) {
    	logger.debug("Getting active windows");
    	ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
    	int count = tabs.size();
    	return count;
    	
    }
    public static void switchBack(WebDriver driver) {
    	logger.debug("Switching to previous tab");
    	ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
    	driver.switchTo().window(tabs.get(0));
    	checkPendingRequests(Browser.getDriver());
    }
    //=====================================================================
    /**
     * Gets the actions for the active driver/</br>
     * Note: "Actions" is WebDriver specific thing, so this could break upon switching from WebDriver to something else.
     *
     * @return   WebDriver's Actions.
     */
    private static Actions getWebDriverActions() {
        return new Actions(Browser.getDriver());
    }

    public static void checkPendingRequests(WebDriver driver) {
        int timeoutInSeconds =  WAITFOR_TIMEOUT;
        if (driver instanceof JavascriptExecutor) {
		    JavascriptExecutor jsDriver = (JavascriptExecutor)driver;

		    for (int i = 0; i< timeoutInSeconds; i++) 
		    {
		        Object numberOfAjaxConnections = jsDriver.executeScript("return window.openHTTPs");
		        // return should be a number
		        if (numberOfAjaxConnections instanceof Long) {
		            Long n = (Long)numberOfAjaxConnections;
		            if (n.longValue() == 0L)  break;
		        } else{
		            //If its not a number page might have been loaded freshly indicating the monkey
		            //path is replased or we havent yet done the patch.
		            monkeyPatchXMLHttpRequest(driver);
		        }
		        sleep(500);
		    }
		    sleep(500);
		}
		else {
		   System.out.println("Web driver: " + driver + " cannot execute javascript");
		}    
       
    }
    
    public static void monkeyPatchXMLHttpRequest(WebDriver driver) {
        try {
            if (driver instanceof JavascriptExecutor) {
                JavascriptExecutor jsDriver = (JavascriptExecutor)driver;
                Object numberOfAjaxConnections = jsDriver.executeScript("return window.openHTTPs");
                if (numberOfAjaxConnections instanceof Long) {
                    return;
                }
                String script = "  (function() {" +
                    "var oldOpen = XMLHttpRequest.prototype.open;" +
                    "window.openHTTPs = 0;" +
                    "XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {" +
                    "window.openHTTPs++;" +
                    "this.addEventListener('readystatechange', function() {" +
                    "if(this.readyState == 4) {" +
                    "window.openHTTPs--;" +
                    "}" +
                    "}, false);" +
                    "oldOpen.call(this, method, url, async, user, pass);" +
                    "}" +
                    "})();";
                jsDriver.executeScript(script);
            }
            else {
               System.out.println("Web driver: " + driver + " cannot execute javascript");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
  //----------------------------NZ TEAM ------------------------------------------------------------
    /**
     * Simply returns the number of Ajax Requests currently available, does not wait for the Ajax Requests to
     * end.
     * 
     * @param driver
     * @return Long the number of Ajax Connections
     */
	public static Long getNumberOfOpenAjaxConnections(WebDriver driver) {
		if (driver instanceof JavascriptExecutor) {
			JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
			for (int tries = 1; tries <= 3; tries++) {
				Object numberOfAjaxConnections = jsDriver.executeScript("return window.openHTTPs");
				// return should be a number
				if (numberOfAjaxConnections instanceof Long) {
					Long n = (Long) numberOfAjaxConnections;
//					Log.logInfo("Number of Open AJAX connections : " + n);
					return n;
				} else {
					monkeyPatchXMLHttpRequest(driver);
				}
			}
		} else {
			Log.logInfo("Web driver: " + driver + " cannot execute javascript");
		}
		return -1L;
	}
    
    /**
     * Simple check that the UI Spinner (the Web Element that tell users that the page is loading)
     * is visible or not.
     * 		
     * @param driver
     * @return boolean true if the Spinner is visible, false otherwise
     */
    public static boolean checkSpinner(WebDriver driver){
    	try{
    		return driver.findElement(By.cssSelector(".row.left-pane>div.spinner")).isDisplayed();
    	}catch(NoSuchElementException ne){
    		return false;
    	}catch(StaleElementReferenceException se){
    		//If the WebDriver throws this error, it means that the element was first found and then lost, instead of
    		//never having existed in the first place. So, the spinner did exist, which should return true.
    		Log.logStep("Spinner...");
    		return true;
    	}
    }
    /**
     * Same purpose as the UiHelper.checkPendindRequests but with a lot less sleep()
     * @param driver
     */
    public static void fastWait(WebDriver driver){
    	while(getNumberOfOpenAjaxConnections(driver) != 0){
			sleep(100);
		}
    }
    
    /**
     * Waits Until the given Web Element is Visible
     * 
     * @param WebElement the element waiting for
     */
    public static void waitForVisibility(WebElement element){
    	int timeOutInSeconds = WAITFOR_TIMEOUT;
    	int seconds = 0;
    	while(!element.isDisplayed() && seconds <= timeOutInSeconds){
    		sleep();
    		seconds++;
    	}
    }
    
    /**
     * Wait for a  WebElement to become visible
     * @param id
     */
	public static boolean waitForVisibility(By id) {
		WebDriverWait wait = new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(id));
		} catch (TimeoutException te) {
			return false;
		}
		return true;
	}
	
	/**
     * Wait for a  WebElement to become visible
     * @param id
     */
	public static boolean waitForVisibility(By id, int timeout) {
		int sec = 0;
		while (sec <= timeout) {
			if (isPresentAndVisible(id))
				return true;
			sec += 1;
			sleep();
		}
		return false;
	}
	
	/**
	 * Checks for both presence and visibility
	 * @param by By its locator
	 * 
	 * @return boolean true if present and visible, false otherwise
	 */
	public static boolean isPresentAndVisible(By by){
		return (isPresent(by) && isVisible(by));
	}
	
	/**
	 * Checks for both presence and visibility
	 * @param webElement WebElement its locator
	 * 
	 * @return boolean true if present and visible, false otherwise
	 */
	public static boolean isPresentAndVisible(WebElement webElement){
		return (isPresent(webElement) && isVisible(webElement));
	}
	
	/**
	 * Checks if a WebElement is visible through its locator
	 * 
	 * @param by By its locator
	 * @return boolean - true if displayed, false otherwise
	 */
	public static boolean isVisible(By by){
		try{
			return Browser.getDriver().findElement(by).isDisplayed();
		}catch(NoSuchElementException nse){
			return false;
		}
	}
	
	/**
	 * Checks if a WebElement is visible through its locator
	 * 
	 * @param webElement WebElement its locator
	 * @return boolean - true if displayed, false otherwise
	 */
	public static boolean isVisible(WebElement webElement){
		try{
			return webElement.isDisplayed();
		}catch(NoSuchElementException nse){
			return false;
		}
	}
	
	/**
	 * Wait until Spinner stops
	 */
	public static void waitForSpinnerEnd(WebDriver driver) {
		int timeout = WAITFOR_TIMEOUT, sec = 0;
		while (sec < timeout && checkSpinner(driver)) {
			sleep();
			sec++;
		}
	}
	
	/**
	 * @author HISHAM
	 * Fluentwait implementation for Spinner end verification
	 * It searches repeatedly until spinner ends
	 * 
	 * a). If found returns true
	 * b). if not found, again searches till initialWait
	 *  
	 * @param int initialWait 
	 * 			- initial wait for searching spinner end 
	 */
	public static boolean fluentWaitForSpinnerEnd(int initialWait) {
		By spinner = By.cssSelector(".row.left-pane>div.spinner");
		while (null != fluentWaitForElementVisibility(spinner, initialWait)) {
			Log.logInfo("Spinner still spinning...");
		}
		Log.logInfo("Spinner ends");
		return true;
	}
	/**
	 * Returns a random String, mostly used to generate random Queries
	 * 
	 * @return String random UUID
	 */
	public static String generateRandonUUIDString() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Wait until Spinner stops by WebElement
	 */
	public static void waitForSpinnerEnd(WebElement spinner, int timeout){
    	try{
    		int sec = 0; 
    		while(sec<timeout && spinner.isDisplayed()){
    			sleep();
    			sec++;
    		}
    	}catch(NoSuchElementException ne){
    		Log.logInfo("No Spinner found.");
    	}
	}
	
	/**
	 * @author Hisham
	 * 
	 *         Description: Explicitly wait using Selenium ExpectedConditions
	 *         until Spinner stops
	 * 
	 * @param spinner
	 *            -Spinner WebElement
	 * @param timeout
	 *            -Time period before throwing an exception
	 */
	public static void explicitWaitForSpinnerEnd(By spinner, int timeout) {
		try {
			Log.logInfo("Spinner started...");
			WebDriverWait exWait = new WebDriverWait(Browser.getDriver(), timeout, 500);
			exWait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
			Log.logInfo("Spinner stops...");
		} catch (TimeoutException spinnerTimeOut) {
			Log.logException("Spinner timeout exceeded", spinnerTimeOut);
		}
	}

	/**
	 * Checks if an AJAX call was made or not within the next half second
	 * 
	 * @param driver
	 *            WebDriver to check
	 * @return boolean true if an AJAX call was made, false otherwise
	 */
	public static boolean waitAndCheckForAjaxCalls(WebDriver driver) {
		int wait_for_milliseconds = 500;
		int waited_for_milliseonds = 0;
		while (waited_for_milliseonds < wait_for_milliseconds)
			if (getNumberOfOpenAjaxConnections(driver) > 0L)
				return true;
		return false;
	}

	/**
	 * Checks if an element is visible. Waits 1 second for it to become visible.
	 * If it is not visible by then, returns false;
	 * 
	 * @param element
	 *            - WebElement to be checked for visibility
	 * @return true if visible within one second, false otherwise
	 */
	public static boolean checkElementWithDelay(WebElement element){
		int wait_for = 1100, waited = 0;
		while(wait_for>waited){
			if(element.isDisplayed())
				return true;
			waited+=100;
			sleep(100);
		}
		return false;
	}
	
	/**
	 * Wait for an Ajax Connection to open within the next 2 seconds If not
	 * opened, then return false
	 * 
	 * @param driver
	 *            - The WebDriver to use
	 * @return - boolean - true if new Ajax Connection is opened, false
	 *         otherwise
	 */
	public static boolean didAjaxConnectionsOpen(WebDriver driver) {
		int waited_in_milliseconds = 0;
		int wait_time_in_milliseconds = 2000;
		while (waited_in_milliseconds < wait_time_in_milliseconds) {
			if (getNumberOfOpenAjaxConnections(driver) > 0)
				return true;
			sleep(100);
			waited_in_milliseconds += 100;
		}
		return false;
	}
	
	/**
	 * Checks if an element is clickable by waiting for it to become clickable
	 * If it becomes clickable then returns true, false otherwise
	 * 
	 * @param element
	 *            - WebElement to check
	 * @return boolean
	 */
	public static boolean isClickable(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT, 100);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (TimeoutException te) {
			return false;
		}
	}
	
	/**
	 * Checks if an element is clickable by waiting for it to become clickable
	 * If it becomes clickable then returns true, false otherwise
	 * 
	 * @param element
	 *            - By locactor to check
	 * @return boolean
	 */
	public static boolean isClickable(By element) {
		try {
			WebDriverWait wait = new WebDriverWait(Browser.getDriver(), WAITFOR_TIMEOUT);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (TimeoutException te) {
			return false;
		}
	}

	/**
	 * Checks if an element is enabled by checking if the class contains
	 * \"disabled\" or disabled attribute shows true
	 * 
	 * @param element
	 *            - the WebElement to check for
	 * @return boolean
	 */
	public static boolean isEnabled(WebElement element) {
		boolean classAttribute = !element.getAttribute("class").contains("disabled");
		boolean seleniumProperties = element.isDisplayed() && element.isEnabled();
		boolean attributeOK = (element.getAttribute("disabled") == null) ? true
				: !element.getAttribute("disabled").equalsIgnoreCase("true");
		return seleniumProperties && classAttribute && attributeOK;
	}
	
	/**
	 * Checks if an element is selected by checking if the class contains
	 * active as value
	 * 
	 * @param by
	 *            - the By locator to check for
	 * @return boolean
	 * @author HISHAM
	 */
	public static boolean isSelected(By by) {
		WebElement element = findElement(by);
		boolean classAttribute = element.getAttribute("class").contains("active");
		boolean seleniumProperties = element.isDisplayed() && element.isEnabled();
		return seleniumProperties && classAttribute;
	}
	
	/**
	 * Checks if an element is selected by checking if the class contains
	 * active as value
	 * 
	 * @param WebElement
	 *            - the element to check for
	 * @return boolean
	 * @author HISHAM
	 */
	public static boolean isSelected(WebElement element) {
		boolean classAttribute = element.getAttribute("class").contains("active");
		boolean seleniumProperties = element.isDisplayed() && element.isEnabled();
		return seleniumProperties && classAttribute;
	}

	/**
	 * Takes a list of objects (generic type) and selects a random object from
	 * the list returns that object
	 * 
	 * @param objects
	 *            List<T> - the list from which to pick the random element
	 * @return T - the random object selected
	 */
	public static <T> T getRandomObjectFromList(List<T> objects) {
		return objects.get((new Random()).nextInt(objects.size()));
	}
	
	/**
	 * Helps to scroll into view an object that would otherwise be obscured
	 * using Javascript. (Should be better than Selenium's Actions
	 * moveToElement)
	 * 
	 * Javascript argument: "arguments[0].scrollIntoView(boolean);"
	 * i).  boolean - If true, the top of the element will be aligned to the top of the visible area of the scrollable ancestor.
	 * ii). boolean - If false, the bottom of the element will be aligned to the bottom of the visible area of the scrollable ancestor.
	 * 
	 * @param element - WebElement - the element to scroll to
	 * @param driver - WebDriver - the driver
	 */
	public static boolean scrollToElementWithJavascript(WebElement element, WebDriver driver) {
		JavascriptExecutor executor = ((JavascriptExecutor) driver);
		executor.executeScript("arguments[0].scrollIntoView(false);", element);
		if (UiHelper.isClickable(element)) {
			return true;
		} else {
			executor.executeScript("arguments[0].scrollIntoView(true);", element);
			if (UiHelper.isClickable(element)) {
				return true;
			}
		}
		Log.logError("Scrolling to top/bottom couldn't make the element visible...");
		return false;
	}
	
	/**
	 * Helps to scroll into view an object that would otherwise be obscured
	 * using Javascript. (Should be better than Selenium's Actions
	 * moveToElement)
	 *
	 * Javascript argument: "arguments[0].scrollIntoView(boolean);"
	 * i).  boolean - If true, the top of the element will be aligned to the top of the visible area of the scrollable ancestor.
	 * ii). boolean - If false, the bottom of the element will be aligned to the bottom of the visible area of the scrollable ancestor. 
	 * 
	 * @param element - By - the element to scroll to
	 * @param driver - WebDriver - the driver
	 */
	public static boolean scrollToElementWithJavascript(By by, WebDriver driver) {
		WebElement element = findElement(by);
		JavascriptExecutor executor = ((JavascriptExecutor) driver);
		executor.executeScript("arguments[0].scrollIntoView(false);", element);
//		Log.logInfo("Scrolled to bottom for element...");
		if (UiHelper.isClickable(element)) {
			return true;
		} else {
			executor.executeScript("arguments[0].scrollIntoView(true);", element);
//			Log.logInfo("Scrolled to top for  element...");
			if (UiHelper.isClickable(element)) {
				return true;
			}
		}
		Log.logError("Scrolling to top/bottom couldn't make the element visible...");
		return false;
	}
	
	/**
	 * @author Hisham
	 * 
	 *		Description: Check/Uncheck checkbox
	 * 
	 * @return boolean 
	 * 		- true if clickable & can be checked/unchecked 
	 * 		- false if not clickable
	 */
	public static boolean checkUncheck(WebElement element) {
		if (UiHelper.isClickable(element)) {
			element.click();
			if (UiHelper.isClickable(element))
				element.click();
			return true;
		}
		return false;
	}
	
	/**
	 * @author Hisham
	 * 
	 * @param locator
	 * @param val
	 * @param sec
	 */
	public static void fluentWaitForCssValue(final By locator, final String val, int sec) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Browser.getDriver()).withTimeout(sec, TimeUnit.SECONDS)
				.pollingEvery(250, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		Predicate<WebDriver> predicate = new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver) {
				WebElement element = driver.findElement(locator);
				String status = element.getAttribute("class");
				if (status.contains(val))
					return true;
				return false;
			}
		};
		wait.until(predicate);
	}
	
	/**
	 * @author Hisham
	 * 
	 * @param element
	 * @param val
	 * @param sec
	 */
	public static Boolean fluentWaitForCssValue(final WebElement element, final String val, int sec) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Browser.getDriver()).withTimeout(sec, TimeUnit.SECONDS)
				.pollingEvery(250, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);

		Function<WebDriver, Boolean> waitFunction = new Function<WebDriver, Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				String status = element.getAttribute("class");
				if (status.contains(val))
					return true;
				return false;
			}
		};
		return Boolean.valueOf(wait.until(waitFunction));
	}
	
	/**
	 * @author Hisham
	 * 
	 * @param locator
	 * @param val
	 * @param sec
	 */
	public static WebElement fluentWaitForElementVisibility(final By locator, int TimeoutSec) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Browser.getDriver())
				.withTimeout(TimeoutSec, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);

		Function<WebDriver, WebElement> waitFunction = new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				WebElement element = driver.findElement(locator);
				if (element.isDisplayed())
					return element;
				return null;
			}
		};
		return wait.until(waitFunction);
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Description: This method uses WebDriver's FluentWait class
	 * to evaluate given element visibility until conditions in waitFunction
	 * becomes true
	 * 
	 * @param WebElement element
	 * 			- The WebElement to search for
	 * @param int TimeoutSec
	 * 			- Wait time in seconds
	 * @return WebElement 
	 * 			- if found within timeout, null otherwise
	 */
	public static WebElement fluentWaitForElementVisibility(final WebElement element, int TimeoutSec) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Browser.getDriver())
				.withTimeout(TimeoutSec, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);

		Function<WebDriver, WebElement> waitFunction = new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				if (element.isDisplayed())
					return element;
				return null;
			}
		};
		return wait.until(waitFunction);
	}
	
	/**
	 * Description:
	 * </br>Finds given element locator using Selenium's support API for FluentWait & ExpectedConditions.
	 * </br>Finding strategy as follows:
	 * </br>	1. Fluently wait for an element until ExpectedConditions become true
	 * </br>	2. Search for ExpectedConditions become true on regular interval time period defined on pollingEvery() method
	 * </br>	3. Search stops and returns element if found within given regular interval
	 * </br>	4. Search stops and returns null if not found till time period defined on withTimeout() method
	 * 
	 * @author HISHAM
	 * 
	 * @param element -Element locator to find
	 * @param timeoutSec -Maximum time for wait
	 * @return By -Locator if found, null otherwise
	 */
	public static By fluentWaitForElementClickability(By element, int timeoutSec) {
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Browser.getDriver())
					.withTimeout(timeoutSec, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.elementToBeClickable(element));
			return element;
		} catch (TimeoutException te) {
			Log.logError("Element not clickable within timeout. Returning null", te);
			return null;
		}
	}
	
	/**
	 * Description:
	 * </br>Finds given element locator using Selenium's support API for FluentWait & ExpectedConditions.
	 * </br>Finding strategy as follows:
	 * </br>	1. Fluently wait for an element until ExpectedConditions become true
	 * </br>	2. Search for ExpectedConditions become true on regular interval time period defined on pollingEvery() method
	 * </br>	3. Search stops and returns element if found within given regular interval
	 * </br>	4. Search stops and returns null if not found till time period defined on withTimeout() method
	 * 
	 * @author HISHAM
	 * 
	 * @param element -Element locator to find
	 * @param timeoutSec -Maximum time for wait
	 * @return element -WebElement if found, null otherwise
	 */
	public static WebElement fluentWaitForElementClickability(WebElement element, int timeoutSec) {
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Browser.getDriver())
					.withTimeout(timeoutSec, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);

			wait.until(ExpectedConditions.elementToBeClickable(element));
			return element;
		} catch (TimeoutException te) {
			Log.logError("Element not clickable within timeout. Returning null", te);
			return null;
		}
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Helper method for focusing WebDriver window using javaScript
	 */
	public static void focusBrowserWindow() {
		WebDriver driver = Browser.getDriver();
		// Store the current window handle
		String currentWindowHandle = driver.getWindowHandle();

		// run javascript and alert code
		/*((JavascriptExecutor) driver).executeScript("alert('Test')");
		driver.switchTo().alert().accept();*/

		// Switch back to to the window using the handle saved earlier
		driver.switchTo().window(currentWindowHandle);
	}
	
	////////////////////////////////JQuery, Angular, and JavaScript Waiter/////////////////////////////
	// http://www.swtestacademy.com/selenium-wait-javascript-angular-ajax/
	
	private static WebDriver jsWaitDriver;
    private static WebDriverWait jsWait;
    private static JavascriptExecutor jsExec;
	
    
  //Get the driver 
    public static void setJSDriver (WebDriver driver) {
        jsWaitDriver = driver;
        jsWait = new WebDriverWait(jsWaitDriver, 10);
        jsExec = (JavascriptExecutor) jsWaitDriver;
    }
	
	 //Wait for JQuery Load
    public static void waitForJQueryLoad() {
        //Wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) jsWaitDriver)
                .executeScript("return jQuery.active") == 0);

        //Get JQuery is Ready
        boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");

        //Wait JQuery until it is Ready!
        if(!jqueryReady) {
            System.out.println("JQuery is NOT Ready!");
            //Wait for jQuery to load
            jsWait.until(jQueryLoad);
        } else {
            System.out.println("JQuery is Ready!");
        }
    }


    //Wait for Angular Load
    public static void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(jsWaitDriver,15);
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
                .executeScript(angularReadyScript).toString());

        //Get Angular is Ready
        boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if(!angularReady) {
            System.out.println("ANGULAR is NOT Ready!");
            //Wait for Angular to load
            wait.until(angularLoad);
        } else {
            System.out.println("ANGULAR is Ready!");
        }
    }

    //Wait Until JS Ready
    public static void waitUntilJSReady() {
        WebDriverWait wait = new WebDriverWait(jsWaitDriver,15);
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        //Wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) jsWaitDriver)
                .executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady =  (Boolean) jsExec.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if(!jsReady) {
            System.out.println("JS in NOT Ready!");
            //Wait for Javascript to load
            wait.until(jsLoad);
        } else {
            System.out.println("JS is Ready!");
        }
    }

    //Wait Until JQuery and JS Ready
    public static void waitUntilJQueryReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        //First check that JQuery is defined on the page. If it is, then wait AJAX
        Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
        if (jQueryDefined == true) {
            //Pre Wait for stability (Optional)
            sleep(20);

            //Wait JQuery Load
            waitForJQueryLoad();

            //Wait JS Load
            waitUntilJSReady();

            //Post Wait for stability (Optional)
            sleep(20);
        }  else {
            System.out.println("jQuery is not defined on this site!");
        }
    }

    //Wait Until Angular and JS Ready
    public static void waitUntilAngularReady() {
        JavascriptExecutor jsExec = (JavascriptExecutor) jsWaitDriver;

        //First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
        Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
        if (!angularUnDefined) {
            Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
            if(!angularInjectorUnDefined) {
                //Pre Wait for stability (Optional)
                sleep(20);

                //Wait Angular Load
                waitForAngularLoad();

                //Wait JS Load
                waitUntilJSReady();

                //Post Wait for stability (Optional)
                sleep(20);
            } else {
                System.out.println("Angular injector is not defined on this site!");
            }
        }  else {
            System.out.println("Angular is not defined on this site!");
        }
    }

    //Wait Until JQuery Angular and JS is ready
    public static void waitJQueryAngular() {
        waitUntilJQueryReady();
        waitUntilAngularReady();
    }

    public static void sleep (Integer seconds) {
        long secondsLong = (long) seconds;
        try {
            Thread.sleep(secondsLong);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}