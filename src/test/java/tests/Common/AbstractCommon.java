package tests.Common;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "Common", "EnvironmentIndependent", "Sanity" })
public abstract class AbstractCommon extends AbstractTest {

	/**
	 * Before Class, Log the class name
	 * 
	 */

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
	}

	/**
	 * After all the tests of a class has run, log the name of the class that
	 * has been completed
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
}