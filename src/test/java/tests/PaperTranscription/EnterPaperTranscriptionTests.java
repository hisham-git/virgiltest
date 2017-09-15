package tests.PaperTranscription;

import java.util.Map;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;

/**
 * Test if the Pending Paper Transcription forms Edit Functionality
 * works correctly for Form Details Checks the Rater/Observer, Started Time and
 * Date, Duration and Upload Document Fields
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
public class EnterPaperTranscriptionTests extends AbstractPaperTranscription{
	
	@Factory(dataProvider = "subjectForms")
	public EnterPaperTranscriptionTests(String formType) {
		super.formType = formType;
		setAssessmentSelectionCriteria();
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Log.logStep("Starting test with form type [" + formType + "]");
		pptSteps.openPaperTranscriptionDetailsPage(criteria);
		numberOfVersions = pptSteps.storeAssessmentVersion();
		toolBarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
	}
	
	@Test(groups = { "EditControlsOnPaperTranscriptionForm",
			"JamaNA" }, description = "Check the functionality of editable control elements on details page & scores tab of a Paper Transcription form")
	public void editControlsOnPaperTranscriptionFormTest() {
		pptSteps.verifyEditModeDisabled();
		pptSteps.clickEditButton();
		pptSteps.verifyEditModeEnabled();
		pptSteps.clickCancelButton();
		pptSteps.verifyEditModeDisabled();

		pptSteps.enableEditMode();
		pptSteps.verifyDropdownControls(formType);
		pptSteps.clickCancelButton();

		pptSteps.enableEditMode();
		pptSteps.changeTimeAndDateRandomly();
		pptSteps.clickCancelButton();
		pptSteps.verifyNoDateTimeChange();

		pptSteps.enableEditMode();
		pptSteps.changeTimeAndDateRandomly();
		pptSteps.clickSaveButton();
		pptSteps.saveChangesWithEsign(username, password);
		pptSteps.verifyCorrectDateTimeChange();
		pptSteps.clickCancelButton();

		pptSteps.enableEditMode();
		pptSteps.changeDurationRandomly();
		pptSteps.clickCancelButton();
		pptSteps.verifyNoDurationChange();
		pptSteps.enableEditMode();
		pptSteps.changeDurationRandomly();
		pptSteps.clickSaveButton();
		pptSteps.saveChangesWithEsign(username, password);
		pptSteps.verifyCorrectDurationChange();
		pptSteps.verifyVersionNumberEquals(numberOfVersions);
		pptSteps.clickCancelButton();

		pptSteps.enableEditMode();
		pptSteps.verifyUploadSourceDocumentButtonEnabled();
		pptSteps.verifyUploadPopUp();
		pptSteps.verifyUploadSourceDocumentButtonEnabled();
		pptSteps.clickCancelButton();
		pptSteps.clickCancelButton();
		
		pptSteps.clickRefreshButton();
		pptSteps.openScoresTab();
		pptSteps.verifyScoresTabEditModeDisabled();
		pptSteps.clickScoresTabEditButton();
		pptSteps.verifyScoresTabEditModeEnabled();
		pptSteps.clickScoresTabCancelButton();
		pptSteps.verifyScoresTabEditModeDisabled();
		
		Map<String, String> scores = pptSteps.getScoresAndAnswersFromTab();
		pptSteps.clickScoresTabEditButton();
		pptSteps.makeRandomScoreChanges();
		pptSteps.verifyEnabledSaveScoresButton();
		pptSteps.clickScoresTabCancelButton();
		pptSteps.verifyScoreChange(scores);
		
		int versionNumberShouldBe = pptSteps.getVersionNumber() + 1;
		pptSteps.clickScoresTabEditButton();
		pptSteps.makeRandomScoreChange();
		pptSteps.clickScoresTabSaveButton();
		pptSteps.verifyCredentialsPopUp();
		pptSteps.enterCredentialsForPopUp(username, password);
		pptSteps.verifyScoreChange();
		pptSteps.verifyVersionNumberEquals(versionNumberShouldBe);
	}
}
