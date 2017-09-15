package tests.Subject;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.SubjectSteps;

/**
 * Created by maksym.tkachov on 1/25/2016.
 */
@Test(groups = "DeleteSubject")
public class DeleteSubjectTests extends AbstractSubject {
    private String visit = "Baseline";
    SubjectSteps steps = new SubjectSteps();

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, "");
	}

    @AfterClass(alwaysRun = true)
    public void afterClass(){
        afterSteps.logout();
        Log.logTestClassEnd(this.getClass());
    }

	@Test(groups = { "DeleteExistingSubject",
			"JamaNA" }, dependsOnGroups = "EditExistingSubject", description = "Check delete existing subject from site & verify the delete in Subject List")
	public void deleteExistingSubjectTest() {
		steps.openSubjectDetails(subjectNameUpdated);
		steps.subjectDetailsVerification(subjectNameUpdated);
		steps.deleteButtonSubjectDetailsVerification(true);
		steps.selectAndScheduleVisitInPosition(visit, 1);
		steps.deleteButtonSubjectDetailsVerification(false);
		steps.unScheduleVisit();
		steps.deleteButtonSubjectDetailsVerification(true);
		steps.deleteSubject();
		steps.subjectNotInSubjectTableVerification(subjectNameUpdated);
	}
}