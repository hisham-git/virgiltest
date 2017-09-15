package hu.siteportal.pages.CentralRating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.google.common.collect.ArrayListMultimap;

import mt.siteportal.controls.DayPickerWidget;
import mt.siteportal.controls.TimePickerWidget;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.objects.ConfirmPopUp;
import mt.siteportal.pages.BasePage;
import mt.siteportal.pages.CrVisitList;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * </p>
 * Description: Page Object class containing different element locators & action
 * helper methods for CR Appointment screen
 * </p>
 * 
 * @author Abdullah Al Hisham
 */
public class CrAppointmentDetails extends BasePage{
	
	public CrAppointmentDetails(WebDriver driver) {
		super(driver);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@FindBy(css = "div#page-title")
	private WebElement titleBlock;
	
	@FindBy(css = "div.info-block > div.row")
	private WebElement infoBlock;
	
	@FindBy(css = "div.dark-block > div.comments")
	private WebElement commentsBlock;
	
	@FindBy(css = "div.appointment-block > div:nth-child(1)")
	private WebElement notificationDateBlock;
	
	@FindBy(css = "div.appointment-block > div:nth-child(2)")
	private WebElement scheduleDateBlock;
	
	@FindBy(css = "div.appointment-block > div:nth-child(3)")
	private WebElement subjectVisitCommentsBlock;
	
	@FindBy(css = "div.appointment-block > div:nth-child(4)")
	private WebElement scheduleBlock;
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private By heading = new By.ByCssSelector("h1");
	private By visitStatus = new By.ByCssSelector("div > h2");
	private By saveButton = new By.ByCssSelector("div > a[title = 'Save']");
	private By backButton = new By.ByCssSelector("div > button[title = 'Back']");
	private By initiateButton = new By.ByCssSelector("div > button[title = 'Initiate']");
	private By recallButton = new By.ByCssSelector("div > button[title = 'Recall Assessment']");
	private By aliasInput = new By.ByXPath("//label[contains(text(), 'Alias')]/following-sibling::input");
	
	private By infoTitle = new By.ByCssSelector("label.small-title");
	private By commentrows = new By.ByCssSelector("p.ng-binding");
	
	private By dateTime = new By.ByCssSelector("div.date-info > label:nth-child(1)");
	private By timeZone = new By.ByCssSelector("div.date-info > label:nth-child(2)");
	
	private By datePickerButton = new By.ByCssSelector("#datepicker a.datepickerbutton > span.icon-calendar");
	private By timePickerButton = new By.ByCssSelector("#timepicker a.datepickerbutton > span.icon-time");
	private By ntfDatePicker = new By.ByCssSelector("body > div:nth-of-type(2).picker-open .datepicker > div.datepicker-days > table");
	private By ntfTimePicker = new By.ByCssSelector("body > div:nth-of-type(3).picker-open .timepicker > div.timepicker-picker > table");
	private By schDatePicker = new By.ByCssSelector("body > div:nth-of-type(4).picker-open .datepicker > div.datepicker-days > table");
	private By schTimePicker = new By.ByCssSelector("body > div:nth-of-type(5).picker-open .timepicker > div.timepicker-picker > table");
	
	private By clearNotificationButton = new By.ByCssSelector("a[title='Clear Notification Date']");
	private By clearScheduleButton = new By.ByCssSelector("a[title='Clear Schedule Date']");
	
	private By pickClinicianLink = new By.ByXPath("//a[contains(text(), 'Pick One')]");
	
	private By scheduleAppointmentButton = new By.ByCssSelector("button[data-ng-click='scheduleAppointment()']");
//	private By updateAppointmentButton = new By.ByCssSelector("button[data-ng-click='scheduleAppointment()']");
//	private By scheduleAppointmentButton = new By.ByXPath("//button/span[contains(text(), 'Schedule Appointment')]");
	private By updateAppointmentButton = new By.ByXPath("//button/span[contains(text(), 'Update Appointment')]");
	private By cancelAppointmentButton = new By.ByCssSelector("button[data-ng-click='cancelAppointment()']");
	
	private By refreshAppointmentButton = new By.ByCssSelector("a[title='Refresh']");
	private By subjectVisitComments  = new By.ByCssSelector("textarea[placeholder='Enter comments here']");
	
	private By calenderBlock = new By.ByCssSelector("div.top-indent > div:nth-child(1)");
	private By calenderHeaders = new By.ByCssSelector("div.grid-header label");
	private By calenderRows = new By.ByCssSelector("div.grid-row");
	private By availableTimeSlots = new By.ByCssSelector("label[data-ng-if='timeSlot.availability === 1']");
	private By partialAvailableTimeSlots = new By.ByCssSelector("label[data-ng-if='timeSlot.availability === 2']");
	private By unavailableTimeSlots = new By.ByCssSelector("label[data-ng-if='timeSlot.availability === 3']");
	
	private By scheduleInfoBlock = new By.ByCssSelector("div.top-indent > div:nth-child(2)");
	
	private By successInfo = new By.ByCssSelector(
			"div#system-message-container div.success-info-container:not(.ng-hide) > label");
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public String getHeadingText() {
		return titleBlock.findElement(heading).getText();
	}
	
	public String getVisitStatusText() {
		return titleBlock.findElement(visitStatus).getText().split(":")[1].trim();
	}
	
	public WebElement getSaveButton() {
		return titleBlock.findElement(saveButton);
	}
	
	public void saveChanges() {
		UiHelper.clickAndWait(getSaveButton());
	}
	
	public WebElement getBackButton() {
		return titleBlock.findElement(backButton);
	}
	
	public void cancelAppointmentFrom(String context) {
		UiHelper.click(getBackButton());
		switch (context) {
		case "Subject Details":
			PageFactory.initElements(driver, SubjectDetails.class);
			break;

		case "CR Visit List":
			PageFactory.initElements(driver, CrVisitList.class);
			break;

		default:
			Log.logError("Context doesn't match...");
			break;
		}
	}
	
	public WebElement getInitiateButton() {
		try {
			return titleBlock.findElement(initiateButton);
		} catch (NoSuchElementException nse) {
			return null;
		}
	}
	
	public WebElement getRecallButton(){
		return titleBlock.findElement(recallButton);
	}
	
	public WebElement getAliasInput() {
		return infoBlock.findElement(aliasInput);
	}
	
	public void setAliasInput(String value) {
		UiHelper.sendKeys(getAliasInput(), value);
	}
	
	/**
	 * </p>
	 * Description: This function returns info on current CR Appointment screen
	 * based on following condition:
	 * 	1. If 'All' is passed as a parameter, returns all available info
	 * 	2. Otherwise, returns specific info for given parameter
	 * </p>
	 * @param item
	 * 		- name of the item to get info for
	 * @return appointmentinfo
	 * 		- Retrived info as key-value pair in Map
	 * 
	 * @author HISHAM
	 */
	public Map<String, String> getAppointmentInfoForItem(String item) {
		String title, value;
		Map<String, String> appointmentinfo = new HashMap<>();
		List<WebElement> infoTitleElements = infoBlock.findElements(infoTitle);

		for (WebElement infoTitleElement : infoTitleElements) {
			title = infoTitleElement.getText();

			if (item.equalsIgnoreCase("All")) {
				switch (title) {
				case "Subject":
					value = infoTitleElement.findElement(new By.ByXPath("following-sibling::div//a")).getText();
					appointmentinfo.put(title, value);
					break;
				case "Alias":
					value = infoTitleElement.findElement(new By.ByXPath("following-sibling::input")).getText();
					appointmentinfo.put(title, value);
					break;
				default:
					value = infoTitleElement.findElement(new By.ByXPath("following-sibling::label")).getText();
					appointmentinfo.put(title, value);
					break;
				}

			} else if (item.equalsIgnoreCase("Subject") && item.equalsIgnoreCase(title)) {
				value = infoTitleElement.findElement(new By.ByXPath("following-sibling::div//a")).getText();
				appointmentinfo.put(title, value);
				return appointmentinfo;

			} else if (item.equalsIgnoreCase("Alias") && item.equalsIgnoreCase(title)) {

				value = infoTitleElement.findElement(new By.ByXPath("following-sibling::input")).getText();
				appointmentinfo.put(title, value);
				return appointmentinfo;

			} else if (item.equalsIgnoreCase(title)) {
				value = infoTitleElement.findElement(new By.ByXPath("following-sibling::label")).getText();
				appointmentinfo.put(title, value);
				return appointmentinfo;
			}
		}
		return appointmentinfo;
	}
	
	/**
	 * </p>
	 * Description: This function returns comments info (Study/Site/Visit) on current CR Appointment screen
	 * </p>
	 *
	 * @return appointmentComments
	 * 		- Retrived info as key-value pair in Map
	 * 
	 * @author HISHAM
	 */
	public Map<String, String> getAppointmentComments() {
		String title, value;
		Map<String, String> appointmentComments = new HashMap<>();
		List<WebElement> comments = commentsBlock.findElements(commentrows);
		for (WebElement comment : comments) {
			title = comment.getText().split(":")[0].trim();
			value = comment.getText().split(":")[1].trim();
			appointmentComments.put(title, value);
		}
		return appointmentComments;
	}
	
	public WebElement getNotificationDateBlock() {
		return notificationDateBlock;
	}
	
	public String getNotificationDateTime() {
		return getNotificationDateBlock().findElement(dateTime).getText();
	}
	
	public String getNotificationTimeZone() {
		String zone = getNotificationDateBlock().findElement(timeZone).getText();
		return TextHelper.splitParentheses(zone);
	}
	
	public WebElement getScheduledDateBlock() {
		return scheduleDateBlock;
	}
	
	public String getScheduleDateTime() {
		return getScheduledDateBlock().findElement(dateTime).getText();
	}
	
	public String getScheduleTimeZone() {
		String zone = getScheduledDateBlock().findElement(timeZone).getText();
		return TextHelper.splitParentheses(zone);
	}
	
	public WebElement getClearNotificationDateButton() {
		return notificationDateBlock.findElement(clearNotificationButton);
	}

	public WebElement getClearScheduleDateButton() {
		return scheduleDateBlock.findElement(clearScheduleButton);
	}
	
	/**	
	 * Uses the Date Picker Widget from  Notification/Schedule date
	 * and changes it to the input as specified
	 * 
	 * @param date String, ranges from 01 to 31, also depending on the month
	 * @param month String, ranges from JAN to DEC
	 * @param year String, of the format YYYY
	 */
	private void setDate(WebElement dateBlock, By datePicker, String date, String month, String year) {
		if (null != UiHelper.fluentWaitForElementVisibility(dateBlock, 5))
			UiHelper.click(dateBlock.findElement(datePickerButton));
		WebElement element = UiHelper.findElement(datePicker);
		DayPickerWidget widget = new DayPickerWidget(element);
		widget.setYear(year);
		widget.setMonth(month);
		widget.setDate(date);
		UiHelper.click(dateBlock.findElement(timePickerButton));
	}

	/**
	 * Uses the Time Picker Widget from  Notification/Schedule date
	 * and changes it to the input as specified
	 * 
	 * @param hours String, ranging from 01 to 12
	 * @param minutes String, ranging from 00 to 59
	 * @param amOrPm String, can either be AM or PM
	 */
	private void setTime(WebElement dateBlock, By timePicker, String hours, String minutes, String amOrPm) {
		UiHelper.click(dateBlock.findElement(timePickerButton));
		WebElement element = UiHelper.findElement(timePicker);
		TimePickerWidget widget = new TimePickerWidget(element);
		widget.setHour(hours);
		widget.setMinutes(minutes);
		widget.setAmOrPm(amOrPm);
		UiHelper.click(dateBlock.findElement(timePickerButton));
	}
	
	/**
	 * </p>
	 * Uses the Time Picker Widget from  Notification date
	 * and changes it to Increasing/Decreasing value of Hour/Minute
	 * </p>
	 * @param control
	 * 			- String, control which will be changed(Hour/Minute)
	 * @param action
	 * 			- String, action which will be performed(Increase/Decrease)
	 * @return widgetTime
	 * 			- String, value of changed hour/minute from widget
	 */
	public String changeNotificationTime(String control, String action) {
		int widgetTime = 0;
		TimePickerWidget timePickerWidget;
		WebElement timePickerElement;
		WebElement ntfTimePickerButton = notificationDateBlock.findElement(timePickerButton);
		UiHelper.click(ntfTimePickerButton);
		timePickerElement = UiHelper.findElement(ntfTimePicker);
		timePickerWidget = new TimePickerWidget(timePickerElement);
		switch (control) {
		case "Hour":
			widgetTime = timePickerWidget.changeHour(action);
			break;
		case "Minute":
			widgetTime = timePickerWidget.changeMinute(action);
			break;
		default:
			Log.logError("Control type parameter doesn't match. Should be 'Hour/Minute'.");
			break;
		}
		UiHelper.click(ntfTimePickerButton);
		return String.valueOf(widgetTime);
	}
	
	/**
	 * </p>
	 * Uses the Time Picker Widget from  Schedule date
	 * and changes it to Increasing/Decreasing value of Hour/Minute
	 * </p>
	 * @param control
	 * 			- String, control which will be changed(Hour/Minute)
	 * @param action
	 * 			- String, action which will be performed(Increase/Decrease)
	 * @return widgetTime
	 * 			- String, value of changed hour/minute from widget
	 */
	public String changeScheduleTime(String control, String action) {
		int widgetTime = 0;
		TimePickerWidget timePickerWidget;
		WebElement timePickerElement;
		WebElement schTimePickerButton = scheduleDateBlock.findElement(timePickerButton);
		UiHelper.click(schTimePickerButton);
		timePickerElement = UiHelper.findElement(schTimePicker);
		timePickerWidget = new TimePickerWidget(timePickerElement);
		switch (control) {
		case "Hour":
			widgetTime = timePickerWidget.changeHour(action);
			break;
		case "Minute":
			widgetTime = timePickerWidget.changeMinute(action);
			break;
		default:
			Log.logError("Control type parameter doesn't match. Should be 'Hour/Minute'.");
			break;
		}
		UiHelper.click(schTimePickerButton);
		return String.valueOf(widgetTime);
	}
	
	public void setNotificationDate(String date, String month, String year) {
		setDate(notificationDateBlock, ntfDatePicker, date, month, year);
	}

	public void setNotificationTime(String hours, String minutes, String amOrPm) {
		setTime(notificationDateBlock, ntfTimePicker, hours, minutes, amOrPm);
	}
	
	public void setScheduleDate(String date, String month, String year) {
		setDate(scheduleDateBlock, schDatePicker, date, month, year);
	}

	public void setScheduleTime(String hours, String minutes, String amOrPm) {
		setTime(scheduleDateBlock, schTimePicker, hours, minutes, amOrPm);
	}
	
	public void clearNotificationDate() {
		UiHelper.click(getClearNotificationDateButton());
	}
	
	public void clearScheduleDate() {
		UiHelper.click(getClearScheduleDateButton());
	}
	
	public WebElement getSubjectVisitCommentsTextarea() {
		return subjectVisitCommentsBlock.findElement(subjectVisitComments);
	}

	public String getSubjectVisitComments() {
		return getSubjectVisitCommentsTextarea().getAttribute("value");
	}

	public void setSubjectVisitComments(String comment) {
		WebElement commentTextArea = getSubjectVisitCommentsTextarea();
		commentTextArea.clear();
		UiHelper.sendKeys(commentTextArea, comment);
	}
	
	public WebElement getPickClinicianLink() {
		return scheduleBlock.findElement(pickClinicianLink);
	}
	
	public boolean pickClinician() {
		WebElement pickClinicianLink = getPickClinicianLink();
		if (UiHelper.isClickable(pickClinicianLink)) {
			UiHelper.clickAndWait(pickClinicianLink);
			return true;
		} else {
			Log.logError("Clinician link not yet available for clicking.");
			return false;
		}
	}
	
	public WebElement getScheduleAppointmentButton() {
		return scheduleBlock.findElement(scheduleAppointmentButton);
	}
	
	public WebElement getCancelAppointmentButton() {
		return scheduleBlock.findElement(cancelAppointmentButton);
	}
	
	public WebElement getUpdateAppointmentButton() {
		return scheduleBlock.findElement(scheduleAppointmentButton);
	}

	public WebElement getRefreshAppointmentButton() {
		return scheduleBlock.findElement(refreshAppointmentButton);
	}

	public void refreshAppointment() {
		UiHelper.click(getRefreshAppointmentButton());
	}
	
	public WebElement getClinicianCalender() {
		return scheduleBlock.findElement(calenderBlock);
	}
	
	public List<String> getClinicianNames() {
		List<String> names = new ArrayList<String>();
		List<WebElement> tableRows = scheduleBlock.findElements(calenderRows);
		for (WebElement row : tableRows) {
			names.add(row.findElement(new By.ByCssSelector("div:nth-child(2):not(.slot) > label")).getText());
		}
		return names;
	}
	
	/*public ArrayListMultimap<String, WebElement> getClinicianCalenderElements() {
		ArrayListMultimap<String, WebElement> clinicianCalenderMap = ArrayListMultimap.create();

		List<WebElement> tableHeaders = scheduleBlock.findElements(calenderHeaders);
		List<WebElement> tableRows = scheduleBlock.findElements(calenderRows);
		for (WebElement row : tableRows) {
			List<WebElement> rowElements = row.findElements(new By.ByCssSelector("div"));
			Map<String, WebElement>
		}
	}*/

	/**
	 * </p>
	 * Description: This method returns time slot WebElements from Clinician
	 * calender based on given type
	 * </p>
	 * 
	 * @param type
	 *            - Type name (Available/PartialAvailable/Unavailable) in String
	 * @return slotsFound - Time slot elements as List<WebElement>
	 * @author HISHAM
	 */
	public List<WebElement> getClinicianTimeSlots(String type) {
		List<WebElement> slotsFound = new ArrayList<WebElement>();
		switch (type) {
		case "Available":
			slotsFound = scheduleBlock.findElements(availableTimeSlots);
			break;
		case "PartialAvailable":
			slotsFound = scheduleBlock.findElements(partialAvailableTimeSlots);
			break;
		case "Unavailable":
			slotsFound = scheduleBlock.findElements(unavailableTimeSlots);
			break;
		default:
			Log.logError("Slot Type doesn't match");
			break;
		}
		if (slotsFound.size() > 0) {
			Log.logInfo("Found: [" + slotsFound.size() + "]" + type + " slot(s");
			return slotsFound;
		}
		Log.logError("Found: [" + slotsFound.size() + "]" + type + " slot(s). Returning null...");
		return null;
	}
	
	/**
	 * </p>
	 * Description: This method returns time slot WebElements from Clinician
	 * calender based on given type & clinician name
	 * </p>
	 * 
	 * @param name
	 * 			- Clinician name in String 
	 * 
	 * @param type
	 * 			- Type name (Available/PartialAvailable/Unavailable) in String
	 * @return slotsFound - Time slot elements as List<WebElement>
	 * @author HISHAM
	 */
	public List<WebElement> getClinicianTimeSlots(String findClinician, String type) {
		List<WebElement> slotsFound = new ArrayList<WebElement>();
		List<WebElement> rows = scheduleBlock.findElements(calenderRows);
		for (WebElement row : rows) {
			String foundClinician = row.findElement(new By.ByCssSelector("div:nth-child(2):not(.slot) > label"))
					.getText();
			if (foundClinician.equalsIgnoreCase(findClinician)) {
				switch (type) {
				case "Available":
					slotsFound = row.findElements(availableTimeSlots);
					break;
				case "PartialAvailable":
					slotsFound = row.findElements(partialAvailableTimeSlots);
					break;
				case "Unavailable":
					slotsFound = row.findElements(unavailableTimeSlots);
					break;
				default:
					Log.logWarning("Slot Type doesn't match");
					break;
				}
				break;
			}
		}
		if (slotsFound.size() > 0) {
			Log.logInfo("[" + slotsFound.size() + "] " + type + " time slot(s) found for clinician: [" + findClinician
					+ "]");
			return slotsFound;
		}
		Log.logWarning("[" + slotsFound.size() + "] " + type + " time slot(s) found for clinician: [" + findClinician
				+ "]. Returning null...");
		return null;
	}
	
	public WebElement getClinicianScheduleInfoBlock(){
		return scheduleBlock.findElement(scheduleInfoBlock);
	}
	
	public Map<String, String> getClinicianScheduleInfo() {
		String title, value;
		WebElement scheduleInfoBlock = getClinicianScheduleInfoBlock();
		if (UiHelper.isPresentAndVisible(scheduleInfoBlock)) {
			Map<String, String> clinicianInfo = new HashMap<>();
			List<WebElement> titles = scheduleInfoBlock.findElements(new By.ByCssSelector("label.small-title"));
			List<WebElement> values = scheduleInfoBlock.findElements(new By.ByCssSelector("label.ng-binding"));

			Iterator<WebElement> titlesItr = titles.iterator();
			Iterator<WebElement> valuesItr = values.iterator();

			while (titlesItr.hasNext() && valuesItr.hasNext()) {
				title = titlesItr.next().getText();
				value = valuesItr.next().getText();
				clinicianInfo.put(title, value);
			}
			return clinicianInfo;
		} else {
			Log.logWarning("Schedule info block is not present/visible. Returning null...");
			return null;
		}
	}
	
	public boolean detailsIsOpened(String subjectName) {
		Map<String, String> item = getAppointmentInfoForItem("Subject");
		if ((getHeadingText().equalsIgnoreCase("Appointment")) && (item.get("Subject").equalsIgnoreCase(subjectName))) {
			return true;
		}
		return false;
	}
	
	public boolean detailsIsOpened() {
		UiHelper.fluentWaitForElementVisibility(titleBlock, 5);
		if (getHeadingText().equalsIgnoreCase("Appointment")) {
			return true;
		}
		return false;
	}
	
	public boolean clinicianIsSelected(String clinician) {
		try {
			scheduleBlock.findElement(new By.ByXPath("//a[contains(text(), '" + clinician + "')]"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public WebElement getCurrentClinicianLink(String clinician) {
		WebElement currentClinicianLink;
		try {
			currentClinicianLink = scheduleBlock
					.findElement(new By.ByXPath("//a[contains(text(), '" + clinician + "')]"));
			return currentClinicianLink;
		} catch (Exception e) {
			Log.logError("Link for clinician: [" + clinician + "] not found. Returning null...");
			return null;
		}
	}
	
	public WebElement getCurrentClinicianLink() {
		return scheduleBlock.findElement(new By.ByXPath("//label[text()[normalize-space() = 'Clinician:']]/a"));
	}

	public WebElement getOldClinicianLink() {
		return scheduleBlock
				.findElement(new By.ByXPath("//label[text()[normalize-space() = 'Currently Scheduled Clinician:']]/a"));
	}
	
	public String getCurrentClinicianName(boolean isOldClinicianNameVisible) {
		String name;
		try {
			if (isOldClinicianNameVisible) {
				name = getOldClinicianLink().getText();
				return name;
			} else {
				name = getCurrentClinicianLink().getText();
				return name;
			}
		} catch (Exception e) {
			Log.logError("Link for current clinician not found. Returning null...");
			return null;
		}
	}
	
	public String getSuccessInfo() {
		WebElement infoFound = UiHelper.fluentWaitForElementVisibility(successInfo, 30);
		if (null != infoFound) {
			return infoFound.getText().trim();
		}
		return "";
	}
}
