package tests.RolesAndClaims;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.utils.dataProvider.ExcelDataMapper;
import steps.Tests.RolesAndClaimsSteps;

/**
 * Test Class for Roles and Claims. Mainly checks for the correct accessibility
 * to the various portals for the different user types
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
public class AccessClaimsTest extends AbstractRolesAndClaimsTest {

	/*
	 * The user's credentials
	 */
	private Map<String, String> userData;
	private String userType, userFullName, userName, userPassword;
	private boolean hasClaim;	

	/*
	 * The Steps Class as specified by the MedAvante Test Creation Guide
	 */
	private RolesAndClaimsSteps rolesAndClaimsSteps;

	/**
	 * Sets the User Type and credentials that will be required for a particular instance of the test
	 * 
	 * @param userType - String - the User Role, E.g. "MedAvante User - Type 3"
	 */
	@Factory(dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public AccessClaimsTest(Map<String, String> userData) {
		this.userType = userData.get("UserType");
		this.userFullName = userData.get("UserFullName");
		this.userName = userData.get("UserName");
		this.userPassword = userData.get("Pass");
		this.userData = userData;
	}

	/**
	 * Before all methods, log
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Log.logInfo(String.format("Starting with [%s : %s]", userType, userFullName));
		rolesAndClaimsSteps = new RolesAndClaimsSteps(Browser.getDriver());
	}

	/**
	 * Log before every method
	 * 
	 * @param method - Method - the method that will execute next
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		try {
			hasClaim = Boolean.parseBoolean( userData.get(method.getName()) );
			Log.logInfo(String.format("[%s : %s] has access to [%s] claim found [%b]", userType, userFullName,
					method.getName(), hasClaim));
		} catch (Exception e) {
			throw new SkipException("Couldn't parse claim values from 'ExcelDataMapper' matrix. Skipping tests...");
		}
	}

	/**
	 * Test Case - Can Access Site Portal
	 * Objective - To check if the given user has the correct access to Siteportal
	 * Steps - 
	 * 	1. Clear all Browser cookies and navigate to the Siteportal's Login Page
	 *  2. Login using the given user's credentials
	 *  3. Check if the user is logged in or not based on the expected
	 *     outcome from the Roles and Claims Matrix Excel
	 * 
	 */
	@Test(enabled = true, priority=1, groups = { "Roles And Claims", "Portal Accessibility", "JamaNA" }, description = "Can Access Site Portal")
	public void canAccessSitePortal() {
		rolesAndClaimsSteps.goToPortalLoginPage(userData.get("SitePortal"));
		rolesAndClaimsSteps.loginWithCredentials(userName, userPassword);
		rolesAndClaimsSteps.verifyPortalDashboardIsOpened(userType, hasClaim);
	}
	
	/**
	 * Test Case - Can Access MedAvante Portal
	 * Objective - To check if the given user has the correct access to MedAvantePortal
	 * Steps - 
	 * 	1. Clear all Browser cookies and navigate to the Maportal's Login Page
	 *  2. Login using the given user's credentials
	 *  3. Check if the user is logged in or not based on the expected
	 *     outcome from the Roles and Claims Matrix Excel
	 * 
	 */
	@Test(enabled = true, priority=2, groups = { "Roles And Claims", "Portal Accessibility", "JamaNA" }, description = "Can Access MedAvante Portal")
	public void canAccessMedAvantePortal() {
		rolesAndClaimsSteps.goToPortalLoginPage(userData.get("MaPortal"));
		rolesAndClaimsSteps.loginWithCredentials(userName, userPassword);
		rolesAndClaimsSteps.verifyPortalDashboardIsOpened(userType, hasClaim);
	}

	/**
	 * Test Case - Can Access Sponsor Portal
	 * Objective - To check if the given user has the correct access to SponsorPortal
	 * Steps - 
	 * 	1. Clear all Browser cookies and navigate to the sponsorportal's Login Page
	 *  2. Login using the given user's credentials
	 *  3. Check if the user is logged in or not based on the expected
	 *     outcome from the Roles and Claims Matrix Excel
	 * 
	 */
	@Test(enabled = false, groups = { "Roles And Claims", "Portal Accessibility", "JamaNA" }, description = "Can Access Sponsor Portal")
	public void canAccessSponsorPortal() {	
		rolesAndClaimsSteps.goToPortalLoginPage(userData.get("SponsorPortal"));
		rolesAndClaimsSteps.loginWithCredentials(userName, userPassword);
		rolesAndClaimsSteps.verifyPortalDashboardIsOpened(userType, hasClaim);
	}

	/**
	 * Test Case - Can Access Admin App
	 * Objective - To check if the given user has the correct access to MedAvante Portal's Admin App
	 * Steps - 
	 * 	1. Clear all Browser cookies and navigate to the Maportal's Login Page
	 *  2. Login using the given user's credentials
	 *  3. Navigate to the Administration Dashboard
	 *  4. Check if the Administration Dashboard has opened/not opened based on the Roles And Claims Matrix
	 * 
	 */
	@Test(enabled = true, priority=3, groups = { "Roles And Claims", "Portal Accessibility", "JamaNA" }, description = "Can Access Admin App")
	public void canAccessAdminApp() {
		rolesAndClaimsSteps.goToPortalLoginPage(userData.get("MaPortal"));
		rolesAndClaimsSteps.loginWithCredentials(userName, userPassword);
//		rolesAndClaimsSteps.gotToAdminDashboard();
		rolesAndClaimsSteps.verifyAdminDashboardIsOpened(userType, hasClaim);
	}

	/**
	 * After Every Test Case, log the method that has ended
	 * 
	 * @param method - Method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		Log.logTestMethodEnd(method, result);
	}

	/**
	 * After all test cases have finished, log it
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logInfo(String.format("Ending with User Type [%s], with username : [%s]", userType, userName));
		Log.logTestClassEnd(this.getClass());
	}
}
