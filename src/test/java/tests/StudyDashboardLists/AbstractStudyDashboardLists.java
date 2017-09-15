package tests.StudyDashboardLists;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.CredentialsHolder;
import mt.siteportal.utils.data.TestParameters;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Tests.StudyDashboardListSteps;
import steps.Tests.StudyDashboardSteps;
import tests.Abstract.AbstractTest;

/**
 * The Abstract Test Class used as the parent of every Test Class for the Assessment, Visit and Subject List Pages.
 * 
 * @author Syed A. Zawad
 *
 */
@Test(groups = { "StudyDashboardLists" })
public abstract class AbstractStudyDashboardLists extends AbstractTest {
	/*
	 * STUDY and SITE used throughout the TESTS
	 */
	protected static String studyName;
	protected static String siteName;
//	protected final String studyName = "AA Pharmaceutical - AA Study 2";
//	protected String siteName = "All Sites";

	/*
	 * Steps Classes
	 */
	protected StudyDashboardListSteps listSteps = new StudyDashboardListSteps();
	protected StudyDashboardSteps dashboardSteps = new StudyDashboardSteps();
	
	/*
	 * User Credentials
	 */
//	protected final String username = CredentialsHolder.get().usernameForUserType("Site Rater - Type 5");
//	protected final String password = CredentialsHolder.get().passwordForUserType("Site Rater - Type 5");

	/*
	 * Some Test Parameters
	 */
	protected List<String> expectedSites;
	
	/**
	 * Before all the tests classes, open the browser and login
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
		Nav.toURL(sitePortalUrl);
		
//		beforeSteps.loginAs(siteportalUserAccountName, siteportalUserAccountPassword);
		beforeSteps.loginAs(siteportalUserAccountName, siteportalUserAccountPassword);
		commonSteps.getToStudyDashboard();
		studyName = dashboardSteps.selectStudyWithMaxFilterValuesForAllCategories();
		if (null == studyName) {
			throw new SkipException("No study found with Maximum Filter values for all Categories. Skipping tests...");
		}
		expectedSites = listSteps.storeSitesAvailable(studyName);
		siteName = expectedSites.get(0);
	}
	
	@AfterTest(alwaysRun = true)
	public void afterTest(ITestContext testContext) {
		afterSteps.logout();
		if (newBrowser != null)
			newBrowser.quit();
		Log.logTestEnd(testContext);
	}
	
	/**
	 * Log that the Test Class is beginning
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		listSteps.getToStudyDashboard();
		listSteps.chooseStudy(studyName);
		expectedSites = listSteps.storeSitesAvailable();
	}
	
	/**
	 * Log after every class
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
	}
	
	/**
	 * Before every method log the name of the method
	 * 
	 * @param method
	 *            - Method - the method that will start
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
	}
	
	/**
	 * After Every test method, log the method name that ended
	 * 
	 * @param method
	 *            - Method that ended
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		Log.logTestMethodEnd(method, result);
	}
}
