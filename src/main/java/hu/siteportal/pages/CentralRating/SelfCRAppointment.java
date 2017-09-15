package hu.siteportal.pages.CentralRating;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Description - Self CR page object page
 * 
 * @author ubiswas
 *
 */
public class SelfCRAppointment extends BasePage {

	public SelfCRAppointment(WebDriver driver) {
		super(driver);
	}

	@FindBy(css = "div#self-cr-modal div.modal-content")
	public WebElement selfCrModalContent;

	@FindBy(css = "div#self-cr-modal div.modal-header")
	private WebElement selfCrModalHeader;

	@FindBy(css = "#self-cr-modal div.modal-body")
	private WebElement selfCrModalBody;

	@FindBy(css = "#self-cr-modal div.modal-menu.col-xs-24.col-sm-10")
	private WebElement selfCrModalMenu;

	@FindBy(css = "#self-cr-modal div.modal-details.col-xs-24.col-sm-14")
	private WebElement selfCrModalMenuDetails;

	@FindBy(css = "#time-slot-content")
	private WebElement selfCrTimeSlotContent;

	@FindBy(css = "div[data-ng-show='ShowConfirmationWithMore']")
	private WebElement selfCrShowConfirmationWithMore;

	@FindBy(css = "div[data-ng-show='ShowConfirmation']")
	private WebElement selfCrShowConfirmation;

	@FindBy(css = "div#cr-scheduling-calendar")
	private WebElement selfCrDatePicker;

	@FindBy(css = "div.col-xs-12.col-sm-6.col-sm-offset-3.confirmation-button-container")
	private WebElement selfCrConfirmationBtnContainer;

	////////////////////////////////

	private By modalTitle = new By.ByCssSelector("h4");
	private By modalCloseBtn = new By.ByCssSelector("button");
	private By selectADateTitle = new By.ByCssSelector("div:nth-child(1) label");
	private By timeSelectionButton = new By.ByCssSelector("button.btn.dropdown-toggle.btn-default");
	private By selectedTime = new By.ByCssSelector("button > span:nth-child(1)");
	private By searchButton = new By.ByCssSelector("button#selfCrSchedulingSearchButton");
	private By timeList = new By.ByCssSelector("ul>li>span");
	private By defaultDate = new By.ByCssSelector("#cr-scheduling-calendar_cell_selected");

	private By yesButton = new By.ByCssSelector("div[ng-click='confirmButtonClick()']");
	private By noButton = new By.ByCssSelector("div[ng-click='discardButtonClick()']");
	private By moreAppointmentLink = new By.ByCssSelector("div:nth-child(3) > div:nth-child(2) > div:nth-child(3) > a");
	private By listOfTimeSlots = new By.ByCssSelector("div.btn.btn-default.ng-binding.k-button");
	private By reasonForReschedule = new By.ByCssSelector("textarea[data-ng-model='crReschedulingComment']");

	//////////////////////////////

	private By lastYear = new By.ByCssSelector("a:nth-child(1)");
	private By presentYear = new By.ByCssSelector("a:nth-child(2)");
	private By nextYear = new By.ByCssSelector("a:nth-child(3)");
	private By datePickerMonth = new By.ByCssSelector("table td");
	private By datePickerDate = new By.ByCssSelector("table td");

	///////////////////////////////

	private By msgForbeforeSelectingTimeSlot = new By.ByCssSelector("div:nth-child(1) > div");
	private By msgForNotFoundSelectedTimeSlot = new By.ByCssSelector(
			"div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div");
	private By msgForFoundSelectedTimeSlot = new By.ByCssSelector(
			"div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div");
	private By msgForNoClinicianFound = new By.ByCssSelector(
			"div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div");
	private By msgForSelectingNotFoundTimeSlot = new By.ByCssSelector(
			"div:nth-child(3) > div.text-center > div:nth-child(1) > div");

	private By confirmationMessage = new By.ByCssSelector(
			"div[data-ng-show='isAppointmentCreated'] div.menu-title.ng-binding");

	//////////////////////////////

	public WebElement getModalContent() {
		return selfCrModalContent;
	}

	public WebElement getDatePickerElement() {
		return selfCrDatePicker;
	}

	public By getElement(String context) {
		return new By.ByCssSelector(context);
	}

