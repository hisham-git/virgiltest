package tests.CentralRating;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import hu.siteportal.pages.CentralRating.CrAppointmentDetails;
import mt.siteportal.objects.ConfirmPopUp;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import tests.Abstract.AbstractTest;

@Test(enabled = false, groups = { "AppointmentScreenFields" })
public class AppointmentScreenFieldsTests extends AbstractTest {
	CrAppointmentDetails crAppointmentDetails;
	ConfirmPopUp confirm;
	Map<String, String> getAppointmentInfoForItem = new HashMap<String, String>();
	Map<String, String> getAppointmentComments = new HashMap<String, String>();
	
	static Date currentDate, futureDate;
	static DateFormat dateFormat, timeFormat;
	static Calendar cal;

	static String[] splitDate, splitTime;
	static String formattedDate, formattedTime, ntfMonth, ntfDate, ntfYear, schMonth, schDate, schYear, startedHr, startedMin,
			startedMeridiem;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		
		currentDate = new Date();
		dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		formattedDate = dateFormat.format(currentDate);
		
		splitDate = (formattedDate.split("-"));
		ntfMonth = new DateFormatSymbols().getShortMonths()[Integer.parseInt(splitDate[0]) - 1].toUpperCase();
		ntfDate = splitDate[1];
		ntfYear = splitDate[2];
		
		cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, 7);
		futureDate = cal.getTime();
		formattedDate = dateFormat.format(futureDate);
		
		splitDate = (formattedDate.split("-"));
		schMonth = new DateFormatSymbols().getShortMonths()[Integer.parseInt(splitDate[0]) - 1].toUpperCase();
		schDate = splitDate[1];
		schYear = splitDate[2];
		
		
		timeFormat = new SimpleDateFormat("h-mm-a");
		formattedTime = timeFormat.format(new Date());
		splitTime = (formattedTime.split("-"));
		startedHr = splitTime[0];
		startedMin = splitTime[1];
		startedMeridiem = splitTime[2];
		
		BeforeSteps beforeSteps = new BeforeSteps();
		crAppointmentDetails = PageFactory.initElements(Browser.getDriver(), CrAppointmentDetails.class);
		Nav.toURL(
				"https://siteportaltest.medavante.net/centralrating/subject/bfc785a5-de60-4adf-9473-b7d492af62c4/studyvisit/e95b8ebb-77ec-425e-886e-41c7d14219ae");
		beforeSteps.loginAs(adminLogin, adminPasword);
	}

	@Test
	public void fieldsTest() {
		UiHelper.sleep(5000);
		Log.logDebugMessage("Appointment Header: " + crAppointmentDetails.getHeadingText());
		Log.logDebugMessage("Visit status: " + crAppointmentDetails.getVisitStatusText());
		getAppointmentInfoForItem = crAppointmentDetails.getAppointmentInfoForItem("All");
		for (String key : getAppointmentInfoForItem.keySet()) {
			Log.logDebugMessage(key + " -> " + getAppointmentInfoForItem.get(key));
		}

		getAppointmentComments = crAppointmentDetails.getAppointmentComments();
		for (String key : getAppointmentComments.keySet()) {
			Log.logDebugMessage(key + " -> " + getAppointmentComments.get(key));
		}
		
		Log.logDebugMessage("default notification date: "+ crAppointmentDetails.getNotificationDateTime());
		Log.logDebugMessage("default notification timeZone: "+ crAppointmentDetails.getNotificationTimeZone());
		Log.logDebugMessage("default schedule date: "+ crAppointmentDetails.getScheduleDateTime());
		Log.logDebugMessage("default schedule timeZone: "+ crAppointmentDetails.getScheduleTimeZone());
		
		Log.logStep("Setting notification date [" + ntfDate + "|" + ntfMonth + "|" + ntfYear + "]");
		crAppointmentDetails.setNotificationDate(ntfDate, ntfMonth, ntfYear);
		crAppointmentDetails.setNotificationTime(startedHr, startedMin, startedMeridiem);
		
		Log.logStep("Setting schedule date [" + schDate + "|" + schMonth + "|" + schYear + "]");
		crAppointmentDetails.setScheduleDate(schDate, schMonth, schYear);
		crAppointmentDetails.setScheduleTime("08", "00", "AM");
		
		Log.logDebugMessage("current notification date: "+ crAppointmentDetails.getNotificationDateTime());
		Log.logDebugMessage("current notification timeZone: "+ crAppointmentDetails.getNotificationTimeZone());
		Log.logDebugMessage("current schedule date: "+ crAppointmentDetails.getScheduleDateTime());
		Log.logDebugMessage("current schedule timeZone: "+ crAppointmentDetails.getScheduleTimeZone());
		
		Log.logStep("Picking a clinician for scheduling...");
		crAppointmentDetails.pickClinician();
		Log.logStep("Refreshing clinician appointment info...");
		crAppointmentDetails.refreshAppointment();
		
//		confirm = crAppointmentDetails.scheduleAppointmentAt("Available", "08", "00", "AM");
		try {
			confirm.clickNoButton();
		} catch (Exception e) {
			Log.logDebugMessage("No slots found");
		}
		
		Log.logStep("Setting schedule date [" + schDate + "|" + schMonth + "|" + schYear + "]");
		crAppointmentDetails.setScheduleDate(schDate, schMonth, schYear);
		crAppointmentDetails.setScheduleTime("10", "00", "AM");
		
//		confirm = crAppointmentDetails.scheduleAppointmentAt("Available", "10", "00", "AM");
		try {
			confirm.clickYesButton();
		} catch (Exception e) {
			Log.logDebugMessage("No slots found");
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
}
