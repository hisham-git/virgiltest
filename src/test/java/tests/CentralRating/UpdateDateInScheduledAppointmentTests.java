package tests.CentralRating;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.CentralRatingSteps;
import steps.Tests.SubjectSteps;

@Test(groups = "UpdateDateInScheduledAppointment")
public class UpdateDateInScheduledAppointmentTests extends AbstractCentralRating{
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Nav.toURL(maPortalUrl);
		subjectSteps = new SubjectSteps();
		crSteps = new CentralRatingSteps();
		beforeSteps = new BeforeSteps();

		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
		subjectSteps.openSubjectDetails(subjectName);
		subjectSteps.selectVisitInPosition(visitName[2], visitPos[2]);
		crSteps.viewReScheduleAppointment();
		crSteps.appointmentDetailsVerification(subjectName);

		randCurrentDateTime = crSteps.getRandomCurrentDate(2);
		futureDateTime = crSteps.getAvailableScheduleDateTime(randCurrentDateTime, clinician, dateSpan);
		crSteps.backToSubject();
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		subjectSteps.selectVisitInPosition(visitName[2], visitPos[2]);
		crSteps.viewReScheduleAppointment();
		crSteps.appointmentDetailsVerification(subjectName);
		randCurrentDateTime = crSteps.getRandomCurrentDate(2);
	}
	
	/**
	 * After every test: 
	 * 1. Return to Study dashboard from first link on breadcrumbs
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		crSteps.backToSubject();
		Log.logTestMethodEnd(method, result);
	}
	
	@DataProvider
	public Object[][] dateChangeInScheduledAppointmentData() {
		return new Object[][] {
//			{ true, false, true, false, false, "UserProvidedDate" },	// See Issue => https://medavanteinc.atlassian.net/browse/VP-5655
			{ false, true, true, false, false, "UserTimeZoneDate" },
			{ true, true, true, false, false, "UserProvidedDate" },
			
			{ true, false, false, false, true, "UserProvidedDate" },
			{ true, false, false, true, false, "UserProvidedDate" },
			{ true, false, false, true, true, "UserProvidedDate" },
			{ false, false, false, false, true, "DefaultDate" },
			{ false, false, false, true, false, "UserTimeZoneDate" },
			{ false, false, false, true, true, "UserTimeZoneDate" },
			
			{ true, true, false, true, false, "UserProvidedDate" },
			{ true, true, false, true, true, "UserProvidedDate" },
			{ false, true, false, true, false, "UserTimeZoneDate" },
			{ false, true, false, true, true, "UserTimeZoneDate" },
		};
	}
	
	@Test(groups = { "DateChangeInScheduledAppointment",
			"SFB-TC-1818" }, description = "Check if user can change notification datetime for the CR Appointments with scheduled status", dataProvider = "dateChangeInScheduledAppointmentData")
	public void dateChangeInScheduledAppointmentTest(boolean setNotification, boolean setSchedule, boolean shouldSave,
			boolean setTimeSlot, boolean changeClinician, String expected) {
		switch (expected) {
		case "UserProvidedDate":
			ntfDateTime = randCurrentDateTime;
			break;
		case "DefaultDate":
			ntfDateTime = crSteps.getCurrentNotificationDateTime();
			break;
		case "UserTimeZoneDate":
			ntfDateTime = crSteps.getUserTimeZoneDateTime(userTimeZone);
			break;
		default:
			break;
		}
		
		crSteps.setDateTime("Notification", randCurrentDateTime, setNotification);
		crSteps.setDateTime("Schedule", futureDateTime, setSchedule);
		crSteps.saveAppointmentAndConfirm(shouldSave, true);
		
		crSteps.getCurrentClinicianCalender(!shouldSave);
		crSteps.updateClinicianAndTimeSlot(setSchedule, changeClinician, setTimeSlot, !shouldSave);
		crSteps.verifyDateTime("Notification", ntfDateTime);
		crSteps.setAppointmentStatus("Scheduled", futureDateTime, shouldSave);
	}
}