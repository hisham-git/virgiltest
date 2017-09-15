package tests.StudyProfile;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Tests.StudyProfileSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "StudyProfile", "EnvironmentIndependent", "Sanity" })
public class StudyProfileTest extends AbstractTest {

	private String studyName = "Auto - SubjectStatusLabel";
	private String siteName = "All Sites";
	private String noTemplatesDefinedMessage = "No templates are defined for this visit and language.";
	private String notAllTemplatesDefinedMessage = "Not all templates are defined for this visit and language.";
	private String visitBaseline = "Baseline";
	private String visit5 = "Visit 5";
	private String visit1 = "Visit 1";

    private StudyProfileSteps steps = new StudyProfileSteps();

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		// beforeSteps.loginAndChooseStudy(adminLogin,adminPasword,study);
		beforeSteps.loginAs(adminLogin, adminPasword);
		if (commonSteps.getToStudyDashboard()) {
			beforeSteps.chooseStudy(studyName);
			beforeSteps.chooseSite(siteName);
		} else {
			throw new SkipException("Couldn't open Study Dashboard. Skipping tests...");
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logStep("Closing Profile");
        afterSteps.closeStudyProfileAndLogout();
        Log.logTestClassEnd(this.getClass());
	}

	@Test(groups = { "StudyProfileContentAndControls", "SFB-TC-605"}, description = "View study profile content and controls")
	public void studyProfileContentAndControlsTest() {
        steps.openStudyProfile();
        steps.isOpenedStudyProfilePageVerification();
        steps.languageVerification(Constants.ENGLISH);
        steps.isFirstVisitSelectedVerification();
        steps.selectLanguageInDropdown(Constants.SPANISH);
        steps.languageVerification(Constants.SPANISH);
        steps.redMessageContentVerification(visitBaseline, Constants.ENGLISH, noTemplatesDefinedMessage);
        steps.redMessageContentVerification(visit5, Constants.SPANISH, notAllTemplatesDefinedMessage);
        steps.visitTemplateFormCountsCompareVerification(visit1, Constants.ENGLISH);
        steps.openForm(1);
        steps.openFormActionVerification();
    }
}
