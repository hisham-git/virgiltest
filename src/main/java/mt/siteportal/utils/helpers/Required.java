package mt.siteportal.utils.helpers;

import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Required {
	private static final Logger LOGGER = LoggerFactory.getLogger(Required.class);

	private static String required = "ng-invalid-required";
	private static String inputRequired = "//input[contains(@class, 'ng-invalid-required')]";
	private static String dropdownRequired = "self::*[ancestor::div[@required]]";
	private static String inputInvalid = "//input[contains(@class, 'ng-invalid-pattern')]";
	private static String inputValid = "//input[contains(@class, 'ng-valid-pattern')]";
	private static String maskValid = "//input[contains(@class, 'ng-valid-mask-pattern')]";


	 /**
	    * Verifies that input element is required
	    *
	    * @param WebElement - element to verify.
	    *
	    * @return true if field is required otherwise false.
	    */
	/*public static boolean isInputRequired(By by) {
		Log.logInfo("Verification that " + by + " is required");
		try {
			WebElement element = UiHelper.findElement(by);
			element.findElement(new By.ByXPath(inputRequired));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}*/
	
	/**
	 * @author HISHAM
	 * 
	 * @param by - Element locator
	 * @return boolean - true if found, false otherwise
	 */
	public static boolean isInputRequired(By by) {
		try {
			WebElement element = UiHelper.findElement(by);
			int arraySize = element.findElements(new By.ByXPath(inputRequired)).size();
			if (arraySize > 0) {
				return true;
			}
			return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	/**
	 * @author HISHAM
	 * 
	 * @param element - Element locator
	 * @return boolean - true if found, false otherwise
	 */
	public static boolean isInputRequired(WebElement element) {
		try {
			int arraySize = element.findElements(new By.ByXPath(inputRequired)).size();
			if (arraySize > 0) {
				return true;
			}
			return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Verifies that input element is required
	 *
	 * @param WebElement - element to verify.
	 *
	 * @return true if field is required otherwise false.
	 */
	public static boolean isRequired(WebElement element) {
		String attributeValue = element.getAttribute("class");
		if (attributeValue.contains(required)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Verifies that input element is required by CSS attribute value
	 *
	 * @param By - element to verify.
	 *
	 * @return true if field is required otherwise false.
	 */
	public static boolean isRequired(By by) {
		WebElement element = UiHelper.findElement(by);
		String attributeValue = element.getAttribute("class");
		if (attributeValue.contains(required)) {
			return true;
		}
		return false;
	}


	public static boolean isInputInvalid(WebElement element) {
		try {
			element.findElement(new By.ByXPath(inputInvalid));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isInputValid(WebElement element) {
		try {
			element.findElement(new By.ByXPath(inputValid));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isMaskValid(WebElement element) {
		try {
			element.findElement(new By.ByXPath(maskValid));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	 /**
	    * Verifies that dropdown element is required
	    *
	    * @param WebElement - element to verify.
	    *
	    * @return true if field is required otherwise false.
	    */
	public static boolean isDropdownRequired(By by) {
		try {
			WebElement element = UiHelper.findElement(by);
			element.findElement(new By.ByXPath(dropdownRequired));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	 /**
	    * Verifies that dropdown element is required
	    *
	    * @param WebElement - element to verify.
	    *
	    * @return true if field is required otherwise false.
	    */
	public static boolean isDropdownRequired(WebElement element) {
		try {
			element.findElement(new By.ByXPath(dropdownRequired));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

    /**
     Add a comment to this line
     * Verifies that WebElement element is disabled
     *
     * @param WebElement - element to verify.
     *
     * @return true if field is disabled otherwise false.
     */
	public static boolean isElementDisabled(WebElement element) {
		if (null == element.getAttribute("disabled")) {
			return false;
		} else {
			return true;
		}
	}
}
