package steps.Tests;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.AttachmentsTab;
import mt.siteportal.details.RaterDetails;
import mt.siteportal.details.ScoresTab;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.objects.UploadFilesPopUp;
import mt.siteportal.pages.About;
import mt.siteportal.pages.Footer;
import mt.siteportal.pages.Header;
import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.EsignDialog;
import mt.siteportal.pages.studyDashboard.StudyProfile;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.CredentialsHolder;
import mt.siteportal.utils.data.TestParameters;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.AudioPlayerHelper;
import mt.siteportal.utils.helpers.FDEFParser;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.helpers.PDFViewerHelper;
import mt.siteportal.utils.helpers.RandomStringGeneratorHelper;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.helpers.VideoPlayerHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import nz.siteportal.pages.studydashboard.ListPages.SubjectList;
import nz.siteportal.pages.studydashboard.ListPages.VisitList;
import steps.Configuration.CommonSteps;
import tests.AssessmentDetails.FileUploadHelper;
import nz.siteportal.objects.QueryPanelItem;
import nz.siteportal.objects.Score;

/**
 * Created by Abdullah Al Hisham on 07/25/2016
 */

public class AssessmentDetailsSteps extends CommonSteps {

	/*
	 * The Page Objects
	 */
	private AssessmentList assessmentList;
	private DashboardList dashboardList;
	private Dashboard dashboard;
	private LoginPage loginPage;
	private VisitList visitList;
	private SubjectList subjectList;
	private AssessmentDetails assessmentDetails;
	private AttachmentsTab attachmentsTab;
	private AudioPlayerHelper audioHelper;
	private QueriesSidePanel qSidePanel;
	private ScoresTab scoresTab;
	private ToolBarFull toolbarFull;
	private EsignDialog esignStep;
	
	/*public AssessmentDetailsSteps() {
		super();
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		loginPage = PageFactory.initElements(Browser.getDriver(), LoginPage.class);
		visitList = PageFactory.initElements(Browser.getDriver(), VisitList.class);
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		audioHelper = PageFactory.initElements(Browser.getDriver(), AudioPlayerHelper.class);
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		scoresTab = PageFactory.initElements(Browser.getDriver(), ScoresTab.class);
	}*/
	
	/**
	 * Credential variables
	 */
	protected String siteportalUserAccountName;
	protected String siteportalUserAccountPassword;

	/*
	 * Study and Sites to be used for the child Test Classes
	 */
	protected String studyName = "AA Pharmaceutical - AA Study 2";
	protected String siteName = "All Sites";

	/*
	 * User Credentials
	 */
	/*protected String siteportalUserAccountName = CredentialsHolder.get()
			.usernameForUserType("Site Rater - Type 2");
	protected String siteportalUserAccountPassword = CredentialsHolder.get()
			.passwordForUserType("Site Rater - Type 2");*/
	
	/**
	 * Set credentials for specific test
	 * 
	 * @param userName
	 * @param pass
	 */
	public void setTestCredentials (String userName, String pass) {
		this.siteportalUserAccountName = userName;
		this.siteportalUserAccountPassword = pass;
	}

	/*
	 * The Default Study Dashboard Card to click to get to the Assessments List
	 */
	protected final String filter = "Assessments";

	/*
	 * Helper Functions
	 */
	/**
	 * Logs a user in and navigates from the Home Page to the Study Dashboard
	 * 
	 * @return Dashboard
	 */
	public Dashboard login() {
		Browser.getDriver().manage().deleteAllCookies();
		Nav.toURL(URLsHolder.getHolder().getSiteportalURL());
		loginPage = PageFactory.initElements(Browser.getDriver(), LoginPage.class);
		loginPage.loginAsSitePerson(siteportalUserAccountName, siteportalUserAccountPassword);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		return dashboard;
	}

	/**
	 * Checks if the Dashboard is opened. If not, then navigate to study
	 * dashboard url. If still not showing study dashboard, relog. Throws
	 * SkipException if the dashboard is still not shown
	 * 
	 * @return Dashboard
	 */
	public void getToDashboard() {
		if (Browser.getDriver().getCurrentUrl().startsWith(URLsHolder.getHolder().getMaportalURL())) {
			dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
			if (!dashboard.isDashboardOpened()) {
				if (homePage.isHomePageOpened()) {
					homePage.openDashboard();
				} else {
					openHomePage();
					homePage.openDashboard();
				}
			}
		} else if (Browser.getDriver().getCurrentUrl().startsWith(URLsHolder.getHolder().getSiteportalURL())
				|| Browser.getDriver().getCurrentUrl().startsWith(URLsHolder.getHolder().getSponsorportalURL())) {
			if (dashboard == null || !dashboard.isDashboardOpened()) {
				Nav.toURL(URLsHolder.getHolder().getSiteportalURL() + "/"
						+ URLsHolder.getHolder().getStudyDashboardRouteURL());
				dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
			} /*
				 * else { Log.logStep("A direct navigation to the study url [" +
				 * URLsHolder.getHolder().getSiteportalURL() +
				 * "] did not lead to the dashboard being opened. This indicates either the user is not logged in, or does not have the permission to access the Study Dashboard. So relogging.."
				 * ); dashboard = login(); }
				 */
		} else {
			Log.logError("Could not get to Dashboard. Incorrect User configuration",
					new SkipException("Could not Navigate to the Study Dashboard"));
		}

		/*Log.logStep("Navigated to Study Dashboard with [Study: " + studyName + "] and [Site: " + siteName + "]");
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		if (null == dashboard.getSelectedSite() || "" == dashboard.getSelectedSite()) {
			dashboard.selectStudy(studyName);
			dashboard.selectSite(siteName);
		}*/
	}

