package tests.AssessmentDetails;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.details.RaterDetails;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 *  
 * Test Objective(s):
 * - To test the general assessment info(status, language)
 * - To test the general page actions (refresh, links)
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "General" })
public class GeneralTests extends AbstractAssessmentDetails {
	
	protected RaterDetails raterDetails;
	
	/**
	 * Before Test class 
	 * 1. Go to 'Paper Transcription' Assessments list
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
		/*commonSteps.getToStudyDashboard();
		detailsSteps.getToAssessmentList("Paper Transcription");*/
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		commonSteps.getToStudyDashboard();
		detailsSteps.getToAssessmentList("Complete");
	}
	
	/**
	 * @return Form Name, SVID, Language
	 */
	@DataProvider
	public Object[][] assessmentStatusWithLanguage(){
		return new Object[][] {
			{ "CGI-I", "Visit8", "English (US)" }
		};
	}

	/**
	 * Steps:
	 * 1. Goto Assessment List
	 * 2. Collect Assessment status from List
	 * 3. Goto Assessment Details page for given data
	 * 3. Verify status with Assessment Details page
	 * 4. Verify Assessment language with given data
	 * 
	 * @param formName
	 * @param visitName
	 * @param language
	 */
	@Test(enabled = true, groups = { "AssessmentStatusWithLanguage",
			"JamaNA" }, description = "Verify the Assessment Status and Language from the Assessment Details page.", dataProvider = "assessmentStatusWithLanguage")
	public void assessmentStatusWithLanguageTest(String formName, String visitName, String language) {
		// detailsSteps.getStatusFromAssessmentList(formName, svid);
		detailsSteps.getStatusFromAssessmentList(subjectName, formName, visitName);
		// detailsSteps.getToAssessmentDetails(formName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.verifyAssessmentStatus();
		detailsSteps.verifyAssessmentLanguage(language);
	}
	
	/**
	 * Steps:
	 * 1. Verify Paper Transcription Assessment by checking Paper Transcription checkbox details page 
	 * 
	 * @param formName
	 * @param svid
	 */
	@Test(enabled = true, groups = { "PaperTranscriptionAssessment",
			"JamaNA" }, description = "Verify Paper Transcription ckeckbox is checked for a paper transcription assessment.", dataProvider = "assessmentStatusWithLanguage")
	public void paperTranscriptionAssessmentTest(String formName,String visitName, String lang) {
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.verifyPaperTranscriptionCheckbox();
	}
	
	/**
	 * Steps:
	 * 1. Click Study Link on Assessment Details page
	 * 2. Verify if Study Profile page is opened
	 * 3. Close Study Profile page
	 * 4. Verify if Study Profile page closed & back to Assessment Details page
	 */
	@Test(enabled = true, groups = { "StudyLinkOnDetailsGrid",
			"JamaNA" }, description = "Verify the Study link on the Assessment Details page.", dataProvider = "assessmentStatusWithLanguage")
	public void studyLinkOnDetailsGridTest(String formName,String visitName, String lang) {
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.clickStudyLink();
		detailsSteps.verifyStudyProfileOpened();
		detailsSteps.closeStudyProfile();
		detailsSteps.verifyStudyProfileClosed();
	}
	
	/**
	 * Steps:
	 * 1. Collect Subject Name from Assessment Details page
	 * 2. Click on Subject link
	 * 3. Verify if Subject Details page is opened
	 * 4. Collect Subject Name from Subject Details page
	 * 5. Verify Subject name from Assessment Details page matches with name from Subject Details page 
	 * 6. Navigate back to Assessment Details page for next test
	 */
	@Test(enabled = true, groups = { "SubjectLinkOnDetailsGrid",
			"JamaNA" }, description = "Verify the Subject link on the Assessment Details grid.", dataProvider = "assessmentStatusWithLanguage")
	public void subjectLinkOnDetailsGridTest(String formName,String visitName, String lang) {
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.getSubjectNameOnAssessmentDetailsPage();
		detailsSteps.clickSubjectLink();
		detailsSteps.verifySubjectDetailsOpened();
		detailsSteps.getSubjectNameOnSubjectDetailsPage();
		detailsSteps.verifySubjectNameMatched();
//		detailsSteps.navigateBackToAssessmentDetails();
	}
	
	/**
	 * 
	 * @return Assessment Form Name, SVID 
	 */
	@DataProvider
	public Object[][] assessmentListFiler(){
		return new Object[][] {
			{ "CGI-I","Visit8" }
		};
	}

	/**
	 * 1. Click on Visit link from Assessment Details screen
	 * 2. Verify if Visit Details page is opened with given SVID
	 * 3. Navigate back to Assessment Details page for next test
	 * 
	 * @param formName
	 * @param svid
	 */
	@Test(enabled = true, groups = { "VisitLinkOnDetailsGrid",
			"JamaNA" }, description = "Verify the Visit link on the Assessment Details grid.", dataProvider = "assessmentListFiler")
	public void visitLinkOnDetailsGridTest(String formName, String visitName) {
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.clickGridVisitLink();
		// detailsSteps.verifyVisitDetailsOpened(svid, "Grid");
		detailsSteps.verifyVisitDetailsOpened(formName, visitName, "Grid");
//		detailsSteps.navigateBackToAssessmentDetails();
	}

	/**
	 * 1. Click on Rater link from Assessment Details screen
	 * 2. Verify if Rater Details page is opened with given SVID
	 * 3. Navigate back to Assessment Details page for next test
	 * 
	 * @param formName
	 * @param visitName
	 */
	@Test(enabled = true, groups = { "RaterLinkOnDetailsGrid",
			"JamaNA" }, description = "Verify the Rater link on the Assessment Details grid.", dataProvider = "assessmentListFiler")
	public void raterLinkOnDetailsGridTest(String formName, String visitName) {		
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.selectAllDetailsViewMode();
		detailsSteps.getRaterNameOnAssessmentDetailsPage();
		detailsSteps.clickGridRaterLink();
		detailsSteps.verifyRaterDetailsOpened("Grid");
//		detailsSteps.navigateBackToAssessmentDetails();
		// detailsSteps.navigateBackToAssessmentList();
	}
	
	/**
	 * @return Form Name, SVID, Rater
	 */
	@DataProvider
	public Object[][] assessmentRater(){
		return new Object[][] { 
			{ "CDR", "Visit1", "Percival Nathans" },
			{ "CGI-I", "Visit8", "Percival Nathans" }
		};
	}
	
	/**
	 * Steps:
	 * 1. Verify the Raters name on AssessmentDetails Page with given name
	 * 
	 * @param formName
	 * @param paramMode
	 */
	@Test(enabled = true, groups = { "RatersName",
			"JamaNA" }, description = "Verify the Rater's Name for a self assigned assessment.", dataProvider = "assessmentRater")
	public void ratersNameTest(String fromName, String visitName, String raterName) {
//		detailsSteps.getToAssessmentList("With Queries");
//		detailsSteps.getToAssessmentList("Complete");
//		detailsSteps.getToAssessmentDetails(fromName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, fromName);
		detailsSteps.getRaterNameOnAssessmentDetailsPage();
		detailsSteps.verifyRaterNameWithUI(raterName);
	}
}
