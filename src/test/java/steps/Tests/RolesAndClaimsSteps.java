package steps.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.AttachmentsTab;
import mt.siteportal.details.ScoresTab;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.pages.Administration.Administration;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.pages.studyDashboard.VisitsInSubject;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.AdminType;
import mt.siteportal.utils.Enums.MAUserType;
import mt.siteportal.utils.Enums.NonMAUserType;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.QueryPanelItem;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.DetailPages.AddSubject;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import nz.siteportal.pages.studydashboard.ListPages.SubjectList;
import steps.Configuration.CommonSteps;

/**
 * Steps Class for the Roles And Claims Tests
 * 
 * @author Syed A. Zawad
 *
 */
public class RolesAndClaimsSteps extends CommonSteps {

	/*
	 * The Page objects needed for all Steps
	 */
	private LoginPage loginPage;
	private Dashboard studyDashboard;
	private HomePage homePage;
	private Administration administrationDashboard;
	private AssessmentList assessmentList;
	private DashboardList dashboardList;
	private AssessmentDetails assesmentDetails;
	private AttachmentsTab attachmentsTab;
	private ScoresTab scoresTab;
	private QueriesSidePanel qSidePanel;
	private QueryPanelItem qPanelItem;
	private VisitsInSubject visitsInSubject;
	

	/*
	 * The WebDriver used for actions
	 */
	private WebDriver driver;

	/*
	 * URLs needed
	 */
	private String siteportalURL, maportalURL, sponsorportalURL;
	
	/*
	 * User Type definitions
	 */
	protected List<String> definedAdminUserTypes = new ArrayList<String>(Arrays.asList(
			"MedAvante Administrator - Type 2", "MedAvante Administrator - Type 3", "MedAvante Administrator - Type 4",
			"System Administrator - Type 1", "System Administrator - Type 2"));

	protected List<String> definedMAUserTypes = new ArrayList<String>(Arrays.asList("MedAvante User Type 1",
			"MedAvante User Type 2", "MedAvante User Type 3", "MedAvante User Type 4"));

	protected List<String> definedSponsorUserTypes = new ArrayList<String>(
			Arrays.asList("Sponsor User Type 1", "Sponsor User Type 2", "Sponsor User Type 3"));

	protected List<String> definedSiteUserTypes = new ArrayList<String>(
			Arrays.asList("Site Coordinator", "Site Rater - Type 1", "Site Rater - Type 2", "Site Rater - Type 3",
					"Site Rater - Type 4", "Site Rater - Type 5"));

	/**
	 * Constructor - initializes the variables required for the Steps
	 * 
	 * @param driver
	 *            - WebDriver, the driver used by the page objects
	 */
	public RolesAndClaimsSteps(WebDriver driver) {
		this.driver = driver;
		loginPage = PageFactory.initElements(driver, LoginPage.class);
		studyDashboard = PageFactory.initElements(driver, Dashboard.class);
		homePage = PageFactory.initElements(driver, HomePage.class);
		administrationDashboard = PageFactory.initElements(driver, Administration.class);
		assessmentList = PageFactory.initElements(driver, AssessmentList.class);
		assesmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		dashboardList = PageFactory.initElements(driver, DashboardList.class);
		scoresTab = PageFactory.initElements(driver, ScoresTab.class);
		attachmentsTab = PageFactory.initElements(driver, AttachmentsTab.class); 
		// TODO : needs to be changed to accommodate for other environments
		siteportalURL = URLsHolder.getHolder().getSiteportalURL();
		// TODO : needs to be changed to accommodate for other environments
		maportalURL = URLsHolder.getHolder().getMaportalURL();
		sponsorportalURL = URLsHolder.getHolder().getSponsorportalURL();
	}
	
	public boolean goToPortalLoginPage(String portalURL) {
				
		if (portalURL.equalsIgnoreCase(maportalURL)) {
			return goToMedAvantePortalLoginPage();
		 } else if (portalURL.equalsIgnoreCase(siteportalURL)) {
			return goToSitePortalLoginPage();
		 } else if (portalURL.equalsIgnoreCase(sponsorportalURL)) {
			return goToSponsorPortalLoginPage();
		 }
		return false;
	}
	
	/**
	 * Clear all the browser cookies and navigate to the MedAvante's Portal url.
	 * The login page should be displayed
	 * 
	 * @return - boolean - true if all the steps executed successfully, false
	 *         otherwise
	 */
	public boolean goToMedAvantePortalLoginPage() {
		Log.logStep("Clearing all cookies");
		Browser.getDriver().manage().deleteAllCookies();
		Log.logStep("Going to Maportal Login page");
		Nav.toURL(maportalURL);
		Log.logStep("Returning true if login page is opened, false otherwise: " + loginPage.isLoginPageOpened());
		return loginPage.isLoginPageOpened();
	}

