package steps.Tests;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import mt.siteportal.controls.Dropdown;
import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.ScoresTab;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.objects.UploadFilesPopUp;
import mt.siteportal.objects.UserCredentialsPopUp;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.EsignReasonFields;
import mt.siteportal.utils.helpers.RandomStringGeneratorHelper;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.ListPages.SubjectList;
import steps.Configuration.CommonSteps;

public class PaperTranscriptionSteps extends CommonSteps {

	private ScoresTab scores;
	private SubjectList subjectList;
	private SubjectDetails subjectDetails;
	private AssessmentDetails assessmentDetails;
	private UploadFilesPopUp uploadFilesPopUp;
	
	AssessmentDetailsSteps assessmentDetailsSteps = new AssessmentDetailsSteps();
	ObserverSteps obsSteps = new ObserverSteps();
	SubjectSteps sbjSteps = new SubjectSteps();
	private StudyDashboardSteps dashboardSteps = new StudyDashboardSteps();
	private StudyDashboardListSteps listSteps = new StudyDashboardListSteps();
	private EsignSteps esignSteps = new EsignSteps();
	
	private String subjectName = "TST-SBJ_091516a";
    private String visitName = "Visit 1";
    private String obsRelation = "Wife";
    private String obsAlias = "ObsWife";
//	private final String fileName = "MMSE_en-us_v2.0.pdf";
	private final String fileName = "ECogPro_en-us_v1.0.pdf";
	private final String filePath = System.getProperty("user.dir") + "\\src\\test\\java\\tests\\prerequisites\\";
	
