package tests.SubjectVisit;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

/**
 * Created by maksym.tkachov on 2/26/2016.
 * Refactored by Abdullah Al Hisham on 12/22/2016.
 */
@Test(groups = { "SubjectVisit", "DeleteSubjectVisit" })
public class DeleteSubjectVisitTests extends AbstractTest {
    private String study = "Auto - ProObsro";
    private String site = "Site_1 - Francis Gibbes";
    private String subjectName = "DeleteSubjectVisit";
    private String assignedObsRo = "AssignedObsRo";
//    private int assignedObsRoPos = 2;
    private String assignedClinRo = "AssignedClinRo";
//    private int assignedClinRoPos = 3;
    private String submittedPro = "SubmittedPro";
//    private int submittedProPos = 4;
    private String notAssigned = "NotAssigned";
//    private int notAssignedPos = 5;
    SubjectSteps steps = new SubjectSteps();

    @BeforeClass(alwaysRun = true)
    public void beforeSteps() {
        Log.logTestClassStart(this.getClass());
        beforeSteps = new BeforeSteps();
        beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() throws SQLException {
        afterSteps.logout();
        Log.logTestClassEnd(this.getClass());
    }

    /**
	 * Subject Status - Discontinued Manually
	 * 
	 * Jama ID: SFB-TC-918
	 * 
	 * Test Objective(s):
	 * 	1. Visit with assigned forms can't be deleted
	 * 	2. Visit without assigned forms can be deleted.
	 * 
	 * Prerequisite(s):
	 * 	1. At least one subject exists with following:
	 * 		1.1 Subject has at least one visit with assigned ObsRO form
	 * 		1.2 Subject has at least one visit with assigned ClinRO form
	 * 		1.3 Subject has at least one visit with submitted PRO form
	 * 		1.4 Subject has at least one visit with not assigned ObsRO and ClinRO forms and not submitted PRO form
	 * 
	 * Action & Expected Result:
	 *	1. Log in to Portal and navigate to Subject details for subject (Pr. #1)
	 *		- Subject details is opened. Visit list is displayed.
	 *
	 *	2. Click on subject visit from (Pr. #1.1)
	 * 		- Option to delete visit is not available
	 * 
	 *	3. Click on subject visit from (Pr. #1.2)
	 * 		- Option to delete visit is not available	
	 * 
	 *	4. Click on subject visit from (Pr. #1.3)
	 * 		- Option to delete visit is not available
	 * 
	 * 	5. Click on subject visit from (Pr. #1.4)
	 * 		- Option to delete visit is available
	 * 
	 * 	6. Click on delete option
	 * 		- Visit is deleted
	 * 		- Control to Add visit is available	
	 */
	@Test(groups = { "DeleteSubjectWithAndWithoutAssignedVisit",
			"SFB-TC-918" }, description = "Checks that visit with and without assigned forms can't be deleted")
	public void deleteSubjectWithAndWithoutAssignedVisitTest() {
		steps.openSubjectDetails(subjectName);
		steps.subjectDetailsVerification(subjectName);
//		steps.selectVisitInPosition(assignedObsRo, assignedObsRoPos);
		steps.selectVisitByName("Scheduled", assignedObsRo);
		steps.deleteButtonVisitTableVerification(false);

//		steps.selectVisitInPosition(assignedClinRo, assignedClinRoPos);
		steps.selectVisitByName("Scheduled", assignedClinRo);
		steps.deleteButtonVisitTableVerification(false);

//		steps.selectVisitInPosition(submittedPro, submittedProPos);
		steps.selectVisitByName("Scheduled", submittedPro);
		steps.deleteButtonVisitTableVerification(false);

//		steps.selectVisitInPosition(notAssigned, notAssignedPos);
		steps.selectVisitByName("Scheduled", notAssigned);
		steps.scheduleVisit();
		steps.deleteButtonVisitTableVerification(true);

		steps.unScheduleVisit();
		steps.visitDeletionVerification();
	}
}