	public void selectYear(String year) {
		selfCrDatePicker.findElement(getYearElement(year)).click();
	}

	private By getYearElement(String year) {

		switch (year) {
		case "Next":
			return nextYear;
		case "Last":
			return lastYear;
		case "Present":
			return presentYear;
		default:
			return presentYear;
		}

	}

	public String getSelectedYear() {
		return selfCrDatePicker.findElement(presentYear).getText().toString();
	}

	public String getModalTitle() {
		return selfCrModalHeader.findElement(modalTitle).getText().toString();
	}

	public WebElement getModalCloseButton() {
		return selfCrModalHeader.findElement(modalCloseBtn);
	}

	public SubjectDetails clickModalCloseButton() {
		UiHelper.click(getModalCloseButton());
		return PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
	}

	public String getSelectADateTitle() {
		return selfCrModalMenu.findElement(selectADateTitle).getText().toString();
	}

	public WebElement getTimeSelectionButton() {
		return selfCrModalMenu.findElement(timeSelectionButton);
	}

	public void deslectTimeSelectionDrpDown() {
		UiHelper.clear(timeSelectionButton);
	}

	public void clickTimeSelectionButton() {
		UiHelper.click(getTimeSelectionButton());
	}

	public List<WebElement> getTimeList() {
		clickTimeSelectionButton();
		List<WebElement> list = selfCrModalMenu.findElements(timeList);
		return list;
	}

	public String getRandomTimeFromCurrentTimeList() {
		List<WebElement> list = getTimeList();
		String time = list.get(0 + (int) (Math.random() * list.size())).getText();
		clickTimeSelectionButton();
		return time;
	}

	public List<String> getCurrentTimesList() {
		List<String> timeList = new ArrayList<String>();
		List<WebElement> list = getTimeList();
		for (WebElement el : list) {
			String time = el.getText();
			timeList.add(time);
		}
		return timeList;
	}

	public String getSelectedTime() {
		return selfCrModalMenu.findElement(selectedTime).getText().toString();
	}

	public WebElement getSearchButton() {
		return selfCrModalMenu.findElement(searchButton);
	}

	public void clickSearchButton() {
		WebElement el = getSearchButton();
		if (el.isEnabled()) {
			UiHelper.click(el);
			UiHelper.sleep(2000);
		}
	}

	public void setMenuDetialsMessageElement(By el) {
	}

	public String getMenuDetialsMessage(By el) {
		WebElement tempEl = selfCrModalMenuDetails.findElement(el);
		String tempText = tempEl.getText();
		return tempText;
	}

	public WebElement getYesButton() {
		if (selfCrShowConfirmationWithMore.findElement(yesButton).isDisplayed()) {
			return selfCrShowConfirmationWithMore.findElement(yesButton);
		} else
			return selfCrShowConfirmation.findElement(yesButton);

	}

	public void clickYesButton() {
		WebElement el = getYesButton();
		if (el.isEnabled())
			UiHelper.click(getYesButton());
	}

	public WebElement getNoButton() {
		if (selfCrShowConfirmationWithMore.findElement(noButton).isDisplayed()) {
			return selfCrShowConfirmationWithMore.findElement(noButton);
		} else
			return selfCrShowConfirmation.findElement(noButton);

	}

	public void clickNoButton() {
		WebElement el = getNoButton();
		if (el.isEnabled())
			UiHelper.click(getNoButton());
	}

	public WebElement getMoreAppointmentLink() {
		return selfCrModalMenuDetails.findElement(moreAppointmentLink);
	}

	public void clickMoreAppointmentLink() {
		UiHelper.click(getMoreAppointmentLink());
	}

	public List<WebElement> getCurrentTimeSlotLists() {
		return selfCrTimeSlotContent.findElements(listOfTimeSlots);
	}

	public void selectTimeSlotAndClick(String timeslot) {
		List<WebElement> timeSlotList = getCurrentTimeSlotLists();
		boolean isTimeSlotFound = false;
		if (!timeSlotList.isEmpty()) {
			for (WebElement el : timeSlotList) {
				if (el.getText().toString().equalsIgnoreCase(timeslot)) {
					Log.logInfo("Selecting TimeSlot: " + "[" + timeslot + "]");
					UiHelper.sleep(3000);
					el.click();
					isTimeSlotFound = true;
					break;
				}
			}
			if (!isTimeSlotFound)
				Log.logInfo("Selected Time Slot is not in list");
		} else
			Log.logInfo("Time Slot list is not present");

	}