	public void addPreRequisitePendingPaperTranscriptions(String formType) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.clickVisitRow(visitName);
		assessmentDetails = subjectDetails.clickEnterPaperTranscriptionLinkFor(formType);
		if (formType.equalsIgnoreCase("ClinRO"))
			assessmentDetails.chooseRandomRater();
		assessmentDetails.setStartedDate("11", "9", "2016");
		assessmentDetails.setStartedTime("10", "00", "AM");
		assessmentDetails.setDuration("01", "30");
		assessmentDetailsSteps.uploadSourceDocument(filePath, fileName);
		assessmentDetailsSteps.clickSaveButton();
	}
	
	public int storeAssessmentVersion() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		return assessmentDetails.getVersionsFromVersionList().size();
	}

	public void verifyEditModeDisabled() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		WebElement editButton = assessmentDetails.getEditButton();
		WebElement saveButton = assessmentDetails.getSaveButton();
		WebElement cancelButton = assessmentDetails.getCancelButton();
		Log.logInfo("Test if the Edit Form button is present and enabled...");
		Assert.assertTrue(UiHelper.isPresent(editButton), "TThe Edit Button is not present.");
		Assert.assertTrue(UiHelper.isEnabled(editButton), "TThe Edit Button is not present.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Save button is not present...");
		Assert.assertFalse(UiHelper.isPresent(saveButton), "TThe Save Button is present.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Cancel button is not present...");
		Assert.assertFalse(UiHelper.isPresent(cancelButton), "TThe Cancel Button is present.");
		Log.logInfo("[Test Passed]");
	}

	public void clickEditButton() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		WebElement editButton = assessmentDetails.getEditButton();
		UiHelper.click(editButton);
	}

	public void verifyEditModeEnabled() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		WebElement editButton = assessmentDetails.getEditButton();
		WebElement saveButton = assessmentDetails.getSaveButton();
		WebElement cancelButton = assessmentDetails.getCancelButton();
		Log.logStep("Test if the Edit Form button has been hidden.");
		Assert.assertFalse(UiHelper.isPresent(editButton), "The Edit button was visible.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Save and Cancel buttons are visible.");
		Assert.assertTrue(UiHelper.isPresent(saveButton), "The Save button was not found.");
		Assert.assertTrue(UiHelper.isPresent(cancelButton), "The Cancel button was not found.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Save button is disabled and the Cancel button is enabled.");
		Assert.assertFalse(UiHelper.isEnabled(saveButton), "The Save button was not disabled.");
		Assert.assertTrue(UiHelper.isEnabled(cancelButton), "The Cancel button was disbled.");
		Log.logInfo("[Test Passed]");
	}

	public void clickCancelButton() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		WebElement cancelButton = assessmentDetails.getCancelButton();
		if (UiHelper.isClickable(cancelButton))
			UiHelper.click(cancelButton);
	}
	
	/**
	 * @author HISHAM
	 */
	public void clickRefreshButton() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		assessmentDetails.clickRefresh();
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
	}

	public void verifyDropdownControls(String type) {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		String formType = formType(type);
		WebElement row = assessmentDetails.getDetailsGridRowWebElementFor(formType);
		if (row != null) {
			Dropdown dropdown = new Dropdown(row);
			Log.logStep("Test if the " + formType + " dropdown is functional");
			Assert.assertTrue(dropdown.isEnabled(), "The dropdown was not enabled for the " + formType + " field ");
		}
	}

	/**
	 * Checks the Assessment selection criteria and based on that, selects the
	 * type
	 * 
	 * @return String - the type of form specified in the selection map, null if
	 *         it could not be matched with any known form types
	 */
	private String formType(String formType) {
		switch (formType) {
		case "ObsRO":
			return "Observer";
		case "ClinRO":
			return "Rater";
		default:
			return null;
		}
	}

	public void verifyUploadSourceDocumentButtonEnabled() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		Log.logInfo("Test if the Upload Source Document is present and enabled...");
		Assert.assertTrue(UiHelper.isEnabled(assessmentDetails.getUploadSourceDocumentButton()),
				"The Upload Source Document Button was not enabled.");
		Log.logInfo("[Test Passed]");
	}

	public void verifyUploadPopUp() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		Log.logInfo("Test if the Upload Pop-up appears on clicking Upload Source Document button");
		UploadFilesPopUp uploadPopUp = assessmentDetails.clickUploadSourceDocumentButton();
		Assert.assertTrue(uploadPopUp.isOpen(), "The Upload Files pop-up did not open.");
		Log.logInfo("[Test Passed]");
		uploadPopUp.clickClose();
	}

	public void enableEditMode() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		if (UiHelper.isPresent(assessmentDetails.getEditButton()))
			assessmentDetails.clickEditButton();

	}

	private String date, month, year, hours, minutes, amOrPm;
	private String initialStartedDate, expectedDate;

	private void generateRandomDateAndTime() {
		date = RandomStringGeneratorHelper.getRandomDate();
		month = RandomStringGeneratorHelper.getRandomMonth();
		year = RandomStringGeneratorHelper.getRandomYear();
		hours = RandomStringGeneratorHelper.getRandomHour();
		minutes = RandomStringGeneratorHelper.getRandomMinute();
		amOrPm = RandomStringGeneratorHelper.getRandomAmOrPm();
	}

	public void changeTimeAndDateRandomly() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		generateRandomDateAndTime();
		initialStartedDate = assessmentDetails.getAssessmentDetailsItemValue("Started");
		expectedDate = date + "-" + month + "-" + year + " " + hours + ":" + minutes + " " + amOrPm;
		Log.logStep("Changing the Started Date and Time to [" + expectedDate + "]");
		assessmentDetails.setStartedDate(date, month, year);
		assessmentDetails.setStartedTime(hours, minutes, amOrPm);
	}

	public void verifyNoDateTimeChange() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		Log.logInfo("Test that there was no change in the Started Date and Time...");
		Assert.assertEquals(assessmentDetails.getAssessmentDetailsItemValue("Started"), initialStartedDate,
				"There was a change in the Started Date and Time. Date and time ");
		Log.logInfo("[Test Passed]");
	}

	public void clickSaveButton() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		WebElement saveButton = assessmentDetails.getSaveButton();
		(new WebDriverWait(Browser.getDriver(), 2)).until(ExpectedConditions.elementToBeClickable(saveButton));
		UiHelper.click(saveButton);
	}

	public void verifyCorrectDateTimeChange() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		Log.logInfo("Test that the Started Date and Time were changed correctly...");
		Assert.assertEquals(assessmentDetails.getAssessmentDetailsItemValue("Started"), expectedDate,
				"There was a change in the Started Date and Time. Date and time ");
		Log.logInfo("[Test Passed]");
	}

	String durationHours, durationMinutes, initialDuration, expectedDuration;

	private void generateRandomDuration() {
		durationHours = RandomStringGeneratorHelper.getRandomHour();
		durationMinutes = RandomStringGeneratorHelper.getRandomMinute();
	}

	public void changeDurationRandomly() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		generateRandomDuration();
		initialDuration = assessmentDetails.getAssessmentDetailsItemValue("Duration");
		String durationHour = initialDuration.split(":")[0];
		String durationMinute = initialDuration.split(":")[1];
		initialDuration = getExpectedDurationText(durationHour, durationMinute);
		expectedDuration = getExpectedDurationText(durationHours, durationMinutes);
		Log.logStep("Changing the Duration to [" + expectedDuration + "]");
		assessmentDetails.setDuration(durationHours, durationMinutes);
	}

	/**
	 * Formats the expected hour and minute into a String that is consistent
	 * with how it is displayed to the user
	 * 
	 * @param hours
	 *            - String - the hour
	 * @param minutes
	 *            - String - the minutes
	 * @return - String - the formatted text
	 */
	private String getExpectedDurationText(String hours, String minutes) {
		if (hours.equals("00"))
			hours = "";
		else {
			hours = (hours.startsWith("0")) ? hours.replaceFirst("0", "") : hours;
			hours = hours + " " + (hours.equals("1") ? "hour" : "hours");
		}
		if (minutes.equals("00"))
			minutes = "";
		else {
			minutes = (minutes.startsWith("0")) ? minutes.replaceFirst("0", "") : minutes;
			minutes = minutes + " " + (minutes.equals("1") ? "minute" : "minutes");
		}
		return (hours + " " + minutes).trim();
	}

	public void verifyNoDurationChange() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		Log.logInfo("Test that there was no change in the Duration Field...");
		Assert.assertEquals(assessmentDetails.getAssessmentDetailsItemValue("Duration"), initialDuration,
				"There was a change in the Duration. Duration ");
		Log.logInfo("[Test Passed]");
	}

	public void verifyCorrectDurationChange() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		Log.logInfo("Test that the Duration was changed correctly...");
		Assert.assertEquals(assessmentDetails.getAssessmentDetailsItemValue("Duration"), expectedDuration,
				"There was a change in the Duration. Duration ");
		Log.logInfo("[Test Passed]");
	}

	public void verifyVersionNumberEquals(int numberOfVersions) {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		assessmentDetails.clickRefresh();
		Log.logInfo("Test that the number of versions equals [" + numberOfVersions + "]...");
		Assert.assertEquals(numberOfVersions, assessmentDetails.getVersionsFromVersionList().size(),
				"The form Version was updated.");
		Log.logInfo("[Test Passed]");
	}

	public void openScoresTab() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		assessmentDetails.clickScoresTabLink();
	}

	public void verifyScoresTabEditModeDisabled() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		WebElement editButton = scores.getEditButton();
		WebElement saveButton = scores.getSaveButton();
		WebElement cancelButton = scores.getCancelButton();
		Log.logInfo("Test if the Scores Edit Form button is present and enabled...");
		Assert.assertTrue(UiHelper.isPresent(editButton), "The Scores Edit Button is not present.");
		Assert.assertTrue(UiHelper.isEnabled(editButton), "The Scores Edit Button is not present.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Scores Save button is not present...");
		Assert.assertFalse(UiHelper.isPresent(saveButton), "The Scores Save Button is present.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Scores Cancel button is not present...");
		Assert.assertFalse(UiHelper.isPresent(cancelButton), "The Scores Cancel Button is present.");
		Log.logInfo("[Test Passed]");
	}

	public void verifyScoresTabEditModeEnabled() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		WebElement editButton = scores.getEditButton();
		WebElement saveButton = scores.getSaveButton();
		WebElement cancelButton = scores.getCancelButton();
		Log.logStep("Test if the Scores Edit Form button has been hidden.");
		Assert.assertFalse(UiHelper.isPresent(editButton), "The Scores Edit button was visible.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Scores Save and Cancel buttons are visible.");
		Assert.assertTrue(UiHelper.isPresent(saveButton), "The Scores Save button was not found.");
		Assert.assertTrue(UiHelper.isPresent(cancelButton), "The Scores Cancel button was not found.");
		Log.logInfo("[Test Passed]");
		Log.logInfo("Test if the Scores Save button is disabled and the Cancel button is enabled.");
		Assert.assertFalse(UiHelper.isEnabled(saveButton), "The Scores Save button was not disabled.");
		Assert.assertTrue(UiHelper.isEnabled(cancelButton), "The Scores Cancel button was disbled.");
		Log.logInfo("[Test Passed]");
	}

	public void clickScoresTabEditButton() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		UiHelper.click(scores.getEditButton());
	}

	public void clickScoresTabCancelButton() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		UiHelper.click(scores.getCancelButton());
	}

	public void makeRandomScoreChanges() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		Log.logStep("Making a random change to a random score.");
		scores.makeRandomChangeTo(scores.getRandomScore());
	}

	public Map<String, String> getScoresAndAnswersFromTab() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		return scores.getScoresAndAnswersFromTab();
	}

	public void verifyScoreChange(Map<String, String> scores2) {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		Map<String, String> newScores = scores.getScoresAndAnswersFromTab();
		Log.logInfo("Test that scores are in their expected values...");
		Assert.assertEquals(newScores, scores2, "The Scores did not match.");
		Log.logInfo("[Test Passed]");
	}

	public void verifyEnabledSaveScoresButton() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		WebElement saveButton = scores.getSaveButton();
		Log.logInfo("Test that the Save Scores Button is enabled...");
		Assert.assertTrue(UiHelper.isEnabled(saveButton), "The Save button was not clickable.");
		Log.logInfo("[Test Passed]");
	}

	public int getVersionNumber() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
