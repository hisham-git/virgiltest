package tests.AssessmentDetails;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * Test Class containing test cases - SFB-TC-843, SFB-TC-845, SFB-TC-850, SFB-TC-851
 * Checks for the correct functionality of the Scores Tab, Video Player, Audio Player and PDF Viewer
 * for their corresponding view modes
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "ViewMode" })
public class ViewModeTests extends AbstractAssessmentDetails {
	
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
		commonSteps.getToStudyDashboard();
	}
	
	/**
	 * Before every method, route to the Assessment (Complete) List
	 * @param Method - the method that will begin
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		detailsSteps.getToAssessmentList();
	}
	
	/**
	 * Data provider for Video Assessment
	 * 
	 * @return Form Name, Visit, View Mode
	 */
	@DataProvider
	public Object[][] videoAssessmentDataSource(){
		return new Object[][] { 
			{ "CGI-I", "Visit8", "Video + Source" }
		};
	}

	/**
	 * Test Objectives: Play video and view 'PDF' template on assessment details
	 * page Layout of 'Video+Source' mode Switching between 'Video+Source' mode
	 * and 'All details' mode
	 * 
	 * Steps : 
	 * 	1. Select the specified Assessment from the Assessment List Page
	 * 	2. Check that the Default view mode is "All Details"
	 * 	3. Check that the View Modes in the View Mode Dropdown are correct
	 * 	4. Select View Mode Video + Source from the View Modes Dropdown
	 * 	5. Check that the PDF Viewer and the Video Player are available
	 * 	6. Scroll to page 3 on the PDF Viewer and play the Video for some time
	 * 	7. Go to "All Details" View Mode
	 * 	8. Go back to "View + Source" View Mode
	 * 	9. Check that the PDF Viewer is at page three and Video Player's timeline is where it was last
	 * 10. Click Refresh
	 * 11. Check that the PDF Viewer shows page 1
	 * 12. Check that the Video Player's timeline starts at the beginning 
	 */
	@Test(enabled = false, groups = { "VideoPlusSourceMode",
			"SFB-TC-843" }, description = "Check Video Player and PDF Source Viewer from the Assessment Details Video+Source View Mode.", dataProvider = "videoAssessmentDataSource")
	public void videoPlusSourceModeTest(String formName, String visitName, String viewMode) {
//		detailsSteps.getToAssessmentDetails(formName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.verifyDefaultViewModeAllDetails();
		detailsSteps.verifyAllVideoViewModes();
		detailsSteps.selectViewMode(viewMode);
		detailsSteps.getAssessmentDetailsCurrentViewMode();
		detailsSteps.verifyCurrentViewMode(viewMode);
		detailsSteps.scrollPageOnPdfViewr("3");
		detailsSteps.playVideoForSomeTime(5);
		detailsSteps.clickCancelModeButton(viewMode);
		detailsSteps.selectViewMode(viewMode);
		detailsSteps.verifyPdfPageNumberNotChanged("3");
		detailsSteps.verifyVideoPlayerTimelineNotChanged();
		detailsSteps.clickRefresh();
		detailsSteps.verifyPdfPageNumberIsAtBeginnig();
		detailsSteps.verifyVideoPlayerTimelineIsAtBeginnig();
	}
	
	/**
	 * Data provider for Video Assessment
	 * 
	 * @return Form Name, visitName, View Mode
	 */
	@DataProvider
	public Object[][] videoAssessmentDataScores(){
		return new Object[][] { 
			{ "CGI-I", "Visit8", "Video + Scores" }
		};
	}
	
	/**
	 * Test Objectives: Play video and view scores on assessment details page
	 * Layout of 'Video+Scores' mode Switching between 'Video+Scores' mode and
	 * 'All details' mode
	 * 
	 * Steps : 
	 * 	1. Select the specified Assessment from the Assessment List Page
	 * 	2. Check that the Default view mode is "All Details"
	 * 	3. Check that the View Modes in the View Mode Dropdown are correct
	 * 	4. Select View Mode Video + Scores from the View Modes Dropdown
	 * 	5. Check that the Scores Tab and the Video Player are available
	 * 	6. Play the Video for some time
	 * 	7. Go to "All Details" View Mode
	 * 	8. Go back to "View + Source" View Mode
	 * 	9. Check that the Video Player's timeline is where it was last
	 * 10. Click Refresh
	 * 11. Check that the Video Player's timeline starts at the beginning 
	 */
	@Test(enabled = false, groups = { "VideoPlusScoreMode",
			"SFB-TC-845" }, description = "Check Video Player and Scores Tab from the Assessment Details Video+Score View Mode", dataProvider = "videoAssessmentDataScores")
	public void videoPlusScoreModeTest(String formName, String visitName, String viewMode) {
//		detailsSteps.getToAssessmentDetails(formName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.verifyDefaultViewModeAllDetails();
		detailsSteps.verifyAllVideoViewModes();
		detailsSteps.selectViewMode(viewMode);
		detailsSteps.getAssessmentDetailsCurrentViewMode();
		detailsSteps.verifyCurrentViewMode(viewMode);
		detailsSteps.playVideoForSomeTime(5);
		detailsSteps.clickCancelModeButton(viewMode);
		detailsSteps.selectViewMode(viewMode);
		detailsSteps.verifyVideoPlayerTimelineNotChanged();
		detailsSteps.selectViewMode("All Details");
		detailsSteps.selectViewMode(viewMode);
		detailsSteps.verifyVideoPlayerTimelineNotChanged();
		detailsSteps.clickRefresh();
		detailsSteps.verifyVideoPlayerTimelineIsAtBeginnig();
	}
	
	/**
	 * Data provider for Audio Assessment
	 * 
	 * @return Form Name, visitName, View Mode
	 */
	@DataProvider
	public Object[][] audioAssessmentDataScores(){
		return new Object[][] { 
			{ "CDR", "Visit2", "Source + Scores" }
		};
	}

	/**
	 * Test Objectives: View 'PDF' template and Scores on assessment details
	 * page Layout of 'Source+Scores' mode Switching between 'Source+Scores'
	 * mode and 'All details' mode Audio playback on 'Source+Scores' mode
	 * 
	 * Steps : 
	 * 	1. Select the specified Assessment from the Assessment List Page
	 * 	2. Check that the Default view mode is "All Details"
	 * 	3. Check that the View Modes in the View Mode Dropdown are correct
	 * 	4. Select View Mode Source + Scores from the View Modes Dropdown
	 * 	5. Check that the Scores Tab and the PDF Viewer are available
	 * 	6. Check that the Audio Player is visible and enabled
	 * 	7. Play the Audio for some time
	 * 	8. Scroll the PDF Viewer to Page 3
	 * 	9. Close the View Mode
	 * 10. Check that the Audio player's timeline stays the same
	 * 11. Click on the Form PDF
	 * 12. Check that the Audio Player's timeline is the same
	 * 13. Check that the PDF Viewer is at page 3
	 * 14. Click Refresh button
	 * 15. Check that the PDF Viewer and the Audio player are refreshed
	 */
	@Test(groups = { "AudioSourcePlusScores",
			"SFB-TC-850" }, description = "Check Audio Player, PDF Viewer and Scores Tab from the Assessment Details Source+Score (with Audio) View Mode.", dataProvider = "audioAssessmentDataScores")
	public void audioSourcePlusScoresTest(String formName, String visitName, String viewMode) {
//		detailsSteps.getToAssessmentDetails(formName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.verifyDefaultViewModeAllDetails();
		detailsSteps.verifyAllAudioViewModes();
		detailsSteps.selectViewMode(viewMode);
		detailsSteps.getAssessmentDetailsCurrentViewMode();
		detailsSteps.verifyCurrentViewMode(viewMode);
		detailsSteps.verifyPresenceOfAudioPlayer();
		detailsSteps.playAudioForSomeTime(3);
		detailsSteps.scrollPageOnPdfViewr("3");
		detailsSteps.clickCancelModeButton(viewMode);
		detailsSteps.verifyAudioPlayerTimelineNotChanged();
		detailsSteps.clickOnFormPDF();
		detailsSteps.verifyPdfPageNumberNotChanged("3");
		detailsSteps.clickRefresh();
		detailsSteps.verifyAudioPlayerTimelineIsAtBeginnig();
		detailsSteps.verifyPdfPageNumberIsAtBeginnig();
	}
	
	/**
	 * Data provider for Assessment
	 * 
	 * @return Form Name, visitName, View Mode
	 */
	@DataProvider
	public Object[][] assessmentDataScores(){
		return new Object[][] { 
			{ "CGI-I", "Visit8", "Source + Scores" }
		};
	}	

	/**
	 * Test Objectives: View 'PDF' template and Scores on assessment details
	 * page Layout of 'Source+Scores' mode Switching between 'Source+Scores'
	 * mode and 'All details' mode
	 * 
	 * Steps : 
	 * 	1. Select the specified Assessment from the Assessment List Page
	 * 	2. Check that the Default view mode is "All Details"
	 * 	3. Check that the View Modes in the View Mode Dropdown are correct
	 * 	4. Select View Mode Source + Scores from the View Modes Dropdown
	 * 	5. Check that the Scores Tab and the PDF Viewer are available
	 * 	6. Scroll the PDF Viewer to Page 3
	 * 	7. Close the View Mode
	 * 	8. Check that the Audio player's timeline stays the same
	 *  9. Click on the Form PDF
	 * 10. Check that the Audio player's timeline is the same
	 * 11. Check that the PDF Viewer is at page 3
	 * 12. Click Refresh button
	 * 13. Check that the PDF Viewer and the Audio player are refreshed
	 */
	@Test(groups = { "SourcePlusScores",
			"SFB-TC-851" }, description = "Ceck PDF Viewer and Scores Tab from the Assessment Details Source+Score (with Audio) View Mode.", dataProvider = "assessmentDataScores")
	public void sourcePlusScoresTest(String formName, String visitName, String viewMode) {
//		detailsSteps.getToAssessmentDetails(formName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, formName);
		detailsSteps.verifyDefaultViewModeAllDetails();
		detailsSteps.verifyAllViewModes();
		detailsSteps.selectViewMode(viewMode);
		detailsSteps.getAssessmentDetailsCurrentViewMode();
		detailsSteps.verifyCurrentViewMode(viewMode);
		detailsSteps.scrollPageOnPdfViewr("3");
		detailsSteps.clickCancelModeButton(viewMode);
		detailsSteps.clickOnFormPDF();
		detailsSteps.verifyPdfPageNumberNotChanged("3");
		detailsSteps.clickRefresh();
		detailsSteps.verifyPdfPageNumberIsAtBeginnig();
	}
}
