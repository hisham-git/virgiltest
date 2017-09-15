package tests.RolesAndClaims;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.CredentialsHolder;
import mt.siteportal.utils.data.TestParameters;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.ListPages.VisitList;
import steps.Tests.AssessmentDetailsSteps;
import steps.Tests.StudyDashboardListSteps;
import tests.Abstract.AbstractTest;

/**
 * Super Class for all Assessment Details tests Contains some useful functions
 * and some configuration data
 * 
 * @author Abdullah Al Hisham
 *
 */
public abstract class AbstractRolesAndClaimsTest extends AbstractTest {
	
	/*
	 * URLs
	 */
	protected final String studyDashboardUrl = URLsHolder.getHolder().getSiteportalURL() + "/"
			+ URLsHolder.getHolder().getStudyDashboardRouteURL();
	protected final String loginPageURL = URLsHolder.getHolder().getSiteportalURL();
	
	/*
	 * STUDY and SITE used throughout the TESTS
	 */
	protected final String studyName = TestParameters.get().getStringParameter("Study Dashboard",
			"Study Name");
	protected final String siteName = TestParameters.get().getStringParameter("Study Dashboard",
			"Site Name");
		
	/*
	 * Steps Classes
	 */
	protected AssessmentDetailsSteps detailsSteps = new AssessmentDetailsSteps();
	protected StudyDashboardListSteps studyDashboardSteps = new StudyDashboardListSteps();
	protected VisitList visitList = new VisitList(Browser.getDriver());
	
	/*
	 * User Credentials
	 */
//	protected final String username = CredentialsHolder.get().usernameForUserType("Site Rater - Type 3");
//	protected final String password = CredentialsHolder.get().passwordForUserType("Site Rater - Type 3");
	
	/**
	 * Before All Test Classes, log which Test is being executed, open the browser and login
	 * 
	 * @param testContext ITestContext which contains the name of the Test from the testng.xml file
	 */

	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext testContext) {
		Log.logInfo("[Beginning New Test] => " + testContext.getName());
		Browser.getCurrentUrl();
		Nav.toURL(loginPageURL);
//		beforeSteps.loginAs(username, password);
//		beforeSteps.loginAndChooseStudySite(username, password, studyName, siteName);
	}

	/**
	 * Before executing any of the methods in a class, check that the user is logged in. If not logged in, then log in.
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
	}

	/**
	 * After all the Tests in a class have finished, log it
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
	}

	/**
	 * After all the Tests Classes have finished executing, log it and close browser
	 * 
	 * @param testContext ITestContext which contains the name of the Test from the testng.xml file
	 */
	@AfterTest(alwaysRun = true)
	public void afterTest(ITestContext testContext) {
		Log.logInfo("[Ending Test] => " + testContext.getName());
//		tearDownTest();
	}

}
