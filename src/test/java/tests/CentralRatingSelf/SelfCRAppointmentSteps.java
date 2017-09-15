package tests.CentralRatingSelf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.support.PageFactory;

import hu.siteportal.pages.CentralRating.SelfCRAppointment;
import mt.siteportal.controls.DayPickerWidget;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Verify;

/**
 * 
 * @author ubiswas
 *
 */

public class SelfCRAppointmentSteps {
	SelfCRAppointment slfcr;
	SubjectDetails subD;
	DayPickerWidget daypicker;

	String selectedDate, selectedTime, selectedMonth, selectedYear = null;

	///////////////////////////////////

	public String getDateByLeadTime(Date currentDate, int leadTime, DateFormat dateformat) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, leadTime);
		return dateformat.format(c.getTime()).split("-")[0];
	}

	public String getNextDate(Date currentDate, int leadTime, DateFormat dateformat) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, leadTime);
		return dateformat.format(c.getTime()).split("-")[0];
	}

	public String getNextMonth(Date currentDate, int leadTime, DateFormat dateformat) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.MONTH, leadTime);
		return dateformat.format(c.getTime()).split("-")[1];
	}

	protected boolean isHoliday(String year, String monthName, String day) throws ParseException {
		int monthnumber = daypicker.getMonthNumberFromName(monthName);
		String dateString = String.format("%s-%d-%s", year, monthnumber, day);
		Date date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
		String dayName = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

		if (dayName.equalsIgnoreCase("Sunday") || dayName.equalsIgnoreCase("Satarday")) {
			return true;
		} else
			return false;
	}
	//////////////////////////////////////////

	public void selectedDateIsEqualToCurrentDatePlusLeadingTime(Date currentDate, int leadTime, DateFormat dateformat) {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		String leadDate = getDateByLeadTime(currentDate, leadTime, dateformat);
		String currDate = slfcr.getDefaultSelectedDate();
		Verify.True(currDate.equalsIgnoreCase(leadDate), "Checking selected date = Current Date + Lead time", "Passed",
				"Failed");

	}

	public void schedulerCalendarPopUpIsDisplayed() {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		Verify.True(slfcr.getModalContent().isDisplayed(), "Checking Schedular Calendar Pops up is displayed", "Passed",
				"Failed");

	}

	public void selectMonth(String month) {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		slfcr.selectAMonth(month);
	}

	public void selectDate(String date) {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		slfcr.selectADate(date);
	}

	public void selectTime() {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		List<String> timeList = new ArrayList<String>();
		timeList = slfcr.getCurrentTimesList();
		int size = timeList.size();
		slfcr.clickTimeSelectionButton();
		int i = 0;

		for (String t : timeList) {
			i++;
			slfcr.selectATime(t);
			slfcr.clickSearchButton();
			if (slfcr.getYesButton().isDisplayed()) {
				selectedTime = t;
				break;
			} else {
				slfcr.selectATime(timeList.get(size - i));
				slfcr.clickSearchButton();
				if (slfcr.getYesButton().isDisplayed()) {
					selectedTime = timeList.get(size - i);
					break;
				}
			}
		}
	}

	public void searchAppointment() {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		slfcr.clickSearchButton();

	}

	public String getCustomMessage() {

		String customMessage = "Your appointment has been scheduled for " + selectedTime + " on " + selectedDate + "-"
				+ selectedMonth + "-" + selectedYear;
		return customMessage;
	}

	public boolean messageFormatIsCorrect(String regex, String msg) {

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(msg);
		if (m.find()) {
			return true;
		} else
			return false;
	}

	public void confirmAppointment() {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		String regex = "[\\D\\s]*\\d*:\\d*[\\D\\s]*\\d*-\\D*-\\d*";
		selectedYear = slfcr.getSelectedYear().split("\\s")[1];
		slfcr.clickYesButton();
		String confirmMsg = slfcr.getConfirmationMessage();
		Verify.True(confirmMsg.equalsIgnoreCase(getCustomMessage()), "Chekcing confirmation message is displayed",
				"Passed", "Failed");

		Verify.True(messageFormatIsCorrect(regex, confirmMsg), "Checking message format is dd-Mmm-YYYY hh:mm am/pm",
				"Passed", "Failed");
	}

	public void closeSchedulerCalendarPopsUp() {
		slfcr = PageFactory.initElements(Browser.getDriver(), SelfCRAppointment.class);
		subD = slfcr.clickModalCloseButton();
		Verify.True(subD.isOpened(), "Closing scheduler calendar pops up", "Passed", "Failed");
	}

	public void confirmVisitStatus(String visitName, String msg) {

		String visitStatusRegex = "[\\D\\s]*[\\d*\\-\\D*\\-\\d*\\s*\\d*\\:\\d*\\s*\\D*]*";
		String status = subD.getStatusOfVisit(visitName);

		Verify.True(status.contains(msg), "Checking visit status is " + msg, "Passed", "Failed");
		Verify.True(messageFormatIsCorrect(visitStatusRegex, status),
				"Checking visit status format is  dd-Mmm-YYYY hh:mm am/pm.", "Passed", "Failed");
	}

	public void selectAppointmentDateAndTime(String[] currentMonthDateDay, String context) {
		selectMonth(currentMonthDateDay[1]);
		selectDate(currentMonthDateDay[0]);
		selectedMonth = currentMonthDateDay[1];
		selectedDate = currentMonthDateDay[0];
		switch (context) {
		case "Scheduled":
			selectTime();
			break;
		case "Rescheduled":
			selectTime();
			break;
		default:
			break;
		}

	}

	public void reschedulerAndCancelControlsAreDisplayed(String visitName) {
		subD = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		Verify.True(
				subD.getViewOrReschedularIcon(visitName).isDisplayed()
						&& subD.getCancelSchedularIcon(visitName).isDisplayed(),
				"Checking Reschedule & Cancel controls are displayed", "Passed", "Failed");

	}

	public void confirmAppointmentWithReason(String reason) {
		slfcr.enterReason(reason);
		confirmAppointment();

	}

	public void getTimesList() {
		slfcr.getCurrentTimesList();

	}
}
