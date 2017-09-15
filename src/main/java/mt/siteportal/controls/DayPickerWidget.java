package mt.siteportal.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Control class, wrapper for the Day Picker Widget WebElement Can be used to
 * set the Date, Month and Year separately This is ONLY for the Day Picker, so
 * need for the Month Picker and Year Picker use different classes
 * 
 * @author Syed Amer Zawad
 *
 */
public class DayPickerWidget extends Control {
	/*
	 * The header controls
	 */
	private By prev = By.cssSelector(" th.prev"), next = By.cssSelector(" th.next"),
			month_header = By.cssSelector(" th.picker-switch");
	/*private String datesXPATHlocatorFirstPart = ".//td[contains(@class, 'day')][text()='",
			datesXPATHlocatorSecondPart = "']|.//td[@class='day active'][text()='", datesXPATHlocatorThirdPart = "']";*/
	private String datesXPATHlocatorFirstPart = ".//td[@class='day'][text()='",
			datesXPATHlocatorSecondPart = "']|.//td[contains(@class, 'day active')][text()='", datesXPATHlocatorThirdPart = "']|.//td[contains(@class, 'day today')][text()='", datesXPATHlocatorFourthPart = "']";

	/*
	 * Constructor
	 */
	public DayPickerWidget(WebElement datePicker) {
		super(datePicker);
		element = datePicker;
	}

