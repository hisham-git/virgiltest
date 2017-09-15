package tests.SubjectFields;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.Enums.SubjectDetailsFields;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Temporary ID appears as a subject identifier while editing a subject
 * 
 * Jama ID: SFB-TC-1779
 * 
 * Release: 2016.3
 * 
 * Test Objective(s):
 * 	Verify that Temporary ID appears as a subject identifier while editing a subject
 * 
 * Prerequisite(s):
 * 	1. At least 1 Study exists with temporary ID is added as a mandatory field
 * 	2. At least 1 site exists for the study
 * 	3. At least 1 user exists with appropriate claims
 * 	4. At least 1 subject exists for the study site with a screening number and without any randomization number
 * 	5. At least 1 subject visit exists
 * 
 * Action & Expected Result:
 *	1. Login to the portal application as the user of Pr#3
 *		- Login is successful
 * 		
 *	2. Navigate to the subject details page of Pr#3
 * 		- Temporary ID field is visible and is disabled
 * 
 *	3. Remove the screening number of Step#2
 * 		- Screening number is removed
 * 		- Temporary ID is enabled
 * 		- Both Screening Number and Temporary ID fields become required and highlighted
 * 
 *	4. Enter a Temporary ID
 * 		- Screening Number no longer highlighted
 * 
 * 	5. Save the subject
 * 		- Subject is saved
 * 		- Subject Number = Temporary ID of Step#4
 * 	
 * 	6. Navigate to the subject list page and verify the entry of step#5 subject
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	7. Navigate to the visit list page and verify the entry of step#5 subject
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	8. Navigate to the visit details page of Step#7
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	9. Navigate to the assessment list page and verify the entry of step#5 subject
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	10. Navigate to the assessment details page of an assessment of Pr#4 visit
 * 		- Subject Number = Temporary ID of Step#4
 */
@Test(groups = "TempIdInEditSubject")
public class TempIdInEditSubjectTest extends AbstractSubjectFields {
    
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenAllSubjects(siteportalUserAccountName, siteportalUserAccountPassword, study, site);
		steps.openSubjectDetails(subjectName);
		steps.clickOnEditSubjectButton();
		steps.editSubjectFieldValue(SubjectDetailsFields.TEMPID.getValue(), "");
		steps.editSubjectFieldValue(SubjectDetailsFields.SCREENING.getValue(), subjectName);
		steps.saveChangesWithEsign(siteportalUserAccountName, siteportalUserAccountPassword);
		steps.returnToDashboard();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
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
		steps.returnToDashboard();
		Log.logTestMethodEnd(method, result);
	}
	
	@Test(groups = { "EditSubjectWithTempId",
			"SFB-TC-1779" }, dependsOnGroups = "TempIdInAssessmentListAndDetails", description = "Check Temporary ID appears as a subject identifier while editing a subject")
	public void editSubjectWithTempIdTest() {
		steps.openAllSubjects();
		steps.openSubjectDetails(subjectName);
		steps.clickOnEditSubjectButton();
		steps.verifyFieldIsVisible(SubjectDetailsFields.TEMPID.getValue(), true);
		steps.verifyFieldIsDisabled(SubjectDetailsFields.TEMPID.getValue(), true);
		
		steps.editSubjectFieldValue(SubjectDetailsFields.SCREENING.getValue(), "");
		steps.verifyFieldIsDisabled(SubjectDetailsFields.TEMPID.getValue(), false);
		steps.verifyFieldIsRequired(SubjectDetailsFields.SCREENING.getValue(), true);
		steps.verifyFieldIsRequired(SubjectDetailsFields.TEMPID.getValue(), true);
		
		steps.editSubjectFieldValue(SubjectDetailsFields.TEMPID.getValue(), subjectName);
		steps.verifyFieldIsRequired(SubjectDetailsFields.SCREENING.getValue(), false);
		
		steps.saveChangesWithEsign(siteportalUserAccountName, siteportalUserAccountPassword);
		steps.subjectDetailsSubjectNameVerification(subjectName);
	}
	
	@Test(groups = { "EditedTempIdInSubjectListAndDetails",
			"SFB-TC-1779" }, dependsOnGroups = "EditSubjectWithTempId", description = "Check edited Temporary ID appears as a subject identifier in Subject List & Details page")
	public void editedTempIdInSubjectListAndDetailsTest() {
		steps.openAllSubjects();
		steps.verifySubjectWithTemporaryIdInSubjectList(subjectName);
	}

	@Test(groups = { "EditedTempIdInVisitListAndDetails",
			"SFB-TC-1779" }, dependsOnGroups = "EditedTempIdInSubjectListAndDetails", description = "Check edited Temporary ID appears as a subject identifier in Visit List & Details page")
	public void editedTempIdInVisitListAndDetailsTest() {
		steps.openAllVisits();
		steps.verifySubjectWithTemporaryIdInViistList(subjectName);
		steps.openVisitDetails(subjectName, visitName);
		steps.visitDetailsSubjectNameVerification(subjectName);
	}

	@Test(groups = { "EditedTempIdInAssessmentListAndDetails",
			"SFB-TC-1779" }, dependsOnGroups = "EditedTempIdInVisitListAndDetails", description = "Check edited Temporary ID appears as a subject identifier in Assessment List & Details page")
	public void editedTempIdInAssessmentListAndDetailsTest() {
		steps.openAllAssessments();
		steps.verifySubjectWithTemporaryIdInAssessmentList(subjectName);
		steps.openAssessmentDetails(subjectName, visitName);
		steps.assessmentDetailsSubjectNameVerification(subjectName);
	}
}
