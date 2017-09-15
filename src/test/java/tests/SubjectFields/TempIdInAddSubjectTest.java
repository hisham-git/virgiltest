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
 * Temporary ID appears as a subject identifier while adding a subject
 * 
 * Jama ID: SFB-TC-1781
 * 
 * Release: 2016.3
 * 
 * Test Objective(s):
 * 	Verify that Temporary ID appears as a subject identifier while adding a subject
 * 
 * Prerequisite(s):
 * 	1. At least 1 Study exists with temporary ID is added as a mandatory field
 * 	2. At least 1 site exists for the study
 * 	3. At least 1 user exists with appropriate claims
 * 
 * Action & Expected Result:
 *	1. Login to the portal application as the user of Pr#3
 *		- Login is successful
 * 		
 *	2. Select the option to add a new subject from the subject list page
 * 		- Add new subject screen is visible
 * 		- Temporary ID and Screening Number are highlighted
 * 
 *	3. Enter a screening number
 * 		- Temporary ID is disabled and no longer highlighted	
 * 
 *	4. Remove screening number and enter a Temporary ID
 * 		- Screening Number no longer highlighted
 * 
 * 	5. Save the subject by entering other required info
 * 		- Subject is saved
 * 		- Subject Number = Temporary ID of Step#4
 * 	
 * 	6. Add a visit to the subject of Step#5 and complete the visit as Not Administered
 * 		- Visit added to subject of Step#5
 * 		- Visit is completed 
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	7. Navigate to the visit list page and verify the entry of step#5 subject
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	8. Navigate to the visit details page of Step#6
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	9. Navigate to the subject list page and verify the entry of step#5 subject
 * 		- Subject Number = Temporary ID of Step#4
 * 
 * 	10. Navigate to the assessment list page and verify the entry of step#5 subject
 * 		- Subject Number = Temporary ID of Step#4
 */
@Test(groups = "TempIdInAddSubject")
public class TempIdInAddSubjectTest extends AbstractSubjectFields {
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenAllSubjects(siteportalUserAccountName, siteportalUserAccountPassword, study, site);
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
	
	@Test(groups = { "AddNewSubjectWithTempId",
			"SFB-TC-1781" }, description = "Check Temporary ID appears as a subject identifier while adding a subject ")
	public void addNewSubjectWithTempIdTest() {
		steps.addNewSubjectButtonVerification();
		steps.openNewSubjectForm(site);
		steps.verifyFieldIsRequired(SubjectDetailsFields.SCREENING.getValue(), true);
		steps.verifyFieldIsRequired(SubjectDetailsFields.TEMPID.getValue(), true);
		
		steps.fillingRequiredField(SubjectDetailsFields.SCREENING.getValue(), subjectName);
		steps.verifyFieldIsDisabled(SubjectDetailsFields.TEMPID.getValue(), true);
		steps.verifyFieldIsRequired(SubjectDetailsFields.TEMPID.getValue(), false);
		
		steps.clearRequiredField(SubjectDetailsFields.SCREENING.getValue());
		steps.fillingRequiredField(SubjectDetailsFields.TEMPID.getValue(), subjectName);
		steps.verifyFieldIsRequired(SubjectDetailsFields.SCREENING.getValue(), false);
		
		steps.selectRequiredDropdown(SubjectDetailsFields.LANGUAGE.getValue());
		steps.saveNewChanges();
		steps.subjectDetailsSubjectNameVerification(subjectName);
		
		steps.selectVisitInPosition(visitName, 0);
		templateSteps.assignTemplateToMe(visitName, 1, false, true);
		assessmentSteps.makeTemplateNotAdministered(siteportalUserAccountName, siteportalUserAccountPassword);
		steps.returnToSubjectDetails(subjectName);
		steps.visitStatusVerification("Complete", visitName, visitPos);
		steps.subjectDetailsSubjectNameVerification(subjectName);
	}
	
	@Test(groups = { "TempIdInSubjectListAndDetails",
			"SFB-TC-1781" }, dependsOnGroups = "AddNewSubjectWithTempId", description = "Check Temporary ID appears as a subject identifier in Subject List & Details page")
	public void tempIdInSubjectListAndDetailsTest() {
		steps.openAllSubjects();
		steps.verifySubjectWithTemporaryIdInSubjectList(subjectName);
	}

	@Test(groups = { "TempIdInVisitListAndDetails",
			"SFB-TC-1781" }, dependsOnGroups = "TempIdInSubjectListAndDetails", description = "Check Temporary ID appears as a subject identifier in Visit List & Details page")
	public void tempIdInVisitListAndDetailsTest() {
		steps.openAllVisits();
		steps.verifySubjectWithTemporaryIdInViistList(subjectName);
		steps.openVisitDetails(subjectName, visitName);
		steps.visitDetailsSubjectNameVerification(subjectName);
	}

	@Test(groups = { "TempIdInAssessmentListAndDetails",
			"SFB-TC-1781" }, dependsOnGroups = "TempIdInVisitListAndDetails", description = "Check Temporary ID appears as a subject identifier in Assessment List & Details page")
	public void tempIdInAssessmentListAndDetailsTest() {
		steps.openAllAssessments();
		steps.verifySubjectWithTemporaryIdInAssessmentList(subjectName);
		steps.openAssessmentDetails(subjectName, visitName);
		steps.assessmentDetailsSubjectNameVerification(subjectName);
	}
}
