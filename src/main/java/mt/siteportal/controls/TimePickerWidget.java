package mt.siteportal.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Control class, wrapper for the Time Picker Widget WebElement Can be used to
 * set the hour, minute and am/pm separately This is ONLY for the Time Picker,
 * so need for the Minutes Picker and Hours Picker use different classes
 * 
 * @author Syed Amer Zawad
 *
 */
public class TimePickerWidget extends Control {

	/*
	 * Locators. WebElements were not used since they need to be re-initialized
	 * too frequently
	 */
	private By increaseHoursButton = By.xpath(".//a[@data-action='incrementHours']"),
			increaseMinutesButton = By.xpath(".//a[@data-action='incrementMinutes']"),
			decreaseHoursButton = By.xpath(".//a[@data-action='decrementHours']"),
			decreaseMinutesButton = By.xpath(".//a[@data-action='decrementMinutes']"),
			toggleAmPm = By.xpath(".//button");
	private By hoursText = By.xpath(".//span[@data-action='showHours']"),
			minutesText = By.xpath(".//span[@data-action='showMinutes']");

	public TimePickerWidget(WebElement timePicker) {
		super(timePicker);
		element = timePicker;
	}

	/**
	 * Set the Time's Hour to the given parameter using the increase/decrease
	 * controls
	 * 
	 * @param hour
	 *            String, should be from 01 to 12
	 * @return true if all goes well, false otherwise, throws SkipException if
	 *         the parameters are of the wrong format
	 */
	public boolean setHour(String hour) {
//		UiHelper.isClickable(element.findElement(hoursText));
		try {
			int hourAsNumber = Integer.parseInt(hour);
			if (hourAsNumber > 12 || hourAsNumber < 0)
				throw new SkipException(
						"The Hours value for the Time Picker Widget is out of range. HOUR parameter was [" + hour
								+ "]. Consider checking the tests parameters...");
			int widgetHourAsNumber = Integer.parseInt(element.findElement(hoursText).getText());
			while (hourAsNumber > widgetHourAsNumber) {
				element.findElement(increaseHoursButton).click();
				UiHelper.sleep(100);
				widgetHourAsNumber = Integer.parseInt(element.findElement(hoursText).getText());
			}
			while (hourAsNumber < widgetHourAsNumber) {
				element.findElement(decreaseHoursButton).click();
				UiHelper.sleep(100);
				widgetHourAsNumber = Integer.parseInt(element.findElement(hoursText).getText());
			}
			return true;
		} catch (NumberFormatException e) {
			Log.logWarning("There was an error when parsing to set the Hours from the timelement widget.");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set the Time's Minutes to the given parameter using the increase/decrease
	 * controls
	 * 
	 * @param minutes
	 *            String, should be from 00 to 59
	 * @return true if all goes well, false otherwise, throws SkipException if
	 *         the parameters are of the wrong format
	 */
	public boolean setMinutes(String minutes) {
//		UiHelper.isClickable(element.findElement(minutesText));
		try {
			int minuteAsNumber = Integer.parseInt(minutes);
			if (minuteAsNumber > 59 || minuteAsNumber < 00)
				throw new SkipException(
						"The Minutes value for the Time Picker Widget is out of range. MINUTES parameter was ["
								+ minutes + "]. Consider checking the tests parameters...");
			int widgetMinuteAsNumber = Integer.parseInt(element.findElement(minutesText).getText());
			while (minuteAsNumber > widgetMinuteAsNumber) {
				element.findElement(increaseMinutesButton).click();
				UiHelper.sleep(100);
				widgetMinuteAsNumber = Integer.parseInt(element.findElement(minutesText).getText());
			}
			while (minuteAsNumber < widgetMinuteAsNumber) {
				element.findElement(decreaseMinutesButton).click();
				UiHelper.sleep(100);
				widgetMinuteAsNumber = Integer.parseInt(element.findElement(minutesText).getText());
			}
			return true;
		} catch (Exception e) {
			Log.logWarning("There was an error when parsing to set the Minutes from the timelement widget. Error : "
					+ e.getMessage());
			return false;
		}
	}

	/**
	 * Set the AM or PM to the given parameter using the AM/PM button
	 * 
	 * @param minutes
	 *            String, should be either AM or PM
	 * @return true if all goes well, false otherwise, throws SkipException if
	 *         the parameters are of the wrong format
	 */
	public boolean setAmOrPm(String amOrPm) {
		if (!(amOrPm.equals("AM") || amOrPm.equals("PM")))
			throw new SkipException("The AM/PM value for the Time Picker Widget is not correct. AMorPM parameter was ["
					+ amOrPm + "]. Consider checking the tests parameters...");
		int numberOfClicks = 0;
		while (!element.findElement(toggleAmPm).getText().equals(amOrPm) && numberOfClicks < 3) {
			element.findElement(toggleAmPm).click();
			numberOfClicks++;
			UiHelper.sleep(100);
		}
		return numberOfClicks < 3;
	}
	
	/**
	 * </p>
	 * Set the Time's Hour using the increase/decrease controls
	 * </p>
	 * @param action
	 * 			- String, value should be Increase/Decrease
	 * @return widgetHourAsNumber
	 * 			- Integer, hour value from timePicker widget
	 * @author HISHAM
	 */
	public int changeHour(String action) {
		int widgetHourAsNumber = 0;
		switch (action) {
		case "Increase":
			element.findElement(increaseHoursButton).click();
			UiHelper.sleep(100);
			widgetHourAsNumber = Integer.parseInt(element.findElement(hoursText).getText());
			Log.logInfo("Hour increased to: [" + widgetHourAsNumber + "]");
			break;

		case "Decrease":
			element.findElement(decreaseHoursButton).click();
			UiHelper.sleep(100);
			widgetHourAsNumber = Integer.parseInt(element.findElement(hoursText).getText());
			Log.logInfo("Hour decreased to: [" + widgetHourAsNumber + "]");
			break;

		default:
			Log.logError("Hour action type doesn't match. Should be 'Increase/Decrease'.");
			break;
		}
		return widgetHourAsNumber;
	}
	
	/**
	 * </p>
	 * Set the Time's Minute using the increase/decrease controls
	 * </p>
	 * @param action
	 * 			- String, value should be Increase/Decrease
	 * @return widgetMinuteAsNumber
	 * 			- Integer, minute value from timePicker widget
	 * @author HISHAM
	 */
	public int changeMinute(String action) {
		int widgetMinuteAsNumber = 0;
		switch (action) {
		case "Increase":
			element.findElement(increaseMinutesButton).click();
			UiHelper.sleep(100);
			widgetMinuteAsNumber = Integer.parseInt(element.findElement(minutesText).getText());
			Log.logInfo("Minute increased to: [" + widgetMinuteAsNumber + "]");
			break;

		case "Decrease":
			element.findElement(decreaseMinutesButton).click();
			UiHelper.sleep(100);
			widgetMinuteAsNumber = Integer.parseInt(element.findElement(minutesText).getText());
			Log.logInfo("Minute decreased to: [" + widgetMinuteAsNumber + "]");
			break;

		default:
			Log.logError("Minute action type doesn't match. Should be 'Increase/Decrease'.");
			break;
		}
		return widgetMinuteAsNumber;
	}
}
