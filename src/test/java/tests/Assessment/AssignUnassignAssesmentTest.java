package tests.Assessment;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.SubjectSteps;
import steps.Tests.TemplateSteps;

/**
 * Created by maksym.tkachov on 5/11/2016.
 *
 * Test Objective(s):    
 *  Check that if rater has qualification, he can be assigned to form in visit
 *  Prerequisite(s):
 *  1. Rater that logged in has qualification at least one form
 *  2. The subject details are opened with forms in visits
 *
 * Steps
 * 1	Select visit with form and schedule it
 *      -The dropdown menu should be appeared with Not Assigned option selected, delete button near visit should be displayed
 * 2	Assign form to Me
 *      -Me option selected, delete button near visit should not be displayed
 * 3	Assign form to Any rater
 *     -Rater name option selected, delete button near visit should not be displayed
 * 4	Unassign form
 *      -Not Assigned option selected, delete button near visit should be displayed
 */
@Test(groups = { "AssignUnassignAssesment" })
public class AssignUnassignAssesmentTest extends AbstractAssessment {
//    private String study = "Auto - SubjectStatusLabel";
//    private String site = "Site_1 - Francis Gibbes";
//    private String subjectName = "AssignAssessment";
//    private String visitName = "Visit 1";
    private String visitName = "AssignedClinRo";
    private String raterName = "Max Tkachov";
    TemplateSteps steps = new TemplateSteps();
    SubjectSteps subjectSteps = new SubjectSteps();

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		// dbSteps.deleteSubjectVisit(subjectName,visitName);
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenSubject(adminLogin, adminPasword, study, site, subjectName);
	}

    @AfterClass(alwaysRun = true)
    public void afterClass() {
       // dbSteps.deleteSubjectVisit(subjectName,visitName);
		steps.deleteSubjectVisit(subjectName, visitName);
        afterSteps.logout();
        Log.logTestClassEnd(this.getClass());
    }

	@Test(groups = { "AssignUnassignAssesmentToRater",
			"JamaNA" }, description = "Check assign and unassign forms for a rater")
	public void assignUnassignAssesmentToRaterTest() {
		subjectSteps.selectVisitByName("Scheduled", visitName);
		steps.scheduleVisit();
		steps.notAssignedOptionVerification();
		subjectSteps.deleteButtonVisitTableVerification(true);
		steps.assignFirstTemplateTo(Constants.ME);
		steps.assignedToRaterVerification(Constants.ME);
		subjectSteps.deleteButtonVisitTableVerification(false);
		steps.assignFirstTemplateTo(raterName);
		steps.assignedToRaterVerification(raterName);
		subjectSteps.deleteButtonVisitTableVerification(false);
		steps.unAssignTempalte();
		steps.notAssignedOptionVerification();
		subjectSteps.deleteButtonVisitTableVerification(true);
	}
}
