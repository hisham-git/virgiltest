package tests.AssessmentDetails;

import java.lang.reflect.Method;

import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Test Case : SFB-TC-910 Assessment details - Ability to edit Assessment details
 * link - https://k8d.jamacloud.com/perspective.req#/testCases/4248986?projectId=29316
 * 
 * Test Objective(s): 
 *	Editing is available in cases ClinRO/PRO/ObsRO form has status Submitted
 * 	and form marked as "Not Complete" or "Not administered" or "Paper Transcription"
 * 
 * Prerequisite(s):
 * 	1. At least one submitted Not Administred ClinRO form assessment exists for a Study
 * 	2. At least one submitted with Not Completed status PRO form assessment exists for a Study
 * 	3. At least one submitted with Not Completed status ObsRO form assessment exists for a Study
 * 	4. At least one submitted Paper transcription ObsPRO form assessment exists for a Study
 * 	5. At least one submitted Paper transcription ClinRO form assessment exists for a Study
 * 	6. At least one submitted Paper transcription PRO form assessment exists for a Study
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "EditDetails", "E-Sign" })
public class EditDetailsTests extends AbstractAssessmentDetails {

	/**
	 * Before Test class navigate to Assessments List
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
//		commonSteps.getToStudyDashboard();
		toolBarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		detailsSteps.getToAssessmentList();
	}
	
	/**
	 * Returns an array of Objects with two properties : {Assessment Name} and {SVID}
	 * 
	 * @return Object[][]{{String, String}}
	 */
	@DataProvider
	public Object[][] assessments() {
		return new Object[][] { 
			{ "CDR", "Visit1", "ClinRO", "Not Administered" },
			{ "CAM", "Visit3", "PRO", "Not Completed" },
			{ "eCog - ObsRO", "Visit5", "ObsRO", "Not Completed" },
			{ "CGI-I", "Visit8", "ClinRO", "Paper transcription" },
			{ "eCog - ObsRO", "Visit6", "ObsRO", "Paper transcription" },
			{ "CAM", "Visit4", "PRO", "Paper transcription" }
		};
	}

	/**
	 * Steps : 
	 * 	1. Navigate to a particular Assessment Details Page (the criteria for choice are given in the requirements documentation in JAMA
	 * 	2. Check if the Edit button is present
	 * 	3. Click Edit Button
	 * 	4. Check if the Save, Cancel, Started Time and Date are activated
	 * 	5. Change the Started Date and Time
	 * 	6. Click Cancel
	 * 	7. Check that the Started Date and Time was not changed
	 * 	8. Click Edit
	 * 	9. Change Started Date and Time
	 * 	10.Click Save
	 * 	11.Check if Time and Date are changed
	 * 
	 * 
	 * @param assessmentName String, name of the Assessment to open from Assessment List
	 * @param svid	String, SVID of the Assessment to open from Assessment List
	 */
	@Test(groups = { "EditAssessmentDetails",
			"SFB-TC-910" }, description = "Check the Edit Functionalities of forms marked as \"Not Complete\" or \"Not administered\" or \"Paper Transcription\" as in SFB-TC-910", dataProvider = "assessments")
	public void editAssessmentDetailsTest(String assessmentName, String visitName, String formType, String chekedAs) {
		Log.logInfo("Starting edit details test for Form: [" + assessmentName + "] with Type: [" + formType
				+ "] and marked as: " + chekedAs);
//		detailsSteps.getToAssessmentDetails(assessmentName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
//		detailsSteps.verifyPresenceOfEditButton(assessmentName, svid);
		detailsSteps.verifyPresenceOfEditButton(assessmentName, visitName);
		detailsSteps.clickEditButton();
		detailsSteps.verifyControlsActivated();
		detailsSteps.editStartedDateTime();
		detailsSteps.clickCancelButton();
		detailsSteps.verifyStartedDateTimeUnchanged();
		detailsSteps.clickEditButton();
		detailsSteps.editStartedDateTime();
		detailsSteps.clickSaveButton();
		esignSteps.esignDialogPredefinedReason(dropDownvalue);
		esignSteps.esignDialogConfirm(siteportalUserAccountName, siteportalUserAccountPassword);
		detailsSteps.verifyStartedDateTimeChanged();
	}

	/**
	 * After every method, go back to the Assessment Lists Page
	 * 
	 * @param method
	 */
	@Override
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		toolBarFull.returnToAllAssesments();
//		detailsSteps.navigateBackToAssessmentList();
		Log.logTestMethodEnd(method, result);
	}	
}
