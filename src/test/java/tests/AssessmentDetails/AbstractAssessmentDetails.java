package tests.AssessmentDetails;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.EsignReasonFields;
import mt.siteportal.utils.data.TestParameters;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.AssessmentDetailsSteps;
import steps.Tests.AssessmentSteps;
import steps.Tests.BreadcrumbsNavigationSteps;
import steps.Tests.EsignSteps;
import steps.Tests.StudyDashboardListSteps;
import steps.Tests.StudyDashboardSteps;
import steps.Tests.SubjectSteps;
import steps.Tests.TemplateSteps;
import tests.Abstract.AbstractTest;

/**
 * Super Class for all Assessment Details tests Contains some useful functions
 * and some configuration data
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = "AssessmentDetails")
public abstract class AbstractAssessmentDetails extends AbstractTest {
	
	protected ToolBarFull toolBarFull;
	
	/*
	 * URLs
	 */
	protected final String studyDashboardUrl = URLsHolder.getHolder().getSiteportalURL() + "/"
			+ URLsHolder.getHolder().getStudyDashboardRouteURL();
	protected final String loginPageURL = URLsHolder.getHolder().getSiteportalURL();
	
	/*
	 * STUDY and SITE used throughout the TESTS
	 */
	/*
	protected final String studyName = TestParameters.get().getStringParameter("Study Dashboard",
			"Study Name");
	protected final String siteName = TestParameters.get().getStringParameter("Study Dashboard",
			"Site Name");
	*/
	
//	protected String studyName = "AA Pharmaceutical - AA Study 2";
	protected String studyName = "Auto - Assessment";
	protected String siteName = "All Sites";
	protected String subjectName = "SUB#01";
//	protected String dropDownvalue = EsignReasonFields.TECHNICAL.getValue();
	protected String dropDownvalue = "Technical difficulties";
	
//	protected String visitName = "Visit5";
		
	/*
	 * Steps Classes
	 */
	protected AssessmentDetailsSteps detailsSteps = new AssessmentDetailsSteps();
	protected StudyDashboardListSteps dashboardListSteps = new StudyDashboardListSteps();
	protected StudyDashboardSteps studyDashboardSteps = new StudyDashboardSteps();
	protected CommonSteps commonSteps = new CommonSteps();
	protected SubjectSteps subjectStep = new SubjectSteps();
	protected AssessmentSteps steps = new AssessmentSteps();
	protected EsignSteps esignSteps = new EsignSteps();
	protected TemplateSteps templateStep = new TemplateSteps();
			

	/*
	 * User Credentials
	 */
	/*protected final String username = CredentialsHolder.get().usernameForUserType("Site Rater - Type 5");
	protected final String password = CredentialsHolder.get().passwordForUserType("Site Rater - Type 5");*/

	/**
	 * Before All Test Classes, log which Test is being executed, open the browser and login
	 * 
	 * @param testContext ITestContext which contains the name of the Test from the testng.xml file
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
		Nav.toURL(loginPageURL);
		
//		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
	}
	
	@AfterTest(alwaysRun = true)
	public void afterTest(ITestContext testContext) {
//		afterSteps.logout();
		if (newBrowser != null)
			newBrowser.quit();
		Log.logTestEnd(testContext);
	}

	/**
	 * Before executing any of the methods in a class log the class name 
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		
	}

	/**
	 * After executing any of the methods in a class log the class name
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		commonSteps.getToStudyDashboard();
		afterSteps.logoutByClearingCookie();
		Log.logTestClassEnd(this.getClass());
	}
}
