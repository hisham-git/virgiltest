package tests.Common;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;

@Test(groups = { "ToolBarOneSite" })
public class ToolBarOneSiteTest extends AbstractCommon {

//	private String studyName = "Auto - SubjectStatusLabel";
	private static final String studyName = "Auto - Subject";

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.getToStudyDashboard();
	}

	@Test(groups = { "UserWithSingleStudySite",
			"SFB-TC-597" }, description = "Verify the Study and site is selected automatically when logged in user is having access to only one Study and Site.")
	public void userWithSingleStudySiteTest() {
		commonSteps.chooseStudy(studyName);
		commonSteps.oneEntityInSiteMenuVerification();
	}
}