	/**
	 * Clear all browser cookies and navigate to the Site portal's url. The
	 * login page should be displayed.
	 * 
	 * @return - boolean - true if all the steps were successful, false
	 *         otherwise
	 */
	public boolean goToSitePortalLoginPage() {
		Log.logStep("Clearing all cookies");
		Browser.getDriver().manage().deleteAllCookies();
		Log.logStep("Going to Siteportal Login page");
		Nav.toURL(siteportalURL);
		Log.logStep("Returning true if login page is opened, false otherwise: " + loginPage.isLoginPageOpened());
		return loginPage.isLoginPageOpened();
	}

	/**
	 * Clear all the browser cookies and navigate to the Sponsor Portal url.
	 * The login page should be displayed
	 * 
	 * @return - boolean - true if all the steps executed successfully, false
	 *         otherwise
	 */
	public boolean goToSponsorPortalLoginPage() {
		Log.logStep("Clearing all cookies");
		Browser.getDriver().manage().deleteAllCookies();
		Log.logStep("Going to Sponsorportal Login page");
		Nav.toURL(sponsorportalURL);
		Log.logStep("Returning true if login page is opened, false otherwise: " + loginPage.isLoginPageOpened());
		return loginPage.isLoginPageOpened();
	}

	/**
	 * Enter the given username and password in the login page for specific
	 * portal and click Login button.
	 * 
	 * @param username
	 *            - String - the username for login
	 * @param password
	 *            - String - the password for login
	 * @return - boolean - true if all the test were in sequence, false
	 *         otherwise
	 */
	public boolean loginWithCredentials(String username, String password) {
		if (Browser.getDriver().getCurrentUrl().contains("maportalat.medavante.net")) {
			if (null != loginPage.loginAsAdmin(username, password))
				return true;
		} else if (Browser.getDriver().getCurrentUrl().contains("siteportalat.medavante.net")) {
			if (null != loginPage.loginAsSitePerson(username, password))
				return true;
		} else if (Browser.getDriver().getCurrentUrl().contains("sponsorportalat.medavante.net")) {
			if (null != loginPage.loginAsSponsorPerson(username, password))
				return true;
		}
		return false;
		// return !loginPage.isLoginPageOpened();
		// Log.logStep("Returning true if login page is opened, false otherwise:
		// " + loginPage.isLoginPageOpened());
		// return loginPage.isLoginPageOpened();
	}

	

	/**
	 * If the current;y displayed page is the Administration Page, do nothing
	 * Otherwise, go the home page by directly navigating to its url and then 
	 * click on the Administration Dashboard link. The Administration Dashboard
	 * should be opened next 
	 * 
	 * @return - boolean - true if all the steps executed successfully, false
	 *         otherwise
	 */
	public boolean getToAdminDashboard() {
		administrationDashboard = PageFactory.initElements(Browser.getDriver(), Administration.class);
		if (administrationDashboard.isOpen())
			return true;
//		if (goToHomePage())
		homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.openAdministration();
		return administrationDashboard.isOpen();
	}
	
	/**
	 * If the currently displayed page is Study Dashboard Page, do nothing
	 * Otherwise, go the home page by directly navigating to its url and then 
	 * click on the Study Dashboard link. Study Dashboard should be opened next
	 * 
	 * @return - boolean - true if all the steps executed successfully, false
	 * otherwise 
	 */
	public boolean goToStudydashboard() {
		if (studyDashboard.isDashboardOpened())
			return true;
		if (goToHomePage())
			homePage.openDashboard();
		return studyDashboard.isDashboardOpened();
	}
	
	/**
	 * Selects given Study and Site. Returns false for siteName selected as 
	 * null/"". For valid studyName, selects all sites for parameter value as All/""/null
	 * 
	 * @param studyName 
	 * 				- String - the name of the Study to be selected
	 * @param siteName
	 * 				- String - the name of the Site to be selected
	 * @return - boolean - true if all the steps executed successfully,
	 * false otherwise
	 */
	
	public boolean selectStudyAndSite(String studyName, String siteName) {
		if (studyName.equals(null) || studyName.equals(""))
			return false;
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		if (siteName.equalsIgnoreCase("All") || siteName.equals("") || siteName.equals(null)) {
			toolbarFull.chooseStudy(studyName);
			toolbarFull.chooseSite("All Sites");
			return true;
		} else {
			toolbarFull.chooseStudy(studyName);
			toolbarFull.chooseSite(siteName);
			return true;
		}
	}
	
