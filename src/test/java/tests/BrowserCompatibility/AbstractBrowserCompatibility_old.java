/*package tests.BrowserCompatibility;

import java.net.MalformedURLException;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.tools.Log;
import steps.Tests.BrowserCompatibilityStep;
import tests.Abstract.AbstractTest;


public abstract class AbstractBrowserCompatibility_old extends AbstractTest{
	
	protected final String loginUrl = URLsHolder.getHolder().getSiteportalURL();
	protected final String warningMessage = "Your browser is not supported";
	
	protected Browser newBrowser;
	protected BrowserCompatibilityStep browserCompStep = new BrowserCompatibilityStep();
	
	@BeforeTest(alwaysRun = true)
	@Parameters({ "ffbrowser" })
	public void beforeTest(String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
	}

	@AfterClass(alwaysRun = true)
	public void afterTest() {
		Log.logInfo("Closing the browser");
		newBrowser.quit();
	}

}
*/