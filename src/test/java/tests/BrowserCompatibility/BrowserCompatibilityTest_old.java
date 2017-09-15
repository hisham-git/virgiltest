/*package tests.BrowserCompatibility;

import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;

import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

*//**
 * Description: This class verifies browser compatibility warning message for
 * not compatible browsers
 * 
 * @author ubiswas
 *
 *//*
public class BrowserCompatibilityTest_old extends AbstractBrowserCompatibility_old {
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		Log.logTestClassStart(this.getClass());
	}
	@Test(groups = {
			"BrowserCompatibility", "JamaNA" }, description = "Checking browser incompatibility warning is displayed on login screen when user logged into unsupported browsers.")
	public void browserIncompatibilityTest() throws InterruptedException {
		browserCompStep.compatibilityWaringMessageVerification(warningMessage);
		browserCompStep.isPresentDissmissButton();
		browserCompStep.dissmissWarningMessageVerification();
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		Log.logStep("Navigating to Login page...");
		Nav.toURL(loginUrl);
	}
	
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method) {
		Log.logTestMethodEnd(method);
		
	}
	
	
	@AfterClass(alwaysRun = true)
	public void afterClass(){
		Log.logTestClassEnd(this.getClass());
	}

}
*/