	/**
	 * 
	 * @param assessmentCard Name of the Assessment card to be selected
	 * @param assessmentName Name of the Assessment to be selected
	 * @param svid Assessment ID to be selected
	 * @return - boolean - true if all the steps executed successfully,
	 * false otherwise
	 */
	public boolean selectAssessment(String assessmentCard, String assessmentName, String svid) {
		Log.logStep("Clicking on card: " + assessmentCard);
		studyDashboard.clicksOnCard("Assessments-" + assessmentCard);
		Log.logStep("Clicking on Assessment: " + assessmentName + " with svid: " + svid);
		assesmentDetails = assessmentList.clickRow(assessmentName, svid);
		if (assesmentDetails.isOpened()) {
			Log.logStep("Returning true if Assessment details page is opened, false otherwise. Found : " + assesmentDetails.isOpened());
			return true;
		}
		return false;
	}
	
	/**
	 * If the home page is not opened, go directly to the page via the url
	 * 
	 * @return - boolean - true if successfully opened, false otherwise
	 */
	private boolean goToHomePage() {
		if(!homePage.isHomePageOpened())
			Nav.toURL(driver.getCurrentUrl());
		return homePage.isHomePageOpened();
	}	
	
	/**
	 * @author HISHAM
	 * 
	 * Objective is to check user can access Portal Dashborad feature if he has claim
	 * 
	 * Steps:
	 * Checks if User has claim then:
	 * 	1. Study Dashboard should open for User Type : MedAvante/Sponsor/Site users 
	 * 
	 * Checks if User doesn't have claim then: 
	 *  a. Administration Dashboard should for User Type : Admin 
	 *  or,
	 *  b. Get Authorization failure message for User Type : MedAvante/Sponsor/Site users
	 * 
	 * @param userType
	 * 			- String - The type of user that needs to be matched for verification
	 * @param hasClaim
	 *			- boolean - If true then should be able to access Study Dashboard
	 */
	public void verifyPortalDashboardIsOpened(String userType, boolean hasClaim) {
		if (hasClaim) {
			if (definedMAUserTypes.contains(userType) || definedSponsorUserTypes.contains(userType)
					|| definedSiteUserTypes.contains(userType)) {
				Log.logInfo(userType + " matched");
				HardVerify.True(studyDashboard.isDashboardOpened(),
						"Verifying 'StudyDashboard' is" + (hasClaim ? "" : "n't") + " opened.", "[TEST PASSED]",
						"'StudyDashboard' is" + (hasClaim ? "n't " : "") + " opened. [TEST FAILED]");
			} else {
				Log.logInfo("No match found with defined " + userType + ". Skipping verification...");
				throw new SkipException("No match found with defined " + userType + ". Skipping verification...");
			}
		} else {
			if (definedAdminUserTypes.contains(userType)) {
				Log.logInfo(userType + " matched");
				if (getToAdminDashboard())
					HardVerify.True(administrationDashboard.isOpen(),
							"Verifying 'Admin Dashboard' is" + (hasClaim ? "n't" : "") + " opened.", "[TEST PASSED]",
							"'Admin Dashboard' is" + (hasClaim ? "" : "n't") + " opened. [TEST FAILED]");
			} else if (definedMAUserTypes.contains(userType) || definedSponsorUserTypes.contains(userType)
					|| definedSiteUserTypes.contains(userType)) {
				Log.logInfo(userType + " matched");
				HardVerify.EqualsIgnoreCase(loginPage.getFailureMessage(),
						"Access is not authorized. Please contact MedAvante Customer Support for assistance.",
						"Verifying 'Error message' displays if user doesn't have access", "[TEST PASSED]",
						"[TEST FAILED]");
			} else {
				Log.logInfo("No match found with defined " + userType + ". Skipping verification...");
				throw new SkipException("No match found with defined " + userType + ". Skipping verification...");
			}
		}
	}

