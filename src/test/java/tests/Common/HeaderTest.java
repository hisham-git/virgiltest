package tests.Common;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;

@Test(groups = { "Header" })
public class HeaderTest extends AbstractCommon {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
//		beforeSteps.loginAs(userLogin, userPassword);
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAs(siteportalUserAccountName, siteportalUserAccountPassword);
	}

	@Test(groups = { "HeaderControls",
			"SFB-TC-558" }, description = "Verify Application Headar menu provides quick navigation through defined portal components.")
	public void headerControlsTest() {
		commonSteps.headerVirgilLogoActionVerification(Constants.USER);
		commonSteps.headerHomeButtonActionVerification(Constants.USER);
		commonSteps.headerAboutButtonActionVerification();
		commonSteps.headerLogoutButtonActionVerification();
		beforeSteps.loginAndOpenDashboard(adminLogin, adminPasword);
		commonSteps.headerVirgilLogoActionVerification(Constants.ADMIN);
		commonSteps.headerHomeButtonActionVerification(Constants.ADMIN);
		commonSteps.headerAdministrationButtonActionVerification();
		commonSteps.headerAboutButtonActionVerification();
	}
}