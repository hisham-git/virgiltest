package tests.AssessmentDetails;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

@Test(groups = { "EsignEyeCharacters" })
public class EyeCharactersVisibilityTest extends AbstractAssessmentDetails {

	@DataProvider
	public Object[][] assessmentWithFlags() {
		return new Object[][] { { "Visit7", "DV-A", "Admin" }, { "Visit8", "CGI-I", "Paper" } };
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
		detailsSteps.getToAssessmentList();
	}

	@Test(groups = { "EsignEyeCharactersVisibilityForAddingFlags",
			"JamaNA" }, description = "Check that while adding ClinRo assessment flag for paper transcription,Eye Characters are visible on e-sign window.", dataProvider = "assessmentWithFlags")
	public void esignEyeCharactersVisibilityForAddingFlagsTest(String visitName, String assessmentName, String type) {
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
		detailsSteps.checkOrUncheckAssessmentFlagCheckboxAndClickConfrimBtn(type);
		detailsSteps.eyeCharactersVisibilityChecking(siteportalUserAccountName, siteportalUserAccountPassword, type, dropDownvalue);
		detailsSteps.navigateBackToAssessmentList();
	}

	@Test(groups = { "EsignEyeCharactersVisibilityForRemovingFlags",
			"JamaNA" }, dependsOnGroups = "EsignEyeCharactersVisibilityForAddingFlags", description = "Check that while Removing ClinRo Assessment flag Eye Characters are visible on e-sign dialog window.", dataProvider = "assessmentWithFlags")
	public void esignEyeCharactersVisibilityForRemovingFlagsTest(String visitName, String assessmentName, String type) {
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
		detailsSteps.checkOrUncheckAssessmentFlagCheckboxAndClickConfrimBtn(type);
		detailsSteps.eyeCharactersVisibilityChecking(siteportalUserAccountName, siteportalUserAccountPassword, type, dropDownvalue);
		detailsSteps.navigateBackToAssessmentList();
	}

}
