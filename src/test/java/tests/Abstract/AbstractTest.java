package tests.Abstract;


import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import hu.siteportal.pdfreport.PdfLog;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.DataHolder;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.TestListener;
import steps.Configuration.AfterSteps;
import steps.Configuration.BeforeSteps;
import steps.Configuration.CommonSteps;
import steps.Configuration.DBSteps;

/**
 * Base test class containing common pre-/post-conditions steps and auxiliary methods.<br/>
 *
 * TODO: use listeners to track test failures like IOException. <br/>
 * TODO: log additional info like IP etc before test start.     <br/>
 */
@Listeners (TestListener.class)
public abstract class AbstractTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);
	protected Browser newBrowser;
	/*
	MT USERS
	 */
	protected static String adminName = DataHolder.getHolder().getAdminName();
	protected static String adminLogin = DataHolder.getHolder().getAdminAccountName();
	protected static String adminPasword = DataHolder.getHolder().getAdminAccountPassword();
	
	protected static String userLogin = DataHolder.getHolder().getUserName();
	protected static String userPassword = DataHolder.getHolder().getUserPassword();
	/*
	 * MAPORTAL USERS
	 */
	protected static String maportalUserName = DataHolder.getHolder().getMaportalUserName();
	protected static String maportalUserAccountName = DataHolder.getHolder().getMaportalUserAccountName();
	protected static String maportalUserAccountPassword = DataHolder.getHolder().getMaportalUserAccountPassword();
	protected static final String maPortalUrl = URLsHolder.getHolder().getMaportalURL();
	
	/*
	 * SITEPORTAL USERS 
	 */
	protected static String siteportalUserName = DataHolder.getHolder().getSiteportalUserName();
	protected static String siteportalUserAccountName = DataHolder.getHolder().getSiteportalUserAccountName();
	protected static String siteportalUserAccountPassword = DataHolder.getHolder().getSiteportalUserAccountPassword();
	protected static final String sitePortalUrl = URLsHolder.getHolder().getSiteportalURL();
	
	/*
	 * Required common steps initialization 
	 */
	protected BeforeSteps beforeSteps = new BeforeSteps();
	protected AfterSteps afterSteps = new AfterSteps();
	protected CommonSteps commonSteps = new CommonSteps();
	protected DBSteps dbSteps = new DBSteps();

	/**
	 * Performs precondition action(s) before each test start.
	 * <p>
	 * @throws MalformedURLException 
	 */
	
	/**
	 * Before all the TestNG test run: 
	 * 1. log the name of the TestNG test suite
	 * 2. log the environment name for test execution
	 */
	@BeforeSuite(alwaysRun = true)
	@Parameters({ "envTarget" })
	public void beforeSuite(String envTarget, ITestContext testContext) {
		Log.logTestSuiteStart(testContext);
		Log.logSuiteInfo("Starting test run on: [" + envTarget.toUpperCase() + "] environment");
	}
	
	/**
	 * After all the TestNG test run: 
	 * 1. log the name of the TestNG test suite
	 */
	@AfterSuite(alwaysRun = true)
	@Parameters({ "envTarget" })
	public void afterSuite(String envTarget, ITestContext testContext) {
		Log.logSuiteInfo("Completed test run  on: [" + envTarget.toUpperCase() + "] environment");
		Log.logTestSuiteEnd(testContext);
	}
	
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
	public synchronized void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		newBrowser = new Browser(browser);
		Log.logTestInfo("Opening " + browser);
		Log.logTestInfo("Navigating to Login page...");
		Nav.toURL(sitePortalUrl);
	}

	/**
	 * After all the Tests Classes have finished executing, log it and close browser
	 * 
	 * @param testContext ITestContext which contains the name of the Test from the testng.xml file
	 */
	@AfterTest(alwaysRun = true)
	public void afterTest(ITestContext testContext) {
		// Close the browser
		if (newBrowser != null)
			newBrowser.quit();
		Log.logTestEnd(testContext);
	}
	
	/**
	 * Before every test, log the method that is being run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public synchronized void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		
	}
	
	/**
	 * After every test, Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public synchronized void afterMethod(Method method, ITestResult result) {
		Log.logTestMethodEnd(method, result);
//		PdfLog.clearActionResultStatusList();
	}
}