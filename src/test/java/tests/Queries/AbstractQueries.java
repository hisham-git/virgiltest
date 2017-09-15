package tests.Queries;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.LoginPage;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import nz.siteportal.pages.studydashboard.ListPages.SubjectList;
import nz.siteportal.pages.studydashboard.ListPages.VisitList;
import steps.Configuration.CommonSteps;
import steps.Tests.QueriesSteps;
import steps.Tests.StudyDashboardSteps;
import tests.Abstract.AbstractTest;

/**
 * Abstract Class - QueriesAbstractTest The super class of every Queries Test
 * Class. This is used simply to remove the need to create @Before.....
 * and @After methods for every class and provides generic methods that will be
 * used by all the Query Tests
 * 
 * Status : COMPLETE
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 */
@Test(groups = { "Queries", "Multi-Environment" })
public abstract class AbstractQueries extends AbstractTest {
	
	/**
	 * Temp variables for test
	 * 
	 */
	protected Dashboard dashboard;
	protected LoginPage loginPage;
	protected HomePage homePage;
	protected ToolBarFull toolbarFull;

	/*
	 * URLs
	 */
	protected final String studyDashboardUrl = URLsHolder.getHolder().getMaportalURL() + "/"
			+ URLsHolder.getHolder().getStudyDashboardRouteURL();
//	protected final String loginPageURL = URLsHolder.getHolder().getMaportalURL();
	protected final String loginPageURL = URLsHolder.getHolder().getSiteportalURL();

	/*
	 * The Panel PageObject that is used for every Query test
	 */
	protected QueriesSidePanel qSidePanel;

	/*
	 * Other PageObjects used by the child test classes
	 */
	protected SubjectList subjectList;
	protected SubjectDetails subjectDetails;
	protected VisitList visitList;
	protected AssessmentList assessmentList;
	protected DashboardList listPage;

	/*
	 * The Study and Site names used in every Query test
	 */
	/*protected final String studyName = "BD Pharmaceuticals - BD Study 1";
	protected final String siteName = "Site1 - Ethan Jacob";*/
	protected static String studyName;
	protected static String siteName;
	
	/**
	 * Min & Max query to find
	 */
	protected static int minQuery = 15;
	protected static int maxQuery = 35;
	
	/**
	 * Step definitions
	 */
	protected CommonSteps commonSteps = new CommonSteps();
	protected QueriesSteps queriesSteps = new QueriesSteps();
	protected StudyDashboardSteps studyDashboardSteps;

	/**
	 * NOTE : The user must have canManageQueries(MA User Type-2) claim Before all the test
	 * classes -> 1. Login with given credentials 2. If the user is not logged
	 * in, skip all tests
	 */
	
	/**
	 * Before all the test classes run: 
	 * 1. log the name of the TestNG test
	 * 2. Open new browser window
	 * 3. Navigate to AUT URL
	 * 
	 * @param browser - optional
	 * @param testContext
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
		Log.logInfo("Navigating to Login page...");
		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.getToStudyDashboard();
		studyDashboardSteps = new StudyDashboardSteps();
		studyName = studyDashboardSteps.selectStudyWithQueryType("All", true);
		if (null != studyName) {
			beforeSteps.chooseStudy(studyName);
		} else {
			throw new SkipException("No study found with queries. Skipping tests...");
		}
		siteName = studyDashboardSteps.selectSiteWithMaxQueries();
//		afterSteps.logout();
		afterSteps.logoutByClearingCookie();
	}

	/**
	 * Log that the Test Class is beginning
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		commonSteps = new CommonSteps();
		queriesSteps = new QueriesSteps();
		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(adminLogin, adminPasword);
		if (commonSteps.getToStudyDashboard()) {
			beforeSteps.chooseStudy(studyName);
			beforeSteps.chooseSite(siteName);
		} else {
			throw new SkipException("Couldn't open Study Dashboard. Skipping tests...");
		}
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}

	/**
	 * Before every test method: 
	 * 1. Open Query Panel
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		qSidePanel = dashboard.openQueriesPanel();
	}
	
	/**
	 * After every test method: 
	 * 1. Close Query Panel
	 * 2. Return to study dashboard by clicking on first link from breadcrumbs
	 * 3. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		qSidePanel.clickClose();
		commonSteps.returnToDashboard();
		Log.logTestMethodEnd(method, result);
	}

	/**
	 * After all test methods 
	 * 1. Logout by clearing all browser cookies
	 * 2. Log the finished Test Class name
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
//		afterSteps.logout();
		afterSteps.logoutByClearingCookie();
		Log.logTestClassEnd(this.getClass());
	}
}
