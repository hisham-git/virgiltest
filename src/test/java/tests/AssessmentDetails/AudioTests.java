package tests.AssessmentDetails;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Test Case - SFB-TC-796 - Assessment Details - Play Audio
 * link - https://k8d.jamacloud.com/perspective.req#/testCases/4212773?projectId=29316
 * 
 * @author Abdullah Al Hisham
 */
@Test(groups = { "Audio" })
public class AudioTests extends AbstractAssessmentDetails {
	
	/*
	 * Identity of the Assessment to be used in this test
	 */
	private final String assessmentName = "CDR", visitName = "Visit2";
	
	
	/**
	 * Before Test class 
	 * 1. Go to assessment list
	 * 2. click an assessment with given name & svid 
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName,
				siteName);
		detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
	}
	
	/**
	 * Checks the Audio Player functionalities for play, pause, loading and finished....
	 * 
	 * Steps - 
	 * 	1.  Check that the Audio Player Exists
	 * 	2.  Check that the Audio Player's initial state is PAUSED
	 * 	3.  Click the Play Audio Button
	 * 	4.  Check that the Audio Player's Status is LOADING
	 * 	5.  Check that the Audio Player is downloading the audio
	 * 	6.  Click the Play Audio Button
	 * 	7.  Check that the Audio Player status is PAUSED
	 * 	8.  Click the Play Audio Button
	 * 	9.  Wait for the Audio Player to be ready
	 * 	10. Check that the status of the Audio Player is PLAYING
	 * 	11. Wait for the Audio Player to end
	 * 	12. Check that the Audio Player's Status is FINISHED
	 * 
	 */
	@Test(enabled = true, groups = { "AudioPlayer",
			"SFB-TC-796" }, description = "Checks the Audio Player functionalities for play, pause, loading and finished controls")
	public void audioPlayerTest() {
		detailsSteps.verifyPresenceOfAudioPlayer();
		detailsSteps.verifyPlayerIsPausedInitially();
		detailsSteps.clickPlayPauseAudio();
		detailsSteps.verifyLoadingStatus();
		detailsSteps.verifyAudioDownload();
		detailsSteps.clickPlayPauseAudio();
		detailsSteps.verifyPlayerIsPaused();
		detailsSteps.clickPlayPauseAudio();
		detailsSteps.verifyPlayerIsPlaying();
		detailsSteps.waitForEnd();
		detailsSteps.verifyPlayerIsFinished();
	}
}
