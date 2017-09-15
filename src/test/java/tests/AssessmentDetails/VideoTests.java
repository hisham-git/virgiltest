package tests.AssessmentDetails;

import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.VideoPlayerHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Test Objective(s):
 * - To test all the properties for the Video Player for Assessment details pages
 * 
 * @author Abdullah Al Hisham
 * 
 */
@Test(groups = { "Video" })
public class VideoTests extends AbstractAssessmentDetails {
	
	/*
	 * Configuration Parameters
	 */
	private final String ASSESSMENT_NAME = "CGI-I", SVID = "214";

	/**
	 * Before Test class 
	 * 1. Go to All Assessments list
	 * 2. click an assessment with given name & svid 
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
		commonSteps.getToStudyDashboard();
		toolBarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		detailsSteps.getToAssessmentList();
		/*detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(ASSESSMENT_NAME, SVID);*/
	}
	
	/**
	 * Before Every Method, go to the specified Assessment and 
	 * click on the Video Player Icon. Log it.
	 * 
	 * @param method - Method, the Method to be run.
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		detailsSteps.getToAssessmentDetails(ASSESSMENT_NAME, SVID);
		detailsSteps.clickPlayVideo();
		detailsSteps.instantiateVPHelper();
	}
	
	/**
	 * Description - Check that the controls on the Video Player work correctly
	 * 
	 * Steps - 
	 * 	1. Check that the initial State of the Video player is PAUSED
	 * 	2. Click Play button
	 * 	3. Check that the state of the Video player is Playing
	 * 	4. Click the Pause button
	 * 	5. Check the State of the Video Player is Paused
	 * 	6. Check that the volume is at 100%
	 * 	7. Click the mute button
	 * 	8. Check that the volume is at 0%
	 * 	9. Click the mute button
	 * 10. Check that the volume is at 100%
	 * 
	 */
	@Test(priority = 1, groups = { "VideoPlayerControls",
			"JamaNA" }, description = "Check the Video Player UI Controls.")
	public void checkVideoPlayerControlsTest() {
		Log.logInfo("Waiting for the page and video player to load...");
		detailsSteps.verifyVideoPlayerInitialStatusPaused();
		detailsSteps.waitForVideoBuffering();
		detailsSteps.verifyPlayingStatusAfterClickingPlay();
		detailsSteps.verifyPausedStatusAfterClickingPause();
		detailsSteps.verifyVolumeStatusAfterClickingMute();
	}
	
	/**
	 * Description - Check that the on-screen controls on the Video Player works correctly
	 * 
	 * Steps - 
	 * 	1. Click the Video Player's Screen
	 * 	2. Check that the Video Player's Status is Playing
	 * 	3. Click on the Video player's Screen
	 * 	4. Check that the Video Player's status is Paused
	 * 	5. Check that the toggle full-screen mode button is present and enabled
	 * 
	 */
	@Test(priority = 2, groups = { "VideoPlayerOnScreenControls",
			"JamaNA" }, description = "Check the Video Players on-screen controls.")
	public void checkVideoPlayerOnScreenControlsTest() {
		Log.logInfo("Waiting for the page and video player to load...");
		detailsSteps.waitForVideoBuffering();
		detailsSteps.verifyPlayingStatusAfterClickingScreen();
		detailsSteps.verifyPausedStatusAfterClickingScreen();
		detailsSteps.verifyFullscreenToggleLink();
	}

	/**
	 * Description - Check that the keyboard controls for the Video Player works correctly
	 * 
	 * Steps - 
	 * 	1. Focus on the Video Player's Screen
	 * 	2. Click the Space button
	 * 	3. Check that the Video Player is Playing
	 * 	4. Click the Space Button
	 * 	5. Check that the VIdeo Player is Paused
	 * 
	 */
	@Test(priority = 3, groups = { "VideoPlayerHotkeyControls",
			"JamaNA" }, description = "Check the Video Players keyboard Hotkey controls.")
	public void checkVideoPlayerHotkeyControlsTest() {
		Log.logInfo("Waiting for the page and video player to load...");
		detailsSteps.waitForVideoBuffering();
		detailsSteps.verifyPlayingStatusAfterPressingSpace();
		detailsSteps.verifyPausedStatusAfterPressingSpace();
	}
	
	/**
	 * After every method, Click Cancel button
	 * @param method - Method that was just finished
	 */
	@Override
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		detailsSteps.clickCancelVideo();
		toolBarFull.returnToAllAssesments();
		Log.logTestMethodEnd(method, result);
	}
}
