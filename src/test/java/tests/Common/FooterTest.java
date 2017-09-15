package tests.Common;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;

@Test(groups = { "Footer" })
public class FooterTest extends AbstractCommon {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
//		beforeSteps.loginAs(userLogin, userPassword);
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAs(siteportalUserAccountName, siteportalUserAccountPassword);
	}

	@Test(groups = { "FooterControls",
			"SFB-TC-993" }, description = "Verify Application Footer menu provides quick navigation through defined portal components.")
	public void footerControlsTest() {
		commonSteps.footerHomeButtonActionVerification(Constants.USER);
		commonSteps.footerAboutButtonActionVerification();
		commonSteps.footerLogoutButtonActionVerification();
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.footerHomeButtonActionVerification(Constants.ADMIN);
		commonSteps.footerApplicationMenuAdministrationButton();
		commonSteps.footerAboutButtonActionVerification();
	}
}