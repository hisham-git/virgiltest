package tests.CentralRatingSelf;

import org.testng.annotations.Test;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

import org.testng.annotations.BeforeClass;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;

/**
 * Description - Schedule/ Reschedule / Cancel CR appointment test by site user.
 * 
 * @author ubiswas
 *
 */
public class SelfCRAppointmentTest extends AbstractSelfCRAppointment {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenSubject(username, pass, study, site, subject);
		subjectD = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
	}

	@Test(groups = { "scheduleAnAppointment", "SFB-TC-1677" }, description = "Schedule CR appointment by Site user")
	public void scheduleAnAppointment() {

		subjectD.addCrVisit(visitName);
		crStep.schedulerCalendarPopUpIsDisplayed();
		crStep.selectedDateIsEqualToCurrentDatePlusLeadingTime(getCurrentDate(), leadTime, dateFormat);
		crStep.selectAppointmentDateAndTime(getCurrentMonthDateDay(), "Scheduled");
		crStep.searchAppointment();
		crStep.confirmAppointment();
		crStep.closeSchedulerCalendarPopsUp();
		crStep.confirmVisitStatus(visitName, "Scheduled");
		crStep.reschedulerAndCancelControlsAreDisplayed(visitName);

	}

	@Test(groups = { "rescheduleAnAppointment",
			"SFB-TC-1677" }, dependsOnGroups = "scheduleAnAppointment", description = "Reschedule CR appointment by Site user")
	public void rescheduleAnAppointment() {

		subjectD.rescheduleCrAppointment(visitName);
		crStep.selectAppointmentDateAndTime(getCurrentMonthDateDay(), "Rescheduled");
		crStep.searchAppointment();
		crStep.confirmAppointmentWithReason("Test");
		crStep.closeSchedulerCalendarPopsUp();
		crStep.confirmVisitStatus(visitName, "Scheduled");

	}

	@Test(groups = { "cancelAnAppointment",
			"SFB-TC-1677" }, dependsOnGroups = "rescheduleAnAppointment", description = "Cancel CR appointment by Site user")
	public void cancelAnAppointment() {

		subjectD.cancelCrAppointment(visitName, "Test");
		crStep.confirmVisitStatus(visitName, "Cancelled");
	}

	@AfterClass
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
		afterSteps.logout();
	}

}
