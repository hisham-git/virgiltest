package tests.AssessmentDetails;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Test Case SFB-TC-808, 
 * link - https://k8d.jamacloud.com/perspective.req#/testCases/4216679?projectId=29316
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "Controls" })
public class ControlsTests extends AbstractAssessmentDetails {
	
	
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
//		commonSteps.getToStudyDashboard();
	}
	
	/**
	 * Before Every Test method
	 * 
	 * @param method - The method that is starting
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		commonSteps.getToStudyDashboard();
		detailsSteps.getToAssessmentList("Complete");
	}
	
	/**
	 * Returns an array of Object of the type {String, String, String} which correspond to {Assessment Name}, {SVID} and {Assessment Type} respectively
	 * 
	 * @return Object[][] - containing object of the format {String, String, String}
	 */
	@DataProvider
	private Object[][] assessmentNamesAndSVIDs(){
		return new Object[][]{
			{"CDR","Visit1",  "ClinRO"},
			{"CAM", "Visit3", "PRO"},
			{"ECog - ObsRO", "Visit5", "ObsRO"}
		};
	}

	/**
	 * Description - Check for the presence of the Controls and appropriate Data for the ObsRO/ClinRO/PRO forms
	 * Test Case : SFB-TC-808
	 * 
	 * Steps : 
	 * 	1. Click on the assessment as defined by the parameters from the Assessment List
	 * 	2. Initialize the correct PageObject depending on the kind of assessment type specified in the parameters
	 * 	3. Check if the Header format, content, refresh button and View Mode buttons functions works correctly
	 * 	4. Check if the Details Grid contains the Fields and Links as appropriate for the type of Assessment
	 * 	5. Check if the Versions Table contains the Data as appropriate for the type of Assessment
	 * 	6. Check if the Scores Tab contains the Fields and Controls as appropriate for the type of Assessment
	 * 	7. Check if the Attachments Tab contains the Fields and Controls as appropriate for the type of Assessment
	 * 
	 * @param assessmentName the name of the Assessment
	 * @param svid The SVID of the Assessment
	 * @param assessmentType the type of Assessment it should be
	 */
	@Test(groups = { "CompletedAssessmentControls",
			"SFB-TC-808" }, description = "Check for the presence of the Controls and appropriate Data for the ObsRO/ClinRO/PRO forms", dataProvider = "assessmentNamesAndSVIDs")
	public void completedAssessmentControlsTest(String assessmentName, String visitName, String assessmentType) {
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
		detailsSteps.verifyHeader(assessmentType);
		detailsSteps.verifyDetailsGrid(assessmentType);
		detailsSteps.verifyVersionsTable(assessmentType);
		// TODO: Disabled due to unclear requirement
		// detailsSteps.verifyScoresTab(assessmentType);
	}
}