	public List<WebElement> getMonthLists() {
		return selfCrDatePicker.findElements(datePickerMonth);
	}

	public List<WebElement> getDateLists() {
		UiHelper.sleep(3000);
		return selfCrDatePicker.findElements(datePickerDate);
	}

	public SelfCRAppointment selectATime(String time) {
		WebElement el = getTimeSelectionButton();
		if (null != UiHelper.fluentWaitForElementClickability(el, 30)) {
			// Log.logInfo("Selecting Time: " + "[" + time + "]");
			UiHelper.scrollToElementWithJavascript(el, Browser.getDriver());
			UiHelper.selectInDropdownBtn(el, time);
			return PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		} else {
			Log.logError("Couldn't choose a time because of dropdown not available within timeout");
			return null;
		}
	}

	public String getAppropriateMessage() {
		String st = getSelectedTime();
		if (st.isEmpty()) {
			return getMenuDetialsMessage((msgForbeforeSelectingTimeSlot));
		} else {
			List<WebElement> foundTimeSlotList = getCurrentTimeSlotLists();
			WebElement isYesBtnPresent = getYesButton();
			if (foundTimeSlotList.isEmpty()) {
				return getMenuDetialsMessage(msgForNoClinicianFound);
			} else {
				if (isYesBtnPresent.isDisplayed()) {
					if (foundTimeSlotList.get(0).isDisplayed()) {
						return getMenuDetialsMessage(msgForSelectingNotFoundTimeSlot);
					}
					return getMenuDetialsMessage(msgForFoundSelectedTimeSlot);
				}
				return getMenuDetialsMessage(msgForNotFoundSelectedTimeSlot);
			}
		}
	}

	public void selectMonthAndClick(String month) {
		List<WebElement> monthList = null;
		monthList = getMonthLists();
		boolean isMonthFound = false;
		if (!monthList.isEmpty()) {
			for (WebElement el : monthList) {
				if (el.getText().toString().equalsIgnoreCase(month)) {
					UiHelper.sleep(3000);
					el.click();
					isMonthFound = true;
					break;
				}
			}
			if (!isMonthFound) {
				Log.logInfo("Selected month is not in list. Selecting current month");
			}

		} else
			Log.logInfo("Month list is not present");

	}

	public void selectDateAndClick(String date) {
		List<WebElement> dateList = null;
		dateList = getDateLists();
		boolean isDateFound = false;
		if (!dateList.isEmpty()) {
			UiHelper.sleep(3000);
			for (WebElement el : dateList) {
				if (el.getText().toString().equalsIgnoreCase(date)) {
					UiHelper.sleep(3000);
					el.click();
					isDateFound = true;
					break;
				}
			}
			if (!isDateFound)
				Log.logInfo("Selected date is not in list");
		} else
			Log.logInfo("Date list is not present");
	}

	public String getScheduledDateTime() {
		return null;
	}

	public String getConfirmationMessage() {
		return selfCrModalBody.findElement(confirmationMessage).getText().toString();
	}

	public void selectAMonth(String month) {
		selectYear("Present");
		selectMonthAndClick(month);
	}

	public void selectADate(String date) {
		selectDateAndClick(date);
	}

	public void selectATimeSlot(String timeSlot) {
		selectTimeSlotAndClick(timeSlot);
	}

	public void addSelectedMonthToList(String month) {
		// monthList.add(month);
	}

	public void removeSelectedMonthFromList(String month) {
		// monthList.remove(month);
	}

	public String getDefaultSelectedDate() {
		return selfCrDatePicker.findElement(defaultDate).getText().toString();
	}

	public String isFoundOpeningByTimeSlots() {
		return null;
	}

	public void enterReason(String reason) {
		selfCrShowConfirmationWithMore.findElement(reasonForReschedule).sendKeys(reason);
	}

	public String getSelectedMonth() {

		return selfCrDatePicker.findElement(datePickerDate).getText();
	}

}