	/**
	 * Sets the year using the header's previous and next buttons
	 * 
	 * @param year
	 *            String, must be of the format YYYY
	 * @return boolean, false if anything goes wrong, true otherwise
	 */
	public boolean setYear(String year) {
		WebElement nextElement;
		WebElement prevElement;
		WebElement widgetElement;
		final int timeOut = 10;
		String widgetYear = TextHelper.splitSpaces(element.findElement(month_header).getText())[1];
		
		if (!year.matches("\\d{4}"))
			throw new SkipException(
					"The format of the year parameter was not correct, expected format YYYY but was [" + year + "]");
		if (year.equals(widgetYear))
			return true;
		try {
			int yearAsNumber = Integer.parseInt(year);
			int widgetYearAsNumber = Integer.parseInt(widgetYear);
			while (yearAsNumber > widgetYearAsNumber) {
				nextElement = UiHelper.fluentWaitForElementClickability(element.findElement(next), timeOut);
				if (null != nextElement) {
					nextElement.click();
				} else {
					Log.logDebugMessage(nextElement + " found null");
					return false;
				}

				widgetElement = UiHelper.fluentWaitForElementVisibility(element.findElement(month_header), timeOut);
				if (null != widgetElement) {
					widgetYear = TextHelper.splitSpaces(widgetElement.getText())[1];
					widgetYearAsNumber = Integer.parseInt(widgetYear);
				} else {
					Log.logDebugMessage(widgetElement + " found null");
					return false;
				}
			}
			
			while (yearAsNumber < widgetYearAsNumber) {
				prevElement = UiHelper.fluentWaitForElementClickability(element.findElement(prev), 10);
				if (null != prevElement) {
					prevElement.click();
				} else {
					Log.logDebugMessage(prevElement + " found null");
					return false;
				}

				widgetElement = UiHelper.fluentWaitForElementVisibility(element.findElement(month_header), timeOut);
				if (null != widgetElement) {
					widgetYear = TextHelper.splitSpaces(widgetElement.getText())[1];
					widgetYearAsNumber = Integer.parseInt(widgetYear);
				} else {
					Log.logDebugMessage(widgetElement + " found null");
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			Log.logWarning(
					"There was an error while trying to set the Year from the Date Widget's Year : " + e.getMessage());
			return false;
		}
	}

	/**
	 * Sets the Month using the header's previous and next buttons
	 * 
	 * @param month
	 *            String, must be of the format and range JAN to DEC
	 * @return false if anything goes wrong, true otherwise
	 */
	public boolean setMonth(String month) {
		WebElement nextElement;
		WebElement prevElement;
		WebElement widgetElement;
		final int timeOut = 10;
		String widgetMonth = TextHelper.splitSpaces(element.findElement(month_header).getText())[0];
		
		/*if (month.equals(widgetMonth))	
			return true;*/
		try {
			int monthAsNumber = getMonthNumberFromName(month);
			if (monthAsNumber == 0)
				throw new SkipException("The format of the month parameter was not correct [" + month + "]");
			int widgetMonthAsNumber = getMonthNumberFromName(widgetMonth);
			if (monthAsNumber == widgetMonthAsNumber){
				return true;
			}
			while (monthAsNumber > widgetMonthAsNumber) {
				nextElement = UiHelper.fluentWaitForElementClickability(element.findElement(next), timeOut);
				if (null != nextElement) {
					nextElement.click();
				} else {
					Log.logDebugMessage(nextElement + " found null");
					return false;
				}
				widgetElement = UiHelper.fluentWaitForElementVisibility(element.findElement(month_header), timeOut);
				if (null != widgetElement) {
					widgetMonth = TextHelper.splitSpaces(widgetElement.getText())[0];
					widgetMonthAsNumber = getMonthNumberFromName(widgetMonth);
				} else {
					Log.logDebugMessage(widgetElement + " found null");
					return false;
				}
			} 
			
			while (monthAsNumber < widgetMonthAsNumber) {
				prevElement = UiHelper.fluentWaitForElementClickability(element.findElement(prev), timeOut);
				if (null != prevElement) {
					prevElement.click();
				} else {
					Log.logDebugMessage(prevElement + " found null");
					return false;
				}
				
				widgetElement = UiHelper.fluentWaitForElementVisibility(element.findElement(month_header), timeOut);
				if (null != widgetElement) {
					widgetMonth = TextHelper.splitSpaces(element.findElement(month_header).getText())[0];
					widgetMonthAsNumber = getMonthNumberFromName(widgetMonth);
				} else {
					Log.logDebugMessage(widgetElement + " found null");
					return false;
				}
			}
			
			return true;
		} catch (Exception e) {
			Log.logWarning(
					"There was an error while trying to set the Year from the Date Widget's Month : " + e.getMessage());
			return false;
		}
	}

	/**
	 * Sets the date from the Date Picker by directly clicking on the desired
	 * number
	 * 
	 * @param date
	 *            String of the format and range 01 to 31, depending on the
	 *            month
	 * @return boolean false if anything goes wrong, true otherwise
	 */
	public boolean setDate(String date) {
		try {
			if (date.startsWith("0"))
				date = date.replaceFirst("0", "");
			By datesXpath = By.xpath(datesXPATHlocatorFirstPart + date + datesXPATHlocatorSecondPart + date
					+ datesXPATHlocatorThirdPart + date + datesXPATHlocatorFourthPart);
			WebElement dateElement = element.findElement(datesXpath);
			dateElement.click();
			return true;
		} catch (NoSuchElementException ne) {
			Log.logWarning("The Date : " + date + " was not found for the Date Widget.");
			return false;
		}
	}

	/**
	 * Helper function, returns the given month's corresponding number
	 * 
	 * @param monthName
	 * @return
	 */
	public int getMonthNumberFromName(String monthName) {
		switch (monthName) {
		case "JAN":
		case "January":
			return 1;
		case "FEB":
		case "February":
			return 2;
		case "MAR":
		case "March":
			return 3;
		case "APR":
		case "April":
			return 4;
		case "MAY":
		case "May":
			return 5;
		case "JUN":
		case "June":
			return 6;
		case "JUL":
		case "July":
			return 7;
		case "AUG":
		case "August":
			return 8;
		case "SEP":
		case "September":
			return 9;
		case "OCT":
		case "October":
			return 10;
		case "NOV":
		case "November":
			return 11;
		case "DEC":
		case "December":
			return 12;
		default:
			return 0;
		}
	}

}
