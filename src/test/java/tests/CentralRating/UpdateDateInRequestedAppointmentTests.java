package tests.CentralRating;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.CentralRatingSteps;
import steps.Tests.SubjectSteps;

@Test(groups = "UpdateDateInRequestedAppointment")
public class UpdateDateInRequestedAppointmentTests extends AbstractCentralRating{
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Nav.toURL(maPortalUrl);
		
		subjectSteps = new SubjectSteps();
		crSteps = new CentralRatingSteps();
		beforeSteps = new BeforeSteps();

		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
		subjectSteps.openSubjectDetails(subjectName);
		subjectSteps.selectVisitInPosition(visitName[1], visitPos[1]);
		crSteps.viewReScheduleAppointment();
		crSteps.appointmentDetailsVerification(subjectName);

		randCurrentDateTime = crSteps.getRandomCurrentDate(2);
		futureDateTime = crSteps.getAvailableScheduleDateTime(randCurrentDateTime, clinician, dateSpan);
		crSteps.backToSubject();
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		subjectSteps.selectVisitInPosition(visitName[1], visitPos[1]);
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
	public Object[][] dateChangeInRequestedAppointmentData() {
		return new Object[][] {
			{ true, false, false, "UserProvidedDate" },
			{ true, true, false, "UserProvidedDate" },
			{ false, true, false, "UserTimeZoneDate" },
			{ true, false, true, "UserProvidedDate"},
			{ false, false, true, "DefaultDate"},
			{ false, true, true, "UserTimeZoneDate" },
			{ true, true, true, "UserProvidedDate" }
		};
	}
	
	@Test(groups = { "DateChangeInRequestedAppointment",
			"SFB-TC-1820" }, description = "Check if user can change notification datetime for the CR Appointments with requested status", dataProvider = "dateChangeInRequestedAppointmentData")
	public void dateChangeInRequestedAppointmentTest(boolean setNotification, boolean setSchedule,
			boolean pickClinician, String expected) {
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
		crSteps.saveAppointment(!pickClinician);
		
		crSteps.pickClinicianFirstTimeSlot(clinician, "Available", pickClinician);
		crSteps.scheduleAppointment(pickClinician);

		crSteps.verifyDateTime("Notification", ntfDateTime);
		crSteps.setAppointmentStatus("Requested", futureDateTime, pickClinician);
	}
}