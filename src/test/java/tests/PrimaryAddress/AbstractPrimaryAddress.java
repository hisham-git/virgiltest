package tests.PrimaryAddress;

import java.net.MalformedURLException;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import steps.Tests.CentralRatingSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "PrimaryAddress" })
public abstract class AbstractPrimaryAddress extends AbstractTest { 
	protected String study, site, subjectName, login, pass, showErrorMessage;
	protected static final String errorMsg = "You cannot schedule/cancel the appointment as there is no active Primary address marked. Please have one of the active addresses marked as Primary and try to schedule/cancel the appointment again.";
//	protected static final String errorMsg = "You cannot schedule/cancel the appointment as there is no Primary address marked. Please have one of the addresses marked as Primary and try to schedule/cancel the appointment again.";
	// ------------------------------------------------------------------------------------------------------------------------
	protected SubjectSteps subjectSteps;
	protected CentralRatingSteps crSteps;
	protected ErrorSteps errorSteps;
	// ------------------------------------------------------------------------------------------------------------------------
	
	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public synchronized void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
}