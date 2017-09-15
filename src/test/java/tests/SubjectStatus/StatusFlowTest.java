package tests.SubjectStatus;


import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dbunit.dataset.DataSetException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Configuration.CommonSteps;

@Test(groups = { "StatusFlow", "E-Sign" })
public class StatusFlowTest extends AbstractSubjectStatus{
	private DateFormat subjectFormat = new SimpleDateFormat("MMddyy");
	private String formattedSubject = subjectFormat.format(new Date());
	private String suffix = "-" + CommonSteps.generateRandomNames(2);
	private String subjectName = "StatusFlowTest#" + formattedSubject + suffix;
	
	private String visitName = "Visit 1";
	private String secondVisitName = "Visit 2";
	private String thirdVisitName = "Visit 3";
	private String fourthVisitName = "Visit 4";
	private String fifthVisitName = "Visit 5";
	private String newStatus = Constants.SUBJECTSTASUSES[0];
	private String emptyStatus = "";
	private String screenedStatus = Constants.SUBJECTSTASUSES[1];
	private String enrolledStatus = Constants.SUBJECTSTASUSES[2];
	private String completedStatus = Constants.SUBJECTSTASUSES[3];
	private String discontinuedStatus = Constants.SUBJECTSTASUSES[4];
	private int position = 1;
	//In this test case we should use study with one site

	@BeforeClass(alwaysRun = true)
	public void creatingSubject() throws DataSetException, SQLException{
		Log.logTestClassStart(this.getClass());
//		dbSteps.deleteSubject(subjectName);
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}

	/**
	 * Subject Status - Flow
	 * 
	 * Jama ID: SFB-TC-736
	 * 
	 * Test Objective(s):
	 * 	Verify that subject gain status on visit completion according to study configuration.
	 * 
	 * Prerequisite(s):
	 * 	1. A study exists with the following:
	 * 		a. First study visit gain NO status
	 * 		b. Second study visit gain Screened status
	 * 		c. Third study visit gain Enrolled status
	 * 		d. Fourth study visit gain NO status
	 * 		e. Early Termination study visit gain Discontinued status
	 * 
	 * Action & Expected Result:
	 *	1. Create New subject and schedule first visit specified in prerequisites for the subject
	 *		- The Subject has New status
	 * 		
	 *	2. Complete first visit for the subject
	 * 		- New status is cleared and the subject has no status
	 * 
	 *	3. Schedule and complete Early Termination visit for the subject
	 * 		- The subject gained Discontinued status	
	 * 
	 *	4. Create new subject, schedule and complete second visit predefined in prerequisites for this subject
	 * 		- New status is cleared and the subject gained Screening status
	 * 
	 * 	5. Create new subject, schedule and complete third visit predefined in prerequisites for this subject
	 * 		- Enrolled status is displayed on subject details page
	 * 	
	 * 	6. Create new subject, schedule and complete fourth visit predefined in prerequisites for this subject
	 * 		- Enrolled status is displayed on subject details page // TODO: requires clarification
	 */
	@Test(groups = { "SubjectStatusOnVisitCompletion",
			"SFB-TC-736" }, description = "Check that subject gain status on visit completion according to study configuration")
	public void subjectStatusOnVisitCompletionTest() {
		subjectSteps.createNewSubject(subjectName);
		subjectSteps.subjectDetailsVerification(subjectName);

		subjectSteps.selectAndScheduleVisitInPosition(visitName, 0);
		subjectSteps.subjectDetailsSubjectStatusVerification(subjectName, newStatus);

		subjectSteps.selectVisitInPosition(visitName, 0); //new step
		templateSteps.assignTemplateToMe(visitName, position, true, true);
		assessmentSteps.makeTemplateNotAdministered(adminLogin, adminPasword);
		subjectSteps.returnToSubjectDetails(subjectName);
		subjectSteps.subjectDetailsSubjectStatusVerification(subjectName, emptyStatus);

		subjectSteps.selectVisitInPosition(secondVisitName, 1); //new step
		templateSteps.assignTemplateToMe(secondVisitName, position, false, true);
		assessmentSteps.makeTemplateNotAdministered(adminLogin, adminPasword);
		subjectSteps.returnToSubjectDetails(subjectName);
		subjectSteps.subjectDetailsSubjectStatusVerification(subjectName, screenedStatus);

		subjectSteps.selectVisitInPosition(thirdVisitName, 2); //new step
		templateSteps.assignTemplateToMe(thirdVisitName, position, false, true);
		assessmentSteps.makeTemplateNotAdministered(adminLogin, adminPasword);
		subjectSteps.returnToSubjectDetails(subjectName);
		subjectSteps.subjectDetailsSubjectStatusVerification(subjectName, enrolledStatus);

		subjectSteps.selectVisitInPosition(fourthVisitName, 3); //new step
		templateSteps.assignTemplateToMe(fourthVisitName, position, false, true);
		assessmentSteps.makeTemplateNotAdministered(adminLogin, adminPasword);
		subjectSteps.returnToSubjectDetails(subjectName);
		subjectSteps.subjectDetailsSubjectStatusVerification(subjectName, completedStatus);

		subjectSteps.addUnscheduledVisit(fifthVisitName);
		subjectSteps.selectVisitInPosition(fifthVisitName, 4); //new step
		templateSteps.assignTemplateToMe(fifthVisitName, position, true, true);
		assessmentSteps.makeTemplateNotAdministered(adminLogin, adminPasword);
		subjectSteps.returnToSubjectDetails(subjectName);
		subjectSteps.subjectDetailsSubjectStatusVerification(subjectName, discontinuedStatus);

		subjectSteps.discontinuedVerification();
	}
}