	/**
	 * @author HISHAM
	 * 
	 * Objective is to check user can access Admin Dashborad feature if he has claim
	 * 
	 * Steps:
	 * Checks if User has claim then:
	 * 	1. Admin Dashboard should open for User Type : Admin
	 * 
	 * Checks if User doesn't have claim then: 
	 *  a. Study Dashboard should for User Type : MedAvante 
	 *  or,
	 *  b. Get Authorization failure message for User Type : Sponsor/Site users
	 * 
	 * @param userType
	 * 			- String - The type of user that needs to be matched for verification
	 * @param hasClaim
	 *			- boolean - If true then should be able to access Study Dashboard
	 */
	public void verifyAdminDashboardIsOpened(String userType, boolean hasClaim) {
		if (hasClaim) {
			if (definedAdminUserTypes.contains(userType)) {
				Log.logInfo("Admin user type matched");
				if (getToAdminDashboard())
					HardVerify.True(administrationDashboard.isOpen(),
							"Verifying 'Admin Dashboard' is" + (hasClaim ? "" : "n't") + " opened.", "[TEST PASSED]",
							"'Admin Dashboard' is" + (hasClaim ? "n't" : "") + " opened. [TEST FAILED]");
			} else {
				Log.logInfo("No match found with defined 'Admin' User Types. Skipping verification...");
				throw new SkipException("No match found with defined 'Admin' User Types. Skipping verification...");
			}
		} else {
			if (definedMAUserTypes.contains(userType)) {
				Log.logInfo("MedAvante user type matched");
				HardVerify.True(studyDashboard.isDashboardOpened(),
						"Verifying 'StudyDashboard' is" + (hasClaim ? "n't" : "") + " opened.", "[TEST PASSED]",
						"'StudyDashboard' is" + (hasClaim ? "" : "n't") + " opened. [TEST FAILED]");
			} else if (definedSiteUserTypes.contains(userType) || definedSponsorUserTypes.contains(userType)) {
				Log.logInfo("Site/Sponsor user type matched");
				HardVerify.EqualsIgnoreCase(loginPage.getFailureMessage(),
						"Access is not authorized. Please contact MedAvante Customer Support for assistance.",
						"Verifying 'Error message' displays if user doesn't have access", "[TEST PASSED]",
						"[TEST FAILED]");
			} else {
				Log.logInfo(
						"No match found with defined 'MedAvante/Sponsor/Site' User Types. Skipping verification...");
				throw new SkipException(
						"No match found with defined 'MedAvante/Sponsor/Site' User Types. Skipping verification...");
			}
		}
	}
	
	/**
	 * Checks for Attachments tab in Assessment Details Page. If found checks if
	 * user has manage access to Paper Transcription assessments
	 * 
	 * @param hasClaim
	 *            - boolean - if true, then user can view edit button on
	 *            AssessmentDetails page and Scores tab
	 */
	public void verifyEnterPaperTranscriptionLinkIsClickable(boolean hasClaim) {	
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		if (hasClaim) {
			HardVerify.True(subjectDetails.enterPaperTranscriptionLinkIsClickable(),
					"Verifying User can" + (hasClaim ? "" : "'t") + " click 'Enter Paper transcription' link", "[TEST PASSED]",
					"User can" + (hasClaim ? "'t " : "") + " click 'Enter Paper transcription' link. [TEST FAILED]");
		} else {
			HardVerify.False(subjectDetails.enterPaperTranscriptionLinkIsClickable(),
					"Verifying User can" + (hasClaim ? "'t " : "") + " click 'Enter Paper transcription' link", "[TEST PASSED]",
					"User can" + (hasClaim ? "" : "'t") + " click 'Enter Paper transcription' link. [TEST FAILED]");
		}
	}
	

	public void verifyManageAssessmentControls(boolean hasClaim) {
		Log.logStep(
				"Test if the user does " + (hasClaim ? "" : "not ") + "have access to Assessment Controls...");
		if (hasClaim) {
			Log.logVerify("Verifying 'Edit Assessment Details' " + (hasClaim ? "" : "not ") + "displayed");
			HardVerify.True(UiHelper.isEnabled(assesmentDetails.getEditButton()),
					"Verifying 'Edit assessment' control is displayed", "[TEST PASSED].",
					"'Edit assessment' control is not displayed.");
			Log.logVerify("Verifying 'Edit Scores' " + (hasClaim ? "" : "not ") + "displayed");
			HardVerify.True(UiHelper.isEnabled(scoresTab.getEditButton()), "Verifying 'Edit Scores' is displayed",
					"[TEST PASSED].", "'Edit Scores' control is not displayed.");
		} else {
			Log.logVerify("Verifying 'Edit Assessment Details' is " + (hasClaim ? "" : "not ") + "displayed");
			HardVerify.False(UiHelper.isEnabled(assesmentDetails.getEditButton()),
					"Verifying 'Edit assessment' control is displayed", "[TEST PASSED].",
					"'Edit assessment' control is not displayed.");
			Log.logVerify("Verifying 'Edit Scores' is" + (hasClaim ? "" : "not ") + "displayed");
			HardVerify.True(UiHelper.isEnabled(scoresTab.getEditButton()), "Verifying 'Edit Scores' is displayed",
					"[TEST PASSED].", "'Edit Scores' control is not displayed.");
		}
	}
	
	
	/**
	 * Checks for Attachments tab in Assessment Details Page. If found checks if
	 * user has view access to PDF attachments
	 * 
	 * @param hasClaim
	 *			- boolean - if true, then user can view pdf attachments on
	 *            Attachments tab
	 */
	public void verifyViewAttachmentControls(boolean hasClaim) {
		Log.logStep("Test if the user can " + (hasClaim ? "" : "not ") + "view attachments...");

		if (UiHelper.isPresent(attachmentsTab.getAttachmentTab())) {
			Log.logStep("Attachment Panel found. Clicking on panel...");
			if (hasClaim) {
				UiHelper.clickAndWait(attachmentsTab.getAttachmentTab());
				Log.logVerify("Verifying attachment link is " + (hasClaim ? "" : "not ") + "displayed");
				HardVerify.NotNull(attachmentsTab.getFileNamesFromAttachmentTab(),
						"Attachment link(s) not found for viewing but was expected");
				HardVerify.True(UiHelper.isClickable(attachmentsTab.getAttachmentWebelement()),
						"Attachment link(s) not found for viewing but was expected");
			} else {
				UiHelper.clickAndWait(attachmentsTab.getAttachmentTab());
				Log.logVerify("Verifying attachment link is " + (hasClaim ? "" : "not ") + "displayed");
				HardVerify.Null(attachmentsTab.getFileNamesFromAttachmentTab(),
						"Attachment link(s) found for viewing but was not expected");
			}
		} else {
			throw new SkipException("Attachment Panel not found. Step execution skipped...");
		}
	}
	
