package tests.Common;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;

@Test(groups = { "ToolBarAllSites" })
public class ToolBarAllSitesTest extends AbstractCommon {

	private static final String studyName = "Auto - SubjectStatusLabel";

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Browser.getDriver().manage().deleteAllCookies();
//		beforeSteps.loginAndChooseStudy(adminLogin, adminPasword, studyName);
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAs(adminLogin, adminPasword);
		if (commonSteps.getToStudyDashboard()) {
			beforeSteps.chooseStudy(studyName);
		} else {
			throw new SkipException("Couldn't choose Study. Skipping tests...");
		}
	}

	@Test(groups = { "UserWithMultipleStudySite",
			"SFB-TC-598" }, description = "Verify if no Study and site is selected automatically when logged in user is having access to more than one Study and Site")
	public void userWithMultipleStudySiteTest() {
		commonSteps.overOneEntityInSiteMenuVerification();
	}
}