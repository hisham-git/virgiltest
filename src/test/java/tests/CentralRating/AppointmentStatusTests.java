package tests.CentralRating;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.CentralRatingSteps;
import steps.Tests.SubjectSteps;

@Test(groups = "AppointmentStatus")
public class AppointmentStatusTests extends AbstractCentralRating{
	
	private static final String subjectName = "TestAppointmentStatus";
	private static final String visitName = "Visit1";
	private static final String clinician = clinicians.get(0);
	private static final String newClinician = clinicians.get(1);
	private static final int visitPos = 0;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Nav.toURL(maPortalUrl);
		
		subjectSteps = new SubjectSteps();
		crSteps = new CentralRatingSteps();
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
		subjectSteps.openSubjectDetails(subjectName);
		subjectSteps.selectVisitInPosition(visitName, visitPos);
		crSteps.viewReScheduleAppointment();
		crSteps.appointmentDetailsVerification(subjectName);

		currentDateTime = new Date();
		futureDateTime = crSteps.getAvailableScheduleDateTime(currentDateTime, clinician, dateSpan);
		crSteps.backToSubject();
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		subjectSteps.subjectDetailsVerification(subjectName);
		subjectSteps.selectVisitInPosition(visitName, visitPos);
		crSteps.viewReScheduleAppointment();
		crSteps.appointmentDetailsVerification(subjectName);
		if (method.getName().equalsIgnoreCase("updateScheduledCrVisitTest")) {
			currentDateTime = new Date();
			futureDateTime = crSteps.getAvailableScheduleDateTime(currentDateTime, newClinician, dateSpan+1);
			crSteps.backToSubject();
			subjectSteps.selectVisitInPosition(visitName, visitPos);
			crSteps.viewReScheduleAppointment();
			crSteps.appointmentDetailsVerification(subjectName);
		}
		crSteps.verifyAppointmentDetailsFields(study, site, subjectName, visitName);
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
	
	@Test(groups = { "PlaceholderAppointment",
			"SFB-TC-1383" }, description = "Check if user can schedule appointment for CR Visit without selecting clinician")
	public void placeholderAppointmentTest() {	
		crSteps.setDateTime("Notification", currentDateTime, true);
		crSteps.setDateTime("Schedule", futureDateTime, true);
		crSteps.verifySaveAppointmentIsEnabled(true);
		crSteps.pickClinicianTimeSlot(clinician, "Available", futureDateTime, true);
		crSteps.verifySaveAppointmentIsEnabled(false);
		crSteps.deSelectClinicianTimeSlot(clinician, "Available", futureDateTime);
		crSteps.verifySaveAppointmentIsEnabled(true);
		crSteps.saveAppointment(true);
		crSteps.verifyVisitStatusInAppointmentDetails("Requested");
		crSteps.verifyInitiateAppointmentIsVisible(false);
		crSteps.appointmentDetailsVerification(subjectName);
	}
	
	@Test(groups = { "CrVisitAppointmentCreation",
			"SFB-TC-1379" }, dependsOnGroups = "PlaceholderAppointment", description = "Check if user can schedule appointment for CR Visit with selecting clinician")
	public void crVisitAppointmentCreationTest() {
		crSteps.setDateTime("Notification", currentDateTime, true);
		crSteps.verifyDateTimeFromUi("Notification");
		crSteps.setDateTime("Schedule", futureDateTime, true);
		crSteps.verifyDateTimeFromUi("Schedule");	
		
		crSteps.setSubjectVisitComment(comment);
		crSteps.verifyCommentIsEntered(comment);
		
		crSteps.clickPickClinicianLink();
		crSteps.verifyClinicianCalenderIsPresent(true);
		crSteps.verifyPresenceOfClinicians(true, clinicians);
		crSteps.pickClinicianFirstTimeSlot(clinician, "Available", true);
		crSteps.scheduleAndVerifyConfirmMessage();
		
		crSteps.verifyVisitStatusInAppointmentDetails("Scheduled");
		crSteps.appointmentDetailsVerification(subjectName);
	}
	
	@Test(groups = { "UpdateScheduledCrVisit",
			"SFB-TC-1427" }, dependsOnGroups = "CrVisitAppointmentCreation", description = "Check if user can update scheduled CR visit appointment with new clinician and date/time")
	public void updateScheduledCrVisitTest() {
		crSteps.setDateTime("Schedule", futureDateTime, true);
		crSteps.clickPickClinicianLink();
		
		crSteps.pickClinicianFirstTimeSlot(newClinician, "Available", true);
		crSteps.verifyClinicianDisplayedAsSelected(true, newClinician);
		
		crSteps.deSelectClinicianTimeSlot(newClinician, "Available", futureDateTime);
		crSteps.verifyClinicianDisplayedAsSelected(false, newClinician);
		
		crSteps.pickClinicianFirstTimeSlot(newClinician, "Available", true);
		crSteps.reScheduleAndVerifyConfirmMessage(false);
		crSteps.appointmentDetailsVerification(subjectName);
		crSteps.reScheduleAndVerifyConfirmMessage(true);
		crSteps.appointmentDetailsVerification(subjectName);
		crSteps.verifyDateTimeFromUi("Schedule");	
	}
	
	@Test(groups = { "InitiateScheduledCrVisit",
			"SFB-TC-1438" }, dependsOnGroups = "UpdateScheduledCrVisit", description = "Check if user is able to initiate appointment for the scheduled CR Visit")
	public void initiateScheduledCrVisitTest() {
		crSteps.verifyInitiateAppointmentIsEnabled(false);
		crSteps.requiredFieldsValidation(true);
		crSteps.setSubjectAlias(alias);
		crSteps.verifyInitiateAppointmentIsEnabled(true);
		crSteps.initiateAndVerifyConfirmMessage(false);

		crSteps.appointmentDetailsVerification(subjectName);
		crSteps.verifyVisitStatusInAppointmentDetails("Scheduled");
		crSteps.initiateAndVerifyConfirmMessage(true);
		crSteps.appointmentDetailsVerification(subjectName);
		crSteps.verifyVisitStatusInAppointmentDetails("Initiated");
	}
	
	@Test(groups = { "RecallInitiatedCrVisit",
			"SFB-TC-1526" }, dependsOnGroups = "InitiateScheduledCrVisit", description = "Check if initiated appointment for CR visit can be recalled")
	public void recallInitiatedCrVisitTest() {
		crSteps.verifyRecallAssessmentIsEnabled(true);
		crSteps.recallAndVerifyConfirmMessage(false);
		crSteps.appointmentDetailsVerification(subjectName);
		crSteps.verifyRecallAssessmentIsEnabled(true);
		crSteps.recallAndVerifyConfirmMessage(true);
		crSteps.verifyVisitStatusInAppointmentDetails("Scheduled");
	}
	
	@Test(groups = { "CancelScheduledCrVisit",
			"SFB-TC-1414" }, dependsOnGroups = "RecallInitiatedCrVisit", description = "Check if scheduled appointment for CR visit can be canceled")
	public void cancelScheduledCrVisitTest() {
		crSteps.verifyCancelAppointmentIsEnabled(true);
		crSteps.cancelAndVerifyConfirmMessage(false);
		crSteps.appointmentDetailsVerification(subjectName);
		crSteps.verifyVisitStatusInAppointmentDetails("Scheduled");
		crSteps.verifyCancelAppointmentIsEnabled(true);
		crSteps.cancelAndVerifyConfirmMessage(true);
		crSteps.appointmentDetailsVerification(subjectName);
		crSteps.veifyScheduledDateIsCleared();
		crSteps.verifyVisitStatusInAppointmentDetails("Pending");
	}
}