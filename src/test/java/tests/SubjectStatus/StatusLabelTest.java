package tests.SubjectStatus;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Name: Subject Status - Label 
 * 
 * Jama ID: SFB-TC-737
 * 
 * Test Objective(s):
 * 	This test script checks displaying of subject status on different pages of the application.
 * 
 * Prerequisite(s):
 * 	1. A study with subjects having different subject status exists
 * 
 * Action & Expected Result:
 *	1. Open Subjects List view for the study
 *		- Read-only labels with last assigned subject status are displayed in the Status column for each subject
 * 		
 *	2. Open Subjects Details screen for a subject which has several statuses
 * 		- Read-only label with last assigned subject status is displayed
 * 
 *	3. Open Visits List view for the study
 * 		- Read-only labels with last assigned status of visit subject are displayed in the Subject Status column for each visit
 * 
 *	4. Open Visit Details page
 * 		- Read-only label with last assigned subject status is displayed
 * 
 * 	5. Repeat step 4 for all configured statuses
 * 		- Read-only label with last assigned subject status is displayed
 * 	
 * 	6. Open Assessments List view for the study
 * 		- Read-only labels with last assigned status of assessment subject are displayed in the Subject Status column for each assessment
 */
@Test(groups = { "StatusLabel" })
public class StatusLabelTest extends AbstractSubjectStatus{

//	String subjectName = "SubjectNew";
//	String newStatus = Constants.SUBJECTSTASUSES[0];

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(adminLogin, adminPasword, study, site);
		subjectSteps.openAllSubjects();
		subjectSteps.getSubjectTableData();
		subjectSteps.returnToDashboard(site);
		subjectSteps.openAllVisits();
		subjectSteps.getVisitTableData();
		subjectSteps.returnToDashboard(site);
		subjectSteps.openAllAssessments();
		subjectSteps.getAssessmentTableData();
		subjectSteps.returnToDashboard(site);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
	
	/**
	 * Before every test, log the method that is being run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
	}
	
	/**
	 * After every test, Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		subjectSteps.returnToDashboard(site);
		Log.logTestMethodEnd(method, result);
	}
	
	/**
	 * Data provider for Subject Visit & Status
	 * 
	 * @return Screening#, Visit#, Status
	 */
	@DataProvider
	public Object[][] subjectVisitStatusListData() {
		return new Object[][] { { "SubjectScreened", "Visit 2", Constants.SUBJECTSTASUSES[1] },
				{ "SubjectEnrolled", "Visit 3", Constants.SUBJECTSTASUSES[2] },
				{ "SubjectCompleted", "Visit 4", Constants.SUBJECTSTASUSES[3] },
				{ "SubjectDiscontinued", "Visit 5", Constants.SUBJECTSTASUSES[4] },
				{ "SubjectWithoutStatus", "Visit 1", "" } };
	}

	@Test(groups = { "SubjectStatusInListAndDetails",
			"SFB-TC-737" }, description = "Checks displaying of subject status on Subject/Visit/Assessment list And details pages of the application", dataProvider = "subjectVisitStatusListData")
	public void subjectStatusInListAndDetailsTest(String subject, String visit, String status) {
		subjectSteps.openAllSubjects();
		subjectSteps.subjectTableSubjectStatusVerification(subject, status);
		subjectSteps.openSubjectDetails(subject);
		subjectSteps.subjectDetailsSubjectStatusVerification(subject, status);
		subjectSteps.returnToDashboard(site);
		subjectSteps.openAllVisits();
		subjectSteps.visitTableSubjectStatusVerification(subject, visit, status, true);
		subjectSteps.openVisitDetails(subject, visit);
		subjectSteps.visitDetailsSubjectStatusVerification(subject, status);
		subjectSteps.returnToDashboard(site);
		subjectSteps.openAllAssessments();
		subjectSteps.assessmentTableSubjectStatusVerification(subject, visit, status, true);
	}
}