	/**
	 * Navigates to a particular Assessment List Page by specifying the filter
	 * name
	 * 
	 * @return AssessmentList
	 */
	public void getToAssessmentList(String filter) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		if (!dashboard.isDashboardOpened())
			getToDashboard();
		dashboard.clicksOnCard("Assessments-" + filter);
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		if (!assessmentList.isOpened())
			throw new SkipException("The Assessment List page could not be opened. Skipping tests..");
	}

	/**
	 * Navigates to a particular Assessment List Page by using the default
	 * filter name
	 * 
	 * @return AssessmentList
	 */
	public void getToAssessmentList() {
		getToAssessmentList(filter);
	}

	public void getToAssessmentDetails(String assessmentName, String svid) {
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		Log.logStep("Clicking Assessment row for " + assessmentName + " & " + svid);
		assessmentDetails = assessmentList.clickRow(assessmentName, svid);
		if (null != assessmentDetails)
			Log.logInfo("Details page is open for Assessment: " + assessmentName);
	}

	public void navigateBackToAssessmentList() {
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		if (!assessmentList.isOpened()) {
			Log.logInfo("Navigating back to Assessment List...");
			Browser.getDriver().navigate().back();
		} else {
			Log.logInfo("Already in 'Assessment List' page...");
		}
	}

	public void navigateBackToAssessmentDetails() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		if (!assessmentDetails.isOpened()) {
			Log.logInfo("Navigating back to Assessment Details...");
			Browser.getDriver().navigate().back();
		} else {
			Log.logInfo("Already in 'Assessment Details' page...");
		}
	}

	public void clickAttachmentsTabLink() {
		Log.logStep("Clicking the Attachments Tab Link...");
		attachmentsTab = assessmentDetails.clickAttachmentsTabLink();
	}

	private int numberOfAttachments;

	public void getAttachmentsCount() {
		numberOfAttachments = attachmentsTab.getNumberOfFilesFromAttachmentTab();
		Log.logStep("The current number of attachments available in the Attachments tab "
				+ ((numberOfAttachments > 1) ? "are " : "is ") + numberOfAttachments);
	}

	public void uploadAttachment(String filePath) {
		UploadFilesPopUp popup = attachmentsTab.clickUploadAttachmentsButton();
		popup.clickAddFileButton();
		FileUploadHelper.inputFilePath(filePath);
		popup.clickUploadButton();
		popup.waitForUploadToFinish();
	}
	
	public void uploadSourceDocument(String filePath, String fileName) {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		UploadFilesPopUp popup = assessmentDetails.clickUploadSourceDocumentButton();
		if (popup.isOpen()) {
			popup.clickAddFileButton();
			FileUploadHelper.inputFilePath(filePath + fileName);
			if (popup.isUploadButtonEnabled()) {
				popup.clickUploadButton();
				popup.waitForUploadToFinish();
				if (UiHelper.isClickable(assessmentDetails.getSourceDocumentElement()))
					HardVerify.NotNull(assessmentDetails.getSourceDocumentFileName(), "File Uploaded");
			}
		}
	}

	public void verifyUploadedAttachment() {
		HardVerify.Equals(numberOfAttachments + 1, attachmentsTab.getNumberOfFilesFromAttachmentTab(),
				"Test if the number of attachments increase by 1 after uploading...", "[TEST PASSED]",
				"The number of attachments did not increase by one. Expected [" + (numberOfAttachments + 1)
						+ "] but was [" + attachmentsTab.getNumberOfFilesFromAttachmentTab() + "]");
	}

	private int number_of_windows_before_click;

	public void clickAttachmentAtIndex(int index) {
		number_of_windows_before_click = Browser.getDriver().getWindowHandles().size();
		attachmentsTab.clickAttachmentLinkAtIndex(index);
	}

	public void verifyAttachmentIsOpened() {
		HardVerify.Equals(number_of_windows_before_click + 1, Browser.getDriver().getWindowHandles().size(),
				"Test if a new window opens for displaying the attached file.", "[TEST PASSED]",
				"The number of browser windows did not increase by one. Number should have been ["
						+ (number_of_windows_before_click + 1) + "] but was ["
						+ Browser.getDriver().getWindowHandles().size() + "]");
	}

	public void deleteAttachment(String assessmentName, int index) {
		if (numberOfAttachments == 0)
			throw new SkipException("There were no attachments found for the Assessment [" + assessmentName + "]");
		Log.logStep("Clicking the delete button for the Attachment at the [" + index + "] index...");
		attachmentsTab.clickDeleteAttachmentAtIndex(index);
	}

	public void verifyAttachmentDeleted() {
		HardVerify.Equals(numberOfAttachments - 1, attachmentsTab.getNumberOfFilesFromAttachmentTab(),
				"Test if the number of queries after clicking the delete button reduces by 1...", "[TEST PASSED]",
				"The expected number of queries was [" + (numberOfAttachments - 1) + "] but was ["
						+ attachmentsTab.getNumberOfFilesFromAttachmentTab() + "]");
	}

	public void verifyPresenceOfAudioPlayer() {
		HardVerify.True(assessmentDetails.audioPlayerExists(), "Test if the Audio Player Exists...", "[TEST PASSED]",
				"The Audio Player does not exist or is not visible.");
	}

	public void verifyPlayerIsPausedInitially() {
		audioHelper = new AudioPlayerHelper(Browser.getDriver());
		HardVerify.True(audioHelper.hasStatus("is-paused"), "Test if the Audio Player's Initial State is Paused...",
				"[TEST PASSED]", "The Audio Player's Initial state should have been [is-paused] but had the classes ["
						+ audioHelper.getStatuses() + "]");
	}

	public void clickPlayPauseAudio() {
		Log.logStep("Clicking the Play Audio Button...");
		audioHelper.clickPlayAudioButton();
	}

	public void verifyLoadingStatus() {
		HardVerify.True(audioHelper.hasStatus("is-loading"),
				"Test if the Audio Player's State after clicking Play is [LOADING]...", "[TEST PASSED]",
				"The Audio Player's State should have been [LOADING] but had the classes [" + audioHelper.getStatuses()
						+ "]");
	}

	public void verifyAudioDownload() {
		Log.logStep("Waiting for the Player to be ready...");
		audioHelper.waitForPlayerToBeReady();
		Log.logStep("Player is ready to play audio.");
	}

	public void verifyPlayerIsPaused() {
		HardVerify.True(audioHelper.hasStatus("is-paused"),
				"Test if the Audio Player's State after clicking Pause is [PAUSED]...", "[TEST PASSED]",
				"The Audio Player's State should have been [PAUSED] but had the classes [" + audioHelper.getStatuses()
						+ "]");
	}

	public void verifyPlayerIsPlaying() {
		HardVerify.True(audioHelper.waitForStatus("is-playing", 3000),
				"Test if the Audio Player's State after clicking Play is [PLAYING]...", "[TEST PASSED]",
				"The Audio Player's State should have been [PLAYING] but had the classes [" + audioHelper.getStatuses()
						+ "]");
	}

	public void waitForEnd() {
		Log.logStep("Waiting for the player to end...");
		audioHelper.waitForPlayerToEnd();
		Log.logStep("Player has finished playing.");
	}

	public void verifyPlayerIsFinished() {
		HardVerify.True(audioHelper.hasStatus("is-finished"),
				"Test if the Audio Player's State after playing is [FINISHED]...", "[TEST PASSED]",
				"The Audio Player's State should have been [FINISHED] but had the classes ["
						+ audioHelper.getStatuses() + "]");
	}

	public boolean checkRowNumbers() {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		int numberOfRows = dashboardList.getListCount();
		Log.logInfo("Number of rows found: " + numberOfRows);
		if (numberOfRows < 1) {
			throw new SkipException(
					"There were less than two Assessments for this filter. The test case requires a minimum of two Assessments to be present for testing."
							+ " The test parameters need to be reviewed before executing this particular filter's tests.");
		}
		return true;
	}

	public String getSiteNameForFirstRow() {
		siteName = dashboardList.getColumnData("Site", dashboardList.getFirstItemFromList());
		return siteName;
	}

	public void clickFirstItemFromAssessmetList() {
		Log.logStep("Clicking on the first item in the table and navigating to its Assessment Details page...");
		dashboardList.clickFirstRow();
	}

	public void verifyStudyName(String study) {
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		HardVerify.Equals(study, toolbarFull.getStudyNameFromBreadcrumbs(),
				"Testing for the correct Study Name in the breadcrumbs...", "[TEST PASSED]",
				"The Study name was not correct, expected [" + study + "] entries but found ["
						+ toolbarFull.getStudyNameFromBreadcrumbs() + "]");
	}

	public void verifySiteName() {
		HardVerify.Equals(siteName, toolbarFull.getSiteNumberFromBreadcrumbs(),
				"Testing for the correct Site Number in the breadcrumbs...", "[TEST PASSED]",
				"The Site name was not correct, expected [" + siteName + "] entries but found ["
						+ toolbarFull.getSiteNumberFromBreadcrumbs() + "]");
	}

	public void verifyFilterName(String filter) {
		if (filter.equals("Assessments"))
			filter = "Assessments: All";
		else
			filter = "Assessments: " + filter;
		HardVerify.Equals(filter, toolbarFull.getFilterFromBreadcrumbs(),
				"Testing for the correct Filter Name in the breadcrumbs...", "[TEST PASSED]",
				"The FIlter name was not correct, expected [" + filter + "] entries but found ["
						+ toolbarFull.getFilterFromBreadcrumbs() + "]");
	}

	public void verifyHeader(String assessmentType) {
		HardVerify.True(assessmentDetails.checkHeader(assessmentType), "Test if the Headers are correct...",
				"[TEST PASSED]", "The Header was not correct. Expected header of the format [Assessment: {NAME} ("
						+ assessmentType + ")] but was [" + assessmentDetails.getHeader() + "]");
	}

	public void verifyDetailsGrid(String assessmentType) {
		HardVerify.True(assessmentDetails.checkDetailsGrid(assessmentType),
				"Test if the Details Grid Data are correct...", "[TEST PASSED]",
				"The Details Grid Data was not correct...");
	}

	public void verifyVersionsTable(String assessmentType) {
		HardVerify.True(assessmentDetails.checkVersionsTable(assessmentType),
				"Test if the Version Table Data are correct...", "[TEST PASSED]",
				"The Version Table Data was not correct...");
	}

	public void verifyScoresTab(String assessmentType) {
		HardVerify.True(assessmentDetails.checkScoresTab(assessmentType), "Test if the Scores Tab is correct...",
				"[TEST PASSED]", "The Scores Tab was not correct...");
	}

	public void verifyPresenceOfEditButton(String assessmentName, String svid) {
		if (assessmentDetails == null)
			throw new SkipException("The Assessment with name : [" + assessmentName + "] and SVID : [" + svid
					+ "] could not be found. Skipping this test...");
		HardVerify.True(UiHelper.isPresent(assessmentDetails.getEditButton()), "Test if the Edit button is Present....",
				"[TEST PASSED]", "The Edit button was not found");
	}

	public void clickEditButton() {
		Log.logStep("Clicking the Edit button...");
		assessmentDetails.clickEditButton();
	}

	public void verifyControlsActivated() {
		HardVerify.True(assessmentDetails.isInEditMode(),
				"Test if the Save, Cancel, Started Date and Time picker buttons are Present....", "[TEST PASSED]",
				"One or more of the buttons was not found.");
	}

	private String initialStartedDate = null;
	private String expectedDate = null;

	public void editStartedDateTime() {
		String date = RandomStringGeneratorHelper.getRandomDate(), month = RandomStringGeneratorHelper.getRandomMonth(),
				year = RandomStringGeneratorHelper.getRandomYear(), hours = RandomStringGeneratorHelper.getRandomHour(),
				minutes = RandomStringGeneratorHelper.getRandomMinute(),
				amOrPm = RandomStringGeneratorHelper.getRandomAmOrPm();

		initialStartedDate = assessmentDetails.getAssessmentDetailsItemValue("Started");
		expectedDate = date + "-" + month + "-" + year + " " + hours + ":" + minutes + " " + amOrPm;
		Log.logStep("Changing the Started Date and Time to [" + expectedDate + "]");
		assessmentDetails.setStartedDate(date, month, year);
		assessmentDetails.setStartedTime(hours, minutes, amOrPm);
	}

	public void clickCancelButton() {
		Log.logStep("Clicking the Cancel Button...");
		assessmentDetails.clickCancelButton();
	}

	public void verifyStartedDateTimeUnchanged() {
		HardVerify.Equals(initialStartedDate, assessmentDetails.getAssessmentDetailsItemValue("Started"),
				"Test if the Started Date and Time entries were not updated after clicking Cancel....", "[TEST PASSED]",
				"Expected date and time after clicking Cancel to be [" + initialStartedDate + "] but was ["
						+ assessmentDetails.getAssessmentDetailsItemValue("Started") + "]");
	}

	public void clickSaveButton() {
		Log.logStep("Clicking the Save Button...");
		assessmentDetails.clickSaveButton();
	}

	public void verifyStartedDateTimeChanged() {
		HardVerify.Equals(expectedDate, assessmentDetails.getAssessmentDetailsItemValue("Started"),
				"Test if the Started Date and Time entries were updated after clicking Save....", "[TEST PASSED]",
				"Expected date and time after clicking to be [" + expectedDate + "] but was ["
						+ assessmentDetails.getAssessmentDetailsItemValue("Started") + "]");
	}

	private ArrayList<QueryPanelItem> qPanelItem;
	private String mainWindowSiteStr, mainWindowSubjStr, mainWindowVisitStr, mainWindowAssessmentStr;
	private String querySiteName, querySubjName, queryVisitName, queryAssessName;

	public boolean getAssessmentQuery() {
		boolean categoryFound = false;
		Log.logStep("Searching for an Assessment Query...");
		for (int i = 0; i < qPanelItem.size(); i++) {
			if (qPanelItem.get(i).getQueryCategory().equals("Assessment")) {
				// flags the categoryFound as True
				Log.logInfo("An Assessment Query is found.");
				categoryFound = true;
				querySubjName = qPanelItem.get(i).getQuerySubjName();
				querySiteName = qPanelItem.get(i).getQuerySite();
				queryVisitName = qPanelItem.get(i).getQueryVisit();
				queryAssessName = qPanelItem.get(i).getQueryAssessmentName();
				// Clicks on the current Query
				qSidePanel = qPanelItem.get(i).clicksOnQuery();
				assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
				// Get the new list of queries
				qPanelItem = qSidePanel.getQueriesFromList();
				break;
			}
			// break;
		}
		return categoryFound;
	}

	public void verifyQuerySiteName() {
		mainWindowSiteStr = assessmentDetails.getAssessmentDetailsItemValue("Site");
		HardVerify.True(mainWindowSiteStr.contains(querySiteName), "Verifying the specific Assessment's site number...",
				"Site number matched. Main-window[" + mainWindowSiteStr + "] and Query Panel[" + querySiteName + "].",
				"Site number didn't match between loaded subject and slected Query");
	}

	public void verifyQuerySubjectName() {
		mainWindowSubjStr = assessmentDetails.getAssessmentDetailsItemValue("Subject");
		HardVerify.True(mainWindowSubjStr.contains(querySubjName),
				"Verifying the specific Assessment's Subject name...",
				"Subject name matched. Main-window[" + mainWindowSubjStr + "] and Query Panel[" + querySubjName + "].",
				"Subject name didn't match.Main-window[" + mainWindowSubjStr + "],Query Panel[" + querySubjName + "]");
	}

	public void verifyQueryVisitName() {
		mainWindowVisitStr = assessmentDetails.getAssessmentDetailsItemValue("Visit");
		HardVerify.True(mainWindowVisitStr.contains(queryVisitName),
				"Verifying the specific Assessment's visit name...",
				"Visit name matched. Main-window[" + mainWindowVisitStr + "] and Query Panel[" + queryVisitName + "].",
				"Visit name didn't match.Main-window[" + mainWindowVisitStr + "],Query Panel[" + queryVisitName + "]");
	}

	public void verifyQueryAssessmentName() {
		mainWindowAssessmentStr = assessmentDetails.getAssessmentDetailsItemValue("Assessment");
		HardVerify.True(mainWindowAssessmentStr.contains(queryAssessName),
				"Verifying the specific Assessment's name...",
				"Assessment name matched. Main-window[" + mainWindowAssessmentStr + "] and Query Panel["
						+ queryAssessName + "].",
				"Assessment name didn't match..Main-window[" + mainWindowAssessmentStr + "],Query Panel["
						+ queryAssessName + "]");

	}

	public void openQuerySidePanel() {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		qSidePanel = dashboard.openQueriesPanel();
		qPanelItem = qSidePanel.getQueriesFromList();
	}

	public void closeQuerySidePanel() {
		if (qSidePanel.isOpened())
			qSidePanel.clickClose();
	}

	private WebElement row;
	private String formName, typeFromList, versionFromList, statusFromList, raterFromList, visitFromList, siteFromList,
			subjectFromList;

	public void getRandomAssessmentDataFromList() {
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		row = assessmentList.getRandomRow();
		formName = assessmentList.getColumnData("Assessment", row);
		typeFromList = assessmentList.getColumnData("Type", row);
		versionFromList = assessmentList.getColumnData("Version", row);
		statusFromList = assessmentList.getColumnData("Status", row);
		raterFromList = assessmentList.getColumnData("Rater", row);
		visitFromList = assessmentList.getColumnData("Visit", row);
		siteFromList = assessmentList.getColumnData("Site", row);
		subjectFromList = assessmentList.getColumnData("Subject", row);
	}

	public void clickRandomAssessment() {
		assessmentDetails = assessmentList.clickAnAssessment(row);
	}

	public void verfyRandomAssessmentDetails() {
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		studyName = toolbarFull.getStudyNameFromBreadcrumbs();
		if (null != assessmentDetails) {
			HardVerify.True(assessmentDetails.isOpened(), "Verifying if the Assessment details page is open...",
					"Assessment details page is open.", "Assessment Details page is not open.");
			HardVerify.True(
					assessmentDetails.verifyGridData(typeFromList, studyName, siteFromList, subjectFromList, formName,
							visitFromList, raterFromList, versionFromList, statusFromList),
					"Assessment data didn't match between Details Grid and Assessment List.");
		} else {
			Log.logInfo("Assessment details not found");
		}
	}

	/**
	 * The Map of Column - Value pairs that can be used to select a particular
	 * Assessment Detail Page from the Assessment List
	 * 
	 * @return Map<String, String> get the row Value's Attribute Values
	 */
	private Map<String, String> getRowColumnValues(String formName) {
		Map<String, String> columnValues = new HashMap<String, String>();
		columnValues.put("Assessment", formName);
		columnValues.put("Type", "ClinRO");
		return columnValues;
	}

	private final String scoreDataformName = "MMSE";
	private int SVID;

	public int getSVIDvalue() {
		Log.logInfo("Getting SVID value...");
		WebElement row = assessmentList.getRow(getRowColumnValues(scoreDataformName));
		if (row == null)
			throw new SkipException("No Assessment with the following properties "
					+ getRowColumnValues(scoreDataformName) + " could be found...");
		SVID = Integer.parseInt(assessmentList.getColumnData("SVID", row));
		Log.logInfo("Found SVID with value: " + SVID);
		return SVID;
	}

	/*
	 * public void clickRow() { assessmentDetails =
	 * assessmentList.clickRow(getRowColumnValues(scoreDataformName)); }
	 */

	public void clickScoresTabLink() {
		scoresTab = assessmentDetails.clickScoresTabLink();
	}

	private FDEFParser fdefParser = null;

	public void parseFDEF() {
		if (null == fdefParser)
			fdefParser = new FDEFParser("MMSE_FDEF_XML.xml");
	}

	public void verifyQuestionGroupingsFDEFtoUI() {
		List<Score> scoresFromFDEF = fdefParser.getScores();
		HardVerify.True(scoresTab.checkQuestionsAndGrouping(scoresFromFDEF),
				"Test if the Questions are grouped as specified by FDEF file...", "[TEST PASSED]",
				"The Question Groupings are not as specified by FDEF file!!!");
	}

	public void verifyAnswerOptionsFDEFtoUI() {
		HardVerify.True(scoresTab.checkPossibleAnswers(fdefParser.getPossibleAnswers()),
				"Test if the Answers are as specified by FDEF file...", "[TEST PASSED]",
				"the Answers are not as specified by FDEF file!!!");
	}

	public void verifyQuestionGroupingsDBtoUI(String formName, int svid) {
		HardVerify.True(scoresTab.checkAgainstDatabase(formName, svid),
				"Test if the Questions and Answers are as specified by the Database...", "[TEST PASSED]",
				"The Question and Answer value pairs are not as specified by the Database!!!");
	}

	public void clickPlayVideo() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		assessmentDetails.clickPlayVideo();
	}

	public void clickCancelVideo() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		assessmentDetails.clickCancelVideo();
	}

	private VideoPlayerHelper vpHelper = null;

	public void instantiateVPHelper() {
		vpHelper = new VideoPlayerHelper(Browser.getDriver());
	}

	public void verifyVideoPlayerInitialStatusPaused() {
		Log.logInfo("Waiting for the page and video player to load...");
		HardVerify.True(vpHelper.isStatus("is-paused"), "Test that the initial Status of the Video Player is paused...",
				"[TEST PASSED]",
				"The video player's status was not \"Puased\" initially. It was : " + vpHelper.getStatusFor("is-paused"));
	}

	public void waitForVideoBuffering() {
		Log.logInfo("Waiting for the video to finish buffering...");
		vpHelper.clickScreen(); // to initiate buffering
		vpHelper.waitForVideoToFullyLoad(60);
	}

	public void verifyPlayingStatusAfterClickingPlay() {
		Log.logStep("Clicking Play Video button...");
		vpHelper.clickPlayOrPause();
		HardVerify.True(vpHelper.isStatus("is-playing"),
				"Test that the Status of the Video Player is playing after clicking play...", "[TEST PASSED]",
				"The video player's status was not playing after clicking play. It was : " + vpHelper.getStatusFor("is-playing"));
	}

	public void verifyPausedStatusAfterClickingPause() {
		Log.logStep("Clicking Pause Video button...");
		vpHelper.clickPlayOrPause();
		HardVerify.True(vpHelper.isStatus("is-paused"),
				"Test that the Status of the Video Player is paused after clicking pause...", "[TEST PASSED]",
				"The video player's status was not paused after clicking pause. It was : " + vpHelper.getStatusFor("is-paused"));
	}

	public void verifyVolumeStatusAfterClickingMute() {
		Log.logStep("Clicking Volume controls...");
		HardVerify.True(vpHelper.checkVolumeControl(), "Test if the Volume Control mutes correctly...", "[TEST PASSED]",
				"The Volume Control does not work correctly.");
	}

	public void verifyPlayingStatusAfterClickingScreen() {
		Log.logStep("Clicking the Video Player's screen...");
		vpHelper.clickScreen();
		HardVerify.True(vpHelper.isStatus("is-playing"),
				"Test that the Status of the Video Player is playing after clicking on the screen...", "[TEST PASSED]",
				"The video player's status was not playing after clicking the screen. Status was : "
						+ vpHelper.getStatusFor("is-playing"));
	}

	public void verifyPausedStatusAfterClickingScreen() {
		Log.logStep("Clicking the Video Player's screen...");
		vpHelper.clickScreen();
		HardVerify.True(vpHelper.isStatus("is-paused"),
				"Test that the Status of the Video Player is paused after clicking on the screen...", "[TEST PASSED]",
				"The video player's status was not paused after clicking the screen. Status was : "
						+ vpHelper.getStatusFor("is-paused"));
	}

	public void verifyFullscreenToggleLink() {
		HardVerify.True(vpHelper.checkFullscreenToggleLink(),
				"Test that the full-screen toggler is present and enabled...", "[TEST PASSED]",
				"The full-screen toggle link did not work as expected.");
	}

	public void verifyPlayingStatusAfterPressingSpace() {
		Log.logStep("Clicking Space key...");
		vpHelper.keypressSpaceOnScreen();
		HardVerify.True(vpHelper.isStatus("is-playing"),
				"Test that the Status of the Video Player is playing after pressing SPACE...", "[TEST PASSED]",
				"The video player's status was not playing after pressing SPACE. Status was : "
						+ vpHelper.getStatusFor("is-playing"));
	}

	public void verifyPausedStatusAfterPressingSpace() {
		Log.logStep("Clicking Space key...");
		vpHelper.keypressSpaceOnScreen();
		HardVerify.True(vpHelper.isStatus("is-paused"),
				"Test that the Status of the Video Player is paused after pressing SPACE...", "[TEST PASSED]",
				"The video player's status was not paused after pressing SPACE. Status was : "
						+ vpHelper.getStatusFor("is-paused"));
	}

	public void verifyAssessmentLanguage(String language) {
		if (assessmentDetails != null) {
			String languageUI = assessmentDetails.getAssessmentLanguage();
			HardVerify.Equals(language, languageUI, "Verifying if the Assessment Language is correct...",
					"Assessment Language is found correct.From UI:" + languageUI + ",From User:" + language,
					"Assessment Language didn't match. From UI:" + languageUI + ",From User:" + language);
		}
	}

	public void getStatusFromAssessmentList(String formName, String svid) {
		row = assessmentList.getRowForAnAssessment(formName, svid);
		statusFromList = assessmentList.getColumnData("Status", row);
	}

	public void verifyAssessmentStatus() {
		if (assessmentDetails != null) {
			String statusDetailsPage = assessmentDetails.getAssessmentStatus();
			HardVerify.True(statusFromList.equalsIgnoreCase(statusDetailsPage),
					"Verifying if the Assessment status is correct...",
					"Assessment status is found correct.From Details Page:" + statusDetailsPage + ",From List Page:"
							+ statusFromList,
					"Assessment status didn't match. From Details Page:" + statusDetailsPage + ",From List Page:"
							+ statusFromList);
		}
	}

	public void clickStudyLink() {
		Log.logStep("Opening the Study Profile from the Assessment Details grid link...");
		assessmentDetails.clickOnGridLink("Study");
		studyProfile = PageFactory.initElements(Browser.getDriver(), StudyProfile.class);
	}

	public void closeStudyProfile() {
		Log.logStep("Closing Study Profile from the Assessment Details grid link...");
		assessmentDetails.clickOnGridLink("Study");
	}

	public void verifyStudyProfileOpened() {
		HardVerify.True(studyProfile.isStudyProfileOpened(),
				"Verify Study Profile page opens after clicking Study link on the Assessment Details page",
				"[TEST PASSED]", "The Study link on the Assessment Details page not working");
	}

	public void verifyStudyProfileClosed() {
		HardVerify.False(studyProfile.isStudyProfileOpened(),
				"Verify Study Profile page closes after clicking Study link on the Assessment Details page",
				"[TEST PASSED]", "The Study link on the Assessment Details page not working");
	}

	private String subjNameOnAssessGrid;

	public void getSubjectNameOnAssessmentDetailsPage() {
		subjNameOnAssessGrid = assessmentDetails.getAssessmentDetailsItemValue("Subject");
		subjNameOnAssessGrid = TextHelper.splitSpaces(subjNameOnAssessGrid)[0];
	}

	public void clickSubjectLink() {
		Log.logStep("Going to the Subject Details page using the Assessment Details grid link...");
		assessmentDetails.clickOnGridLink("Subject");
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
	}

	public void verifySubjectDetailsOpened() {
		HardVerify.True(subjectDetails.isOpened(), "Verifying if the Subject Details page is open...", "[TEST PASSED]",
				"Subject Details page link on the Assessment Details Grid is not working");
	}

	private String subjNameOnDetailPage;

	public void getSubjectNameOnSubjectDetailsPage() {
		subjNameOnDetailPage = subjectDetails.getSubjNameFromHeader();
	}

	public void verifySubjectNameMatched() {
		HardVerify.Equals(subjNameOnAssessGrid, subjNameOnDetailPage, "Verifying if the Subject Name matches...",
				"Subject Name matched. From Subject Detail page:" + subjNameOnDetailPage
						+ ",From Assessment Details Grid:" + subjNameOnAssessGrid,
				"Subject Name didn't match. From Subject Detail page:" + subjNameOnDetailPage
						+ ",From Assessment Details Grid:" + subjNameOnAssessGrid);
	}

	public void clickGridVisitLink() {
		Log.logStep("Going to the Visit Details page using the Assessment Details grid link...");
		assessmentDetails.clickOnGridLink("Visit");
		Log.logStep("Redirecting to the Visit details page...");
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
	}

	public void clickGridHeaderVisitLink() {
		Log.logStep("Going to the Visit Details page using the Assessment Details Header grid link...");
		assessmentDetails.clickOnGridHeaderLink("Visit");
		Log.logStep("Redirecting to the Visit details page...");
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
	}

	public void verifyVisitDetailsOpened(String svid, String gridType) {
		HardVerify.True(visitDetails.detailsIsOpened(svid),
				"Verify 'Visit Details' page opens after clicking " + gridType
						+ " visit link on Assessment Details page",
				"[TEST PASSED]", gridType + " visit link on the Assessment Details page not working");
	}

	public void selectAllDetailsViewMode() {
		assessmentDetails.selectViewMode("All Details");
	}

	private String raterNameUI;

	public void getRaterNameOnAssessmentDetailsPage() {
		raterNameUI = assessmentDetails.getAssessmentDetailsItemValue("Rater");
	}

	public void clickGridRaterLink() {
		Log.logStep("Going to the Rater's Details page using the Assessment Details grid link...");
		assessmentDetails.clickOnGridLink("Rater");
		Log.logStep("Redirecting to Rater's Details page...");
		raterDetails = PageFactory.initElements(Browser.getDriver(), RaterDetails.class);
	}

	public void getRaterNameOnGridHeaderLink() {
		raterNameUI = assessmentDetails.getGridHeaderValue("Rater");
	}

	public void clickGridHeaderRaterLink() {
		Log.logStep("Going to the Rater's Details page using the Assessment Details Header grid link...");
		assessmentDetails.clickOnGridHeaderLink("Rater");
		Log.logStep("Redirecting to Rater's Details page...");
		raterDetails = PageFactory.initElements(Browser.getDriver(), RaterDetails.class);
	}

	public void verifyRaterDetailsOpened(String gridType) {
		HardVerify.True(raterDetails.isOpened(raterNameUI),
				"Verify 'Rater Details' page opens after clicking " + gridType
						+ " rater link on Assessment Details page",
				"[TEST PASSED]", gridType + " rater link on the Assessment Details page not working");
	}

	public void verifyPaperTranscriptionCheckbox() {
		HardVerify.True(assessmentDetails.isPaperTranscription(),
				"Verifying if Paper Transcription is checked for this paper transcription assessment...",
				"Paper Transcription checkbox is checked.", "Paper Transcription checkbox is NOT checked.");
	}

	private String modeUI;

	public void getAssessmentDetailsCurrentViewMode() {
		modeUI = assessmentDetails.getViewMode();
	}

	public void verifyDefaultViewModeAllDetails() {
		getAssessmentDetailsCurrentViewMode();
		HardVerify.Equals(modeUI, "All Details",
				"Verifying current view Mode is 'All Details' on Assessment Details page...", "[TEST PASSED]",
				"current view Mode is not 'All Details'. View mode found as:" + modeUI);
	}

	public boolean selectViewMode(String viewMode) {
		boolean selected = assessmentDetails.selectViewMode(viewMode);
		if (!selected)
			throw new SkipException("No such View Mode found in the list.");
		return selected;
	}

	public void verifyCurrentViewMode(String viewMode) {
		HardVerify.True(assessmentDetails.verifyViewMode(viewMode),
				"Verifying if View Mode: [" + viewMode + "] has been selected...", "[TEST PASSED]",
				"View Mode didn't match. From UI: " + modeUI + " | From user: " + viewMode);
	}

	public void clickRefresh() {
		Log.logStep("Clicking refresh button for View Mode: [" + assessmentDetails.getViewMode() + "]...");
		assessmentDetails = assessmentDetails.checkRefresh();
		if (null != assessmentDetails) {
			Log.logInfo("Assessment Details Page refreshed");
		} else {
			Log.logInfo("Assessment Details Page not refreshed correctly. Skipping test...");
			throw new SkipException("Assessment Details Page not refreshed correctly");
		}
	}

	public void verifyRefreshOnAssessmentDetails(String viewMode) {
		HardVerify.True(assessmentDetails.verifyViewMode(viewMode),
				"Verifying if Refresh button is working as expected for view mode: " + viewMode, "Test Paseed",
				"Refresh button is not working as expected for view mode: " + viewMode);
	}

	public void clickCancelModeButton(String viewMode) {
		Log.logStep("Clicking on the cancel button for View Mode: [" + viewMode + "]...");
		assessmentDetails.clickCancelModeButton();
	}

	public void verifyRaterNameWithUI(String raterName) {
		HardVerify.Equals(raterName, raterNameUI,
				"Verifying if the Rater's name matches with that provided by the tester...",
				"Name matched. Rater's Name:" + raterNameUI + ", Tester Provided Name:" + raterName,
				"Name didn't match. Rater's Name:" + raterNameUI + ", Tester Provided Name:" + raterName);
	}
	
	/**
	 * The List containing all the options of the View Modes for an Assessment with a Video
	 * @return List<String> of all the VIew Modes that should be available
	 */
	private List<String> getExpectedVideoViewModes() {
		String[] views = new String[] { "All Details", "Video + Source", "Video + Scores", "Source + Scores" };
		return Arrays.asList(views);
	}
	
	public void verifyAllVideoViewModes() {
		HardVerify.Equals(getExpectedVideoViewModes(), assessmentDetails.getAllViewModes(),
				"Test that the available View Modes are correct...", "[TEST PASSED]",
				"Expected " + getExpectedVideoViewModes() + " " + " but was " + assessmentDetails.getAllViewModes());
	}
	
	/**
	 * The List containing all the options of the View Modes for an Assessment without a Video
	 * @return List<String> of all the View Modes that should be available
	 */
	private List<String> getExpectedAudioViewModes() {
		String[] views = new String[] { "All Details", "Source + Scores" };
		return Arrays.asList(views);
	}
	
	public void verifyAllAudioViewModes() {
		HardVerify.Equals(getExpectedAudioViewModes(), assessmentDetails.getAllViewModes(),
				"Test that the available View Modes are correct...", "[TEST PASSED]",
				"Expected " + getExpectedAudioViewModes() + " " + " but was " + assessmentDetails.getAllViewModes());
	}
	
	public void verifyAllViewModes() {
		verifyAllAudioViewModes();
	}
	
	private Double videoTimeline;
	public void playVideoForSomeTime(int time) {
		vpHelper = new VideoPlayerHelper(Browser.getDriver());
		Log.logStep("Playing Video for "+ time + " seconds...");
		vpHelper.playForSomeTime(5);
		videoTimeline = vpHelper.getVideoTimeline();
	}
	
	private Double audioTimeline;
	public void playAudioForSomeTime(int time) {
		audioHelper = new AudioPlayerHelper(Browser.getDriver());
		Log.logStep("Playing Audio for "+ time + " seconds...");
		audioHelper.playForSomeTime(3);
		audioTimeline = audioHelper.getTimeline();
	}
	
	private PDFViewerHelper pdfHelper;
	public void scrollPageOnPdfViewr(String pageNumber) {
		pdfHelper = new PDFViewerHelper(Browser.getDriver());
		Log.logStep("Moving to page " + pageNumber + " for the PDF on the PDF Viewer...");
		pdfHelper.goToPageNumber(pageNumber);
	}
	
	public void verifyPdfPageNumberNotChanged(String pageNumber) {
		HardVerify.Equals(pageNumber, pdfHelper.getCurrentPageNumber(),
				"Test if the PDF Viewer's page number is the same after closing and reopening the View mode...",
				"[TEST PASSED]",
				"Expected page number to be [" + pageNumber + "] but was [" + pdfHelper.getCurrentPageNumber() + "]");
	}
	
	public void verifyPdfPageNumberIsAtBeginnig() {
		HardVerify.Equals("1", pdfHelper.getCurrentPageNumber(),
				"Test if the PDF Viewer's page number is set to 1 after clicking refresh...", "[TEST PASSED]",
				"Expected page number to be [1] but was [" + pdfHelper.getCurrentPageNumber() + "]");
	}
	
	public void verifyVideoPlayerTimelineNotChanged() {
		HardVerify.Equals(videoTimeline, vpHelper.getVideoTimeline(),
				"Test if the Video Player's timeline remains the same after closing and reopening the View mode...",
				"[TEST PASSED]",
				"Expected timeline to be [" + videoTimeline + "] but was [" + vpHelper.getVideoTimeline() + "]");
	}
	
	public void verifyAudioPlayerTimelineNotChanged() {
		HardVerify.Equals(audioTimeline, audioHelper.getTimeline(),
				"Test if the Audio Player stays in the same state after closing the View Mode...", "[TEST PASSED]",
				"The Audio Player's timeline changed from [" + audioTimeline + "] to [" + audioHelper.getTimeline() + "]");
	}
	
	public void verifyVideoPlayerTimelineIsAtBeginnig() {
		HardVerify.Equals(0.0, Math.floor(vpHelper.getVideoTimeline()),
				"Test if the Video Player's timeline is set to 0% after clicking refresh...", "[TEST PASSED]",
				"Expected timeline to be around [0] but was [" + vpHelper.getVideoTimeline() + "]");
	}
	
	public void verifyAudioPlayerTimelineIsAtBeginnig() {
		HardVerify.Equals(0.0, Math.floor(audioHelper.getTimeline()),
				"Test if the Audio Player's timeline is set to 0% after clicking refresh...", "[TEST PASSED]",
				"Expected timeline to be around [0] but was [" + audioHelper.getTimeline() + "]");
	}
	
	public void clickOnFormPDF() {
		Log.logStep("Clicking the PDF Form...");
		assessmentDetails.clickOnFormPDF();
	}
	
	/**
	 * 
	 * Steps for Test Class: NavigationTests
	 * 
	 */
	private WebElement visitRow;

	public void getVisitRow(String colName, String value) {
		visitList = PageFactory.initElements(Browser.getDriver(), VisitList.class);
		visitRow = visitList.getRowForVisit(colName, value);
	}

	public void getToVisitDetails(String svid) {
		if (null != visitRow) {
			Log.logStep("Clicking Visit row for svid : " + svid);
			visitDetails = visitList.clickAVisit(visitRow);
		}
	}

	public void verifyVisitDetailsIsOpened(int svid) {
		HardVerify.True(visitDetails.detailsIsOpened(svid), "Verifying Navigation to Visit Details through a Visit",
				"[TEST PASSED]",
				"Visit Details page is not open. Navigation to Visit Details through a Visit is NOT working.");
	}

	public void verifyVisitDetailsIsOpened(String visitName) {
		HardVerify.True(visitDetails.detailsIsOpened(visitName),
				"Verifying Navigation to Visit Details through a Visit", "[TEST PASSED]",
				"Visit Details page is not open. Navigation to Visit Details through a Visit is NOT working.");
	}

	public void clickAssessmentThumbnailAtIndex(int index) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);

		if (subjectDetails.isOpened()) {
			assessmentDetails = subjectDetails.clickFormThumbnail(index);
		} else if (visitDetails.detailsIsOpened()) {
			assessmentDetails = visitDetails.clickFormThumbnail(index);
		}
	}

	public void verifyAssessmentDetailsIsOpenedVia(String via) {
		HardVerify.True(assessmentDetails.isOpened(), "Verifying Navigation to Assessment Details through " + via,
				"[TEST PASSED]", "Assessment details page is not open. Navigation to Assessment Details through " + via
						+ " is NOT working.[TEST FAILED]");
	}
	
	public void getToSubjectDetails(String subjectName) {
			Log.logStep("Clicking row for Subject : " + subjectName);
			subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
			subjectDetails =  subjectList.clickRowForSubject(subjectName);
	}
	
	public void verifySubjectDetailsIsOpened() {
		HardVerify.True(subjectDetails.isOpened(),
				"Verifying Navigation to Subject Details through a Subject",
				"[TEST PASSED]",
				"Subject Details page is not open. Navigation to Subject Details through a Subject is NOT working.");
	}
	
	public void clickVisitRowFor(String visitName) {
		//TODO replace with appropriate method
		subjectDetails.clickVisitRow(visitName);
		UiHelper.checkPendingRequests(Browser.getDriver());
	}
	
	public void getToVisitDetails(String visitName, int svid) {
		visitList = PageFactory.initElements(Browser.getDriver(), VisitList.class);
		Log.logStep("Clicking Visit row for " + visitName + " & " + svid);
		visitDetails = visitList.clickRow(visitName, svid);
		if (null != visitDetails)
			Log.logInfo("Details page is open for Visit: " + visitName);
	}

	/**
	 * @author HISHAM
	 * 
	 * @param subjectName
	 * @param visitName
	 * @param assessmentName
	 */
	public void getToVisitDetails(String visitName, String subjectName) {
		visitList = PageFactory.initElements(Browser.getDriver(), VisitList.class);
		Log.logStep("Clicking row for Visit: [" + visitName + "] of Sub#: [" + subjectName + "]");
		visitDetails = visitList.clickRow(subjectName, visitName);
	}

	public void deleteAttachment(int attachmentsIndex) {
		if (numberOfAttachments == 0)
			throw new SkipException("There were no attachments found for the Assessment");
		Log.logStep("Clicking the delete button for the Attachment at the [" + attachmentsIndex + "] index...");
		attachmentsTab.clickDeleteAttachmentAtIndex(attachmentsIndex);
		
	}

	/**
	 * @author ubiswas 
	 * 
	 * Description: Open assessment details page..
	 * 
	 * @param subjectName
	 * @param visitName
	 * @param assessmentName
	 */
	public void getToAssessmentDetails(String subjectName, String visitName, String assessmentName) {
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		Log.logStep("Clicking Assessment row for " + assessmentName + " & " + visitName +"of Sub#: " + subjectName);
		assessmentDetails = assessmentList.clickRow(subjectName, visitName, assessmentName);
		if (null != assessmentDetails)
			Log.logInfo("Details page is open for Assessment: " + assessmentName);
	}

	/**
	 * @author ubiswas
	 * 
	 * Description: open selected visit details based on visit
	 *         		name and assessment name
	 * @param assessmentName
	 * @param visitName
	 * @param gridType
	 */
	public void verifyVisitDetailsOpened(String assessmentName, String visitName, String gridType) {
		HardVerify.True(visitDetails.detailsOfSelectedVisit(assessmentName, visitName),
				"Verify 'Visit Details' page opens after clicking " + gridType
						+ " visit link on Assessment Details page",
				"[TEST PASSED]", gridType + " visit link on the Assessment Details page not working");

	}

	public void getStatusFromAssessmentList(String subjectName, String assessmentName, String visitName) {
		row = assessmentList.getRowSelectedAssessment(subjectName, assessmentName, visitName);
		statusFromList = assessmentList.getColumnData("Status", row);

	}
	
	public int getSVIDFromAssessment(String subjectName, String assessmentName, String visitName) {
		return this.getSVIDvalue(subjectName, assessmentName, visitName);
	}

	/**
	 * @author ubiswas 
	 * 
	 * Description: get SVID based on formName
	 * @param subjectName
	 * @param assessmentName
	 * @param visitName
	 * @return
	 */
	private int getSVIDvalue(String subjectName, String assessmentName, String visitName) {
		Log.logInfo("Getting SVID value...");
		row = assessmentList.getRowSelectedAssessment(subjectName, assessmentName, visitName);
		if (row == null)
			throw new SkipException("No Assessment with the following properties "
					+ getRowColumnValues(assessmentName) + " could be found...");
		SVID = Integer.parseInt(assessmentList.getColumnData("SVID", row));
		Log.logInfo("Found SVID with value: " + SVID);
		return SVID;
	}
	
	/**
	 * @author UTTAM
	 * 
	 * Description - Uncheck assessment flag and click on Confirm button , checking eye characters visibility on e-sign dialog window
	 */
		public void checkOrUncheckAssessmentFlagCheckboxAndClickConfrimBtn(String type) {
			assessmentDetails.checkRefresh();
			assessmentDetails.clinkOnCheckbox(type);
			HardVerify.True(assessmentDetails.checkConfirmBtnIsVisible(),
					"Checking Confirm button is visible after uncheking checkbox ", "[PASSED : Confirm button is visible]",
					"[FAILED : Confrim button is not visible]");
			esignStep = assessmentDetails.confirmAssesment();
		}

		public void eyeCharactersVisibilityChecking(String login, String pass, String type, String reason) {
			HardVerify.True(esignStep.isDialogOpened(), "Cheking dialog window is opened", "[PASSED: Dialog is opened]",
					"[FAILED: Failed to open e-sign dialog]");
			esignStep.inputPredefinedReason(reason);
			esignStep.enterPassAndValidateEyeCharacters(login, pass);
			/*if (!type.equalsIgnoreCase("Paper")) {
				esignStep.enterPassAndValidateEyeCharacters(login, pass);
			} else {
				esignStep.inputPredefinedReason(reason);
				esignStep.loginAs(login, pass);
			}*/
		}
}