	/**
	 * Checks for Attachments tab in Assessment Details Page. If found checks if
	 * user has manage access to attachment controls
	 * 
	 * @param hasClaim
	 *			- boolean - if true, then user can manage attachment pdf on
	 *            Attachments tab
	 */
	public void verifyManageAttachmentControls(boolean hasClaim) {
		Log.logStep("Test if the user can " + (hasClaim ? "" : "not ") + "manage attachments...");

		if (UiHelper.isPresent(attachmentsTab.getAttachmentTab())) {
			Log.logStep("Attachment Panel found. Clicking on panel...");
			if (hasClaim) {
				UiHelper.clickAndWait(attachmentsTab.getAttachmentTab());
				Log.logVerify("Verifying add attachment button is " + (hasClaim ? "" : "not ") + "displayed");
				HardVerify.True(attachmentsTab.isUploadAttachmentsButtonPresent(),
						"Add attachment button not found but was expected");
				Log.logVerify("Verifying delete attachment button is " + (hasClaim ? "" : "not ") + "displayed");
				HardVerify.True(UiHelper.isPresentAndVisible(attachmentsTab.getDeleteAttachmentsButton().get(0)),
						"Delete attachment button not found but was expected");
			} else {
				UiHelper.clickAndWait(attachmentsTab.getAttachmentTab());
				Log.logVerify("Verifying add attachment button is " + (hasClaim ? "" : "not ") + "displayed");
				HardVerify.False(attachmentsTab.isUploadAttachmentsButtonPresent(),
						"Add attachment button found but was not expected");
				Log.logVerify("Verifying delete attachment button is " + (hasClaim ? "" : "not ") + "displayed");
				HardVerify.False(UiHelper.isPresentAndVisible(attachmentsTab.getDeleteAttachmentsButton().get(0)),
						"Delete attachment button found but was not expected");
			}
		} else {
			throw new SkipException("Attachment Panel not found. Step execution skipped...");
		}
	}
	
	/**
	 * Checks for Video Controls in Assessment Details Page. If found checks if
	 * user has access to Video Controls
	 * 
	 * @param hasClaim
	 *            - boolean - if true, then user can manage Video Controls in
	 *            Assessment Details Page
	 */
	public void verifyCanAccessVideoRecording(boolean hasClaim) {
		Log.logStep("Verify if the user has " + (hasClaim ? "" : "not ") + "Access to Video Controls...");
		if (hasClaim) {
			HardVerify.True(UiHelper.isClickable(assesmentDetails.getPlayVideoIcon()),
					"Verifying Video Controls " + (hasClaim ? "" : "not ") + "clickable", "[TEST PASSED]",
					"Video Controls " + (hasClaim ? "not" : "") + " expected. [TEST FAILED]");
		} else {
			HardVerify.False(UiHelper.isClickable(assesmentDetails.getPlayVideoIcon()),
					"Verifying Video Controls " + (hasClaim ? "" : "not ") + "clickable", "[TEST PASSED]",
					"Video Controls " + (hasClaim ? "not" : "") + " expected. [TEST FAILED]");
		}
	}
	
	/**
	 * Checks for Audio Controls in Assessment Details Page. If found checks if
	 * user has access to Audio Controls
	 * 
	 * @param hasClaim
	 *            - boolean - if true, then user can manage Audio Controls in
	 *            Assessment Details Page
	 */
	public void verifyCanAccessAudioRecording(boolean hasClaim) {
		Log.logStep("Verify if the user has " + (hasClaim ? "" : "not ") + "Access to Audio Controls...");
		if (hasClaim) {
			HardVerify.True(assesmentDetails.audioPlayerExists(),
					"Verifying Audio Controls " + (hasClaim ? "" : "not ") + "Present", "[TEST PASSED]",
					"Audio Controls " + (hasClaim ? "not" : "") + " expected. [TEST FAILED]");
		} else {
			HardVerify.False(assesmentDetails.audioPlayerExists(),
					"Verifying Audio Controls " + (hasClaim ? "" : "not ") + "Present", "[TEST PASSED]",
					"Audio Controls " + (hasClaim ? "not" : "") + " expected. [TEST FAILED]");
		}
	}
	
