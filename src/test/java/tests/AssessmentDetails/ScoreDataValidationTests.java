package tests.AssessmentDetails;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * Test Class to check the Scores on the Score Tab, by checking in the FDEF file, Database and UI
 * 
 * @author Abdullah Al Hisham
 */
@Test(groups = { "ScoreDataValidation" })
public class ScoreDataValidationTests extends AbstractAssessmentDetails {

	/**
	 * Before all the Tests are executed, Go to All Assessments Parse the FDEF
	 * file & store the value for required for later test executions
	 */
	
	private int svid = 0;
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
		commonSteps.getToStudyDashboard();
		detailsSteps.getToAssessmentList();
		detailsSteps.parseFDEF();
		
	}

	/**
	 * Returns an array of Object of the type {String, Integer} which correspond
	 * to {Assessment Name} and {SVID} respectively
	 * 
	 * @return Object[][] - containing object of the format {String, Integer}
	 */
	/*@DataProvider
	private Object[][] assessmentNamesAndSVIDs() {
		return new Object[][] { 
			{ "MMSE", new Integer(214) },
			{ "MMSE", new Integer(215) },
		};
	}*/
	
	@DataProvider
	private Object[][] assessmentNamesAndSVIDs() {
		return new Object[][] { 
			{ "CDR","Visit2" }
			
		};
	}

	/**
	 * Steps:
	 * 1. Navigate to a particular Assessment Details Page (the criteria for choice are given in the requirements documentation in JAMA
	 * 2. Check if the Scores Tab Link is present
	 * 3. Click on Scores Tab Link
	 * 4. Checks that the FDEF file was parsed correctly to be displayed on the UI
	 * 5. Checks that the Score's Answers are one of the possible answers as specified in the FDEF file
	 * 6. Checks that the Database and UI displayed is consistent
	 */
	@Test(groups = { "QuestionGroupingsFDEFtoUI",
			"JamaNA" }, description = "Checks Scores tab fields & values against FDEF & DB", dataProvider = "assessmentNamesAndSVIDs")
//	public void questionGroupingsFDEFtoUITest(String scoreDataformName, Integer svid) {
	public void questionGroupingsFDEFtoUITest(String assessmentName, String visitName) {
//		detailsSteps.getToAssessmentDetails(scoreDataformName, svid.toString());
		svid = detailsSteps.getSVIDFromAssessment(subjectName, assessmentName, visitName);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
		detailsSteps.clickScoresTabLink();
		detailsSteps.verifyQuestionGroupingsFDEFtoUI();
		detailsSteps.verifyAnswerOptionsFDEFtoUI();
//		detailsSteps.getToAssessmentList();
//		detailsSteps.verifyQuestionGroupingsDBtoUI(assessmentName, svid);
		detailsSteps.verifyQuestionGroupingsDBtoUI(assessmentName,svid );
		
		detailsSteps.navigateBackToAssessmentList();
	}
}