package tests.PaperTranscription;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.objects.UploadFilesPopUp;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.EsignReasonFields;
import mt.siteportal.utils.data.TestParameters;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.AssessmentDetailsSteps;
import steps.Tests.EsignSteps;
import steps.Tests.ObserverSteps;
import steps.Tests.PaperTranscriptionSteps;
import steps.Tests.StudyDashboardListSteps;
import steps.Tests.StudyDashboardSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

/**
 * Abstract Super Class for the Paper Transcription Tests Contains functions and
 * variabled that are common for every Paper Transcription Test
 * 
 * @author Syed A. Zawad
 *
 */
@Test(groups = { "PaperTranscription" })
public abstract class AbstractPaperTranscription extends AbstractTest {

	/*
	 * Configurations
	 */
	protected static final String studyName = "Auto - PaperTranscription";
	protected static final String siteName = "1000 - Ethan Jacob";
	protected static final String loginPageURL = URLsHolder.getHolder().getSiteportalURL();;
	protected String username = siteportalUserAccountName;
	protected String password = siteportalUserAccountPassword;
	protected String formType;
	
	/*
	 * Steps Classes
	 */
	protected PaperTranscriptionSteps pptSteps = new PaperTranscriptionSteps();
	protected StudyDashboardSteps dashboardSteps = new StudyDashboardSteps();
	protected StudyDashboardListSteps listSteps = new StudyDashboardListSteps();
	protected EsignSteps esignSteps = new EsignSteps();
	
	protected AssessmentDetailsSteps assessmentDetailsSteps = new AssessmentDetailsSteps();
	protected ObserverSteps obsSteps = new ObserverSteps();
	protected SubjectSteps sbjSteps = new SubjectSteps();
	
	/*
	 * Page Objects used
	 */
	protected SubjectDetails subjectDetails;
	protected AssessmentDetails assessmentDetails;
	protected UploadFilesPopUp uploadFilesPopUp;
	protected ToolBarFull toolBarFull;
	
	/*
	 * Criteria used to select the correct Assessment required for the
	 * test
	 */
	protected Map<String, String> criteria = new HashMap<String, String>();
	
	/*
	 * The Version Number count. Editing the forms should not affect the version
	 * count in any way
	 */
	protected int numberOfVersions;

	protected static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	protected static final String formattedDate = dateFormat.format(new Date());
	
	protected static final String[] splitDate = (formattedDate.split("-"));
	protected static final String startedMonth = new DateFormatSymbols().getShortMonths()[Integer.parseInt(splitDate[0]) - 1].toUpperCase();
	protected static final String startedDate = splitDate[1];
	protected static final String startedYear = splitDate[2];
	
	protected static final DateFormat timeFormat = new SimpleDateFormat("h-mm-a");
	protected static final String formattedTime = timeFormat.format(new Date());
	protected static final String[] splitTime = (formattedTime.split("-"));
	protected static final String startedHr = splitTime[0];
	protected static final String startedMin = splitTime[1];
	protected static final String startedMeridiem = splitTime[2];
	
	/*
	 * Random subjectName generation
	 */
	protected static final DateFormat subjectFormat = new SimpleDateFormat("MMddyy");
	protected static final String formattedSubject = subjectFormat.format(new Date());
	protected static final String suffix = "-" + CommonSteps.generateRandomNames(2);
	protected static final String subjectName = "PPTest#" + formattedSubject + suffix;
	/*
	* Attributes used in the Test Case Class
	*/
	protected static final String visitName = "Visit 1";
	protected static final String obsRelation = "Wife";
	protected static final String obsAlias = "ObsWife";
	protected static final String fileName = "ECogPro_en-us_v1.0.pdf";
	protected static final String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\";
	
	/**
	 * Data provider. Contains the criteria used to select a particular
	 * assessment. The data is kept as HashMap<String, String> where the key is
	 * the name of the criteria and value is the value of that is expected for a
	 * selected assessment. Example - Key - Status, Value - Complete will select
	 * an assessment which has its Status column set to Complete
	 * 
	 * @return HashMap<String, String>
	 */
	protected Map<String, String> setAssessmentSelectionCriteria() {
		criteria.put("Status", "Pending");
		criteria.put("Type", formType);
		criteria.put("Subject", subjectName);
		return criteria;
	}
	
	@DataProvider(name = "subjectForms")
	public static Object[][] subjectForms() {
		return new Object[][] { { "ClinRO" }, { "ObsRO" }, { "PRO" } };
	}
		
	/**
	 * Before all the Tests, login as Site Rater to the SitePortal
	 * 
	 * @param testContext
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		/*dbSteps.deleteObserver(subjectName, obsRelation, obsAlias);
		dbSteps.deleteSubjectVisit(subjectName, visitName);
		dbSteps.deleteSubject(subjectName);*/
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
		Nav.toURL(loginPageURL);
		UiHelper.focusBrowserWindow();
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName,
				siteName);
		beforeSteps.openAllSubjects();
		sbjSteps.addSubjectToSite(subjectName, siteName);
		obsSteps.addObserver(obsRelation, obsAlias);
		sbjSteps.scheduleVisit();
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		toolBarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);

		String[] formsItr = { "ClinRO", "ObsRO", "PRO" };
		for (String form : formsItr) {
			assessmentDetails = subjectDetails.clickEnterPaperTranscriptionLinkFor(form);
			pptSteps.saveChangesWithEsign(username, password);
			if (form.equalsIgnoreCase("ClinRO"))
				assessmentDetails.chooseRandomRater();
			assessmentDetails.setStartedDate(startedDate, startedMonth, startedYear);
			assessmentDetails.setStartedTime(startedHr, startedMin, startedMeridiem);
			assessmentDetails.setDuration("01", "00");
			assessmentDetailsSteps.uploadSourceDocument(filePath, fileName);
			assessmentDetailsSteps.clickSaveButton();
			toolBarFull.returnToSubject(subjectName);
		}
	}
	
	/**
	 * After all test classes finished, log the name of the TestNG Test that
	 * has been completed
	 */
	@AfterTest(alwaysRun = true)
	public void afterTest(ITestContext testContext) {
		afterSteps.logout();
		/*dbSteps.deleteObserver(subjectName, obsRelation, obsAlias);
		dbSteps.deleteSubjectVisit(subjectName, visitName);
		dbSteps.deleteSubject(subjectName);*/
		if (newBrowser != null)
			newBrowser.quit();
		Log.logTestEnd(testContext);
	}
}