	/**
	 * Checks for Assessment pdf forms in Assessment Details Page. If found
	 * checks if user has access to Assessment pdf forms
	 * 
	 * @param hasClaim
	 *            - boolean - if true, then user can access Assessment pdf forms
	 *            in Assessment Details Page
	 */
	public void verifyCanAccessSource(boolean hasClaim) {
		if (hasClaim) {
			HardVerify.True(UiHelper.isClickable(assesmentDetails.getAssessmentPdfFormElement()),
					"Verifying user can " + (hasClaim ? "" : "not ") + "access source PDF", "[TEST PASSED]",
					"Source PDF can " + (hasClaim ? "not" : "") + " be accessed. [TEST FAILED]");
		} else {
			HardVerify.False(UiHelper.isClickable(assesmentDetails.getAssessmentPdfFormElement()),
					"Verifying user can " + (hasClaim ? "" : "not ") + "access source PDF", "[TEST PASSED]",
					"Source PDF can " + (hasClaim ? "not" : "") + " be accessed. [TEST FAILED]");
		}
	}
	
	/**
	 * Checks for 'Assignment' drop-down. If found
	 * checks it contains 'Me' as a value
	 * 
	 * @param hasClaim
	 *            - boolean - if true, user can assign himself as a rater for the assessment
	 *  
	 */
	public void verifyCanAssignToMe(boolean hasClaim) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		if (hasClaim) {
			HardVerify.True(subjectDetails.getAssignmentStatus(hasClaim, 0),
					"Verifying 'Assignment' drop-down does " + (hasClaim ? "" : "n't") + "contain 'Me' as a value", "[TEST PASSED]",
					"'Assignment' drop-down does" + (hasClaim ? "n't " : "") + " contain 'Me' as a value. [TEST FAILED]");
		} else {
			HardVerify.True(subjectDetails.getAssignmentStatus(hasClaim, 0),
					"Verifying 'Not Assigned' text is" + (hasClaim ? "n't" : "") + " present", "[TEST PASSED]",
					"'Not Assigned' text is" + (hasClaim ? "" : "n't") + " present. [TEST FAILED]");
		}
	}
	
	/**
	 * Checks for 'Assignment' drop-down. If found
	 * checks it contains 'Others' as a value
	 * 
	 * @param hasClaim
	 *            - boolean - if true, user can assign others as a rater for the assessment
	 */
	public void verifyCanAssignToOthers(boolean hasClaim) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		if (hasClaim) {
			HardVerify.True(subjectDetails.getAssignmentStatus(hasClaim, 0),
					"Verifying 'Assignment' drop-down does " + (hasClaim ? "" : "n't") + "contain 'Other Rater name' as a value", "[TEST PASSED]",
					"'Assignment' drop-down does" + (hasClaim ? "n't " : "") + " contain 'Other Rater name' as a value. [TEST FAILED]");
		} else {
			HardVerify.True(subjectDetails.getAssignmentStatus(hasClaim, 0),
					"Verifying 'Not Assigned' text is" + (hasClaim ? "" : "n't") + " present", "[TEST PASSED]",
					"'Not Assigned' text is" + (hasClaim ? "n't " : "") + " present. [TEST FAILED]");
		}
	}
	
	public void verifyCanViewQueries(boolean hasClaim) {
		if (hasClaim) {
			HardVerify.True(UiHelper.isPresentAndVisible(studyDashboard.getQueriesButton()),
					"Verifying 'Queries' link is" + (hasClaim ? "" : "n't") + " present on page.", "[TEST PASSED]",
					"'Queries' link is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		} else {
			HardVerify.False(UiHelper.isPresentAndVisible(studyDashboard.getQueriesButton()),
					"Verifying 'Queries' link is" + (hasClaim ? "" : "n't") + " present on page.", "[TEST PASSED]",
					"'Queries' link is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		}
	}
	
	public void verifyCanManageQueries(boolean hasClaim) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		qSidePanel = dashboard.openQueriesPanel();
		QueryPanelItem item = qSidePanel.getQueriesFromList().get(0);
		item.expand();
		try {
			if (hasClaim) {
				HardVerify.True(qSidePanel.isAddQueryEnabled(),
						"Verifying 'Add Query' button is" + (hasClaim ? "" : "n't") + " present on page.",
						"[TEST PASSED]",
						"'Add Query' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
				HardVerify.True(UiHelper.isClickable(item.getEditButton()),
						"Verifying 'Edit Query' button is" + (hasClaim ? "" : "n't") + " present on page.",
						"[TEST PASSED]", "'Edit Query' button is" + (hasClaim ? "n't " : "")
								+ " present on page. [TEST FAILED]");
				HardVerify.True(UiHelper.isClickable(item.getDeleteButton()),
						"Verifying 'Delete Query' button is" + (hasClaim ? "" : "n't") + " present on query.",
						"[TEST PASSED]", "'Delete Query' button is" + (hasClaim ? "n't " : "")
								+ " present on query. [TEST FAILED]");
			} else {
				HardVerify.False(qSidePanel.isAddQueryEnabled(),
						"Verifying 'Add Query' button is" + (hasClaim ? "" : "n't") + " present on page.",
						"[TEST PASSED]",
						"'Add Query' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
				HardVerify.False(UiHelper.isClickable(item.getEditButton()),
						"Verifying 'Edit Query' button is" + (hasClaim ? "" : "n't") + " present on page.",
						"[TEST PASSED]", "'Edit Query' button is" + (hasClaim ? "n't " : "")
								+ " present on page. [TEST FAILED]");
				HardVerify.False(UiHelper.isClickable(item.getDeleteButton()),
						"Verifying 'Delete Query' button is" + (hasClaim ? "" : "n't") + " present on query.",
						"[TEST PASSED]", "'Delete Query' button is" + (hasClaim ? "n't " : "")
								+ " present on query. [TEST FAILED]");
			}
		} finally {
			qSidePanel.clickClose();
		}
	}
	
	public void expandQualifiedQuery(String formName) {
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		qSidePanel = dashboard.openQueriesPanel();
		ArrayList<QueryPanelItem> queryItems = qSidePanel.getQueriesFromList();

		for (int i = 0; i < queryItems.size(); i++) {
			if (queryItems.get(i).getQueryAssessmentName().equalsIgnoreCase(formName)) {
				Log.logInfo("Query with Assessment: " + formName + " found. Expanding it...");
				qPanelItem = qSidePanel.getQueriesFromList().get(i);
				qPanelItem.expand();
				break;
			}
		}
	}
	
	public void verifyCanReplyToQueriesIfQualified(boolean hasClaim, boolean hasFormQualification,
			String formName) {
		try {
			if (hasClaim) {
				if (hasFormQualification) {
					Log.logInfo(
							"User is" + (hasFormQualification ? "" : "n't") + " qualified for Assessment: " + formName);
					HardVerify.True(qPanelItem.isReplyTextBoxVisible(),
							"Verifying 'Reply' text box is" + (hasFormQualification ? "" : "n't")
									+ " present on query details.",
							"[TEST PASSED]", "'Reply' text box is" + (hasFormQualification ? "n't " : "")
									+ " present on query details. [TEST FAILED]");
					HardVerify.True(qPanelItem.isReplyButtonVisible(),
							"Verifying 'Reply' button is" + (hasFormQualification ? "" : "n't")
									+ " present on query details.",
							"[TEST PASSED]", "'Reply' button is" + (hasFormQualification ? "n't " : "")
									+ " present on query details. [TEST FAILED]");
				} else {
					Log.logInfo(
							"User is" + (hasFormQualification ? "" : "n't") + " qualified for Assessment: " + formName);
					HardVerify.False(qPanelItem.isReplyTextBoxVisible(),
							"Verifying 'Reply' text box is" + (hasFormQualification ? "" : "n't")
									+ " present on query details.",
							"[TEST PASSED]", "'Reply' text box is" + (hasFormQualification ? "n't " : "")
									+ " present on query details. [TEST FAILED]");
					HardVerify.False(qPanelItem.isReplyButtonVisible(),
							"Verifying 'Reply' button is" + (hasFormQualification ? "" : "n't")
									+ " present on query details.",
							"[TEST PASSED]", "'Reply' button is" + (hasFormQualification ? "n't " : "")
									+ " present on query details. [TEST FAILED]");
				}
			} else {
				HardVerify.False(qPanelItem.isReplyTextBoxVisible(),
						"Verifying 'Reply' text box is" + (hasClaim ? "" : "n't")
								+ " present on query details.",
						"[TEST PASSED]", "'Reply' text box is" + (hasClaim ? "n't " : "")
								+ " present on query details. [TEST FAILED]");
				HardVerify.False(qPanelItem.isReplyButtonVisible(),
						"Verifying 'Reply' button is" + (hasClaim ? "" : "n't") + " present on query details.",
						"[TEST PASSED]", "'Reply' button is" + (hasClaim ? "n't " : "")
								+ " present on query details. [TEST FAILED]");
			}
		} finally {
			qPanelItem.collapse();
			qSidePanel.clickClose();
		}
	}
	
	public void verifyAddSubjectIsPresent(boolean hasClaim) {
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		if (hasClaim) {
			HardVerify.NotNull(subjectList.getAddButton(),
					"Verifying 'Add Subject' button is" + (hasClaim ? "" : "n't") + " present on page.",
					"[TEST PASSED]",
					"'Add Subject' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		} else {
			HardVerify.Null(subjectList.getAddButton(),
					"Verifying 'Add Subject' button is" + (hasClaim ? "" : "n't") + " present on page.",
					"[TEST PASSED]",
					"'Add Subject' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		}
	}
	
	public void verifyAddUnscheduledVisitIsPresent(boolean hasClaim) {
		visitsInSubject = PageFactory.initElements(Browser.getDriver(), VisitsInSubject.class);
		if (hasClaim) {
			HardVerify.NotNull(visitsInSubject.getAddUnscheduledVisitButton(),
					"Verifying 'Add Unscheduled Visit' button is" + (hasClaim ? "" : "n't") + " present on page.",
					"[TEST PASSED]",
					"'Add Unscheduled Visit' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		} else {
			HardVerify.Null(visitsInSubject.getAddUnscheduledVisitButton(),
					"Verifying 'Add Unscheduled Visit' button is" + (hasClaim ? "" : "n't") + " present on page.",
					"[TEST PASSED]",
					"'Add Unscheduled Visit' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		}
	}
	
	public void verifyRemoveVisitIsPresent(boolean hasClaim) {
		visitsInSubject = PageFactory.initElements(Browser.getDriver(), VisitsInSubject.class);
		if (hasClaim) {
			HardVerify.NotNull(visitsInSubject.getRemoveVisitButton(),
					"Verifying 'Remove Visit' button is" + (hasClaim ? "" : "n't") + " present on page.",
					"[TEST PASSED]",
					"'Remove Visit' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		} else {
			HardVerify.Null(visitsInSubject.getRemoveVisitButton(),
					"Verifying 'Remove Visit' button is" + (hasClaim ? "" : "n't") + " present on page.",
					"[TEST PASSED]",
					"'Remove Visit' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		}
	}
	
	public void verifyAddVisitIsPresent(boolean hasClaim) {
		visitsInSubject = PageFactory.initElements(Browser.getDriver(), VisitsInSubject.class);
		if (hasClaim) {
			HardVerify.NotNull(visitsInSubject.getAddVisitButton(),
					"Verifying 'Add Visit' button is" + (hasClaim ? "" : "n't") + " present on page.", "[TEST PASSED]",
					"'Add Visit' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		} else {
			HardVerify.Null(visitsInSubject.getAddVisitButton(),
					"Verifying 'Add Visit' button is" + (hasClaim ? "" : "n't") + " present on page.", "[TEST PASSED]",
					"'Add Visit' button is" + (hasClaim ? "n't " : "") + " present on page. [TEST FAILED]");
		}
	}

	public void verifyDisableProObsroCheckBoxIsActive(boolean hasClaim) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		if (hasClaim) {
			subjectDetails.subjectToEdit();
			HardVerify.True(subjectDetails.checkUncheckDisablePROchkbox(),
					"Verifying 'DisablePRO' check-box is" + (hasClaim ? "" : "n't") + " clickable.", "[TEST PASSED]",
					"'DisablePRO' check-box is" + (hasClaim ? "n't " : "") + " clickable. [TEST FAILED]");
			HardVerify.True(subjectDetails.checkUncheckDisableObsROchkbox(),
					"Verifying 'DisableObsRO' check-box is" + (hasClaim ? "" : "n't") + " clickable.", "[TEST PASSED]",
					"'DisableObsRO' check-box is" + (hasClaim ? "n't " : "") + " clickable. [TEST FAILED]");
		} else {
			if (null != subjectDetails.getSubjectEditButton()) {
				subjectDetails.subjectToEdit();
			}
			HardVerify.False(subjectDetails.checkUncheckDisablePROchkbox(),
					"Verifying 'DisablePRO' check-box is" + (hasClaim ? "" : "n't") + " clickable.", "[TEST PASSED]",
					"'DisablePRO' check-box is" + (hasClaim ? "n't " : "") + " clickable. [TEST FAILED]");
			HardVerify.False(subjectDetails.checkUncheckDisableObsROchkbox(),
					"Verifying 'DisableObsRO' check-box is" + (hasClaim ? "" : "n't") + " clickable.", "[TEST PASSED]",
					"'DisableObsRO' check-box is" + (hasClaim ? "n't " : "") + " clickable. [TEST FAILED]");
		}
	}
}