//		return TextHelper.getVersionNumber(scores.getVersion()) + 1;
		return TextHelper.getVersionNumber(scores.getVersion());
	}

	private String scoreText, changedScoreValue;

	public void makeRandomScoreChange() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		WebElement scoreItem = scores.getRandomScore();
		scoreText = scoreItem.findElement(By.xpath(".//div[1]")).getText();
		Log.logStep("Making a random change to the score - " + scoreText);
		changedScoreValue = scores.makeRandomChangeTo(scoreItem);
		Log.logStep("Changed the value to [" + changedScoreValue + "]");
	}

	public void clickScoresTabSaveButton() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		UiHelper.scrollToElementWithJavascript(scores.getSaveButton(), Browser.getDriver());
		UiHelper.click(scores.getSaveButton());
	}

	public void verifyCredentialsPopUp() {
		UserCredentialsPopUp popup = new UserCredentialsPopUp(
				Browser.getDriver().findElement(By.id("paperTranscription")), Browser.getDriver());
		Log.logInfo("Test if the E-Sign Credentials Dialog are opened...");
		Assert.assertTrue(popup.isOpen(), "The E-sign dialog did not open.");
		Log.logInfo("[Test Passed]");
	}

	public void enterCredentialsForPopUp(String username, String password) {
		UserCredentialsPopUp popup = new UserCredentialsPopUp(
				Browser.getDriver().findElement(By.id("paperTranscription")), Browser.getDriver());
		popup.enterCredentials(username, password);
		Log.logStep("Clicking OK.");
		popup.ok();
	}

	public void verifyScoreChange() {
		scores = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
		String scoreValue = scores.getScoresAndAnswersFromTab().get(scoreText);
		Log.logInfo("Test if the Scores change after clicking Save...");
		Assert.assertEquals(scoreValue, changedScoreValue, "The Score did not match.");
		Log.logInfo("[Test Passed]");
	}

	public void selectVisitForSubject(String visitName) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		String visitStatus = subjectDetails.getStatusOfVisit(visitName);
		if (visitStatus.equalsIgnoreCase("Skipped") || visitStatus.equalsIgnoreCase(""))
			subjectDetails.clickAddButtonForVisit(visitName);
		subjectDetails.clickVisitRow(visitName);
	}

	public boolean getToSubjectDetails(String subjectName) {
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
//		openAllSubjects();
		subjectList.clickRowForSubject(subjectName);
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		return subjectDetails.isOpened();
	}

	public void verifyEnterPaperTranscriptionLink(String formType) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		assessmentDetails = subjectDetails.clickEnterPaperTranscriptionLinkFor(formType);
		Log.logInfo("Test if the Assessment Details page is opened after clicking the Enter Paper Transcription Link...");
		Assert.assertTrue(assessmentDetails.isOpened(),
				"The Assessment Details Page was not opened on clicking the Enter Paper Transcription Link.");
		Log.logInfo("[Test Passed]");
	}
	
	public void openPaperTranscriptionDetailsPage(Map<String, String> criteria) {
		dashboardSteps.getToStudyDashboard();
		dashboardSteps.goToList("Assessments-Paper Transcription");
		if (!listSteps.clickRow(criteria)) {
			Browser.getDriver().close();
			throw new SkipException(
					"No Assessment with the criteria " + criteria + " could be found. Skipping this test.");
		}
	}
	
	public void saveChangesWithEsign(String userName, String pass){
        if (esignSteps.dialogIsOpenedVerification()) {
			esignSteps.esignDialogPredefinedReason(EsignReasonFields.TECHNICAL.getValue());
			esignSteps.esignDialogConfirm(userName, pass);
		}
    }
}
