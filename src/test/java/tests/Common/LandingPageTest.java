package tests.Common;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;

@Test(groups = { "LandingPage" })
public class LandingPageTest extends AbstractCommon {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Nav.toURL(sitePortalUrl);
	}

	@Test(groups = { "LandingPageForUsers",
			"SFB-TC-599" }, description = "Verify Landing page controls and navigation based on roles and claims")
	public void landingPageForUsersTest() {
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.homepageContentAdminVerification();
		afterSteps.logout();
//		beforeSteps.loginAs(userLogin, userPassword);
		beforeSteps.loginAs(siteportalUserAccountName, siteportalUserAccountPassword);
		commonSteps.homepageContentUserVerification();
		// TODO add verification for other three types of user
	}
}