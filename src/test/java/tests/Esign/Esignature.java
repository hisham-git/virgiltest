package tests.Esign;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Tests.AssessmentSteps;
import steps.Tests.EsignSteps;
import tests.Abstract.AbstractTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

/**
 * Created by maksym.tkachov on 4/08/2016.
 *
 */
@Test(groups = { "E-sign" })
public class Esignature extends AbstractTest {

    private String study = "Auto - ProObsro";
    private String subjectName = "NotAdminAssessment";
    private String visitName = "AssignedClinRo";
	private int visitPos = 3, formPos = 1;
    private String site = "Site_1 - Francis Gibbes";

    EsignSteps steps = new EsignSteps();
    AssessmentSteps assessmentSteps = new AssessmentSteps();

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        Log.logTestClassStart(this.getClass());
        dbSteps.deleteSubjectVisit(subjectName,visitName);
		beforeSteps.loginAndOpenEsignDialog(adminLogin, adminPasword, study, site, subjectName, visitName, visitPos,
				formPos, Constants.ME);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() throws SQLException {
        dbSteps.deleteSubjectVisit(subjectName,visitName);
        afterSteps.logout();
    }

	@Test(groups = { "EsignDialogueBox", "SFB-TC-902" }, description = "E-Sign Not Administered form")
	public void eSignDialogueBoxTest() {
		steps.dialogIsOpenedVerification();
		steps.invalidCredentialsMessageVerification("", "");
		steps.invalidCredentialsMessageVerification(adminLogin, userPassword);
		steps.cancelButtonActionVerification();
		assessmentSteps.confirmNotAdministredAssessment();
		steps.esignDialogConfirm(adminLogin, adminPasword);
		assessmentSteps.assessmentStatusVerification(Constants.ASSESSMENTSTATUSPROCESSING);
	}
}