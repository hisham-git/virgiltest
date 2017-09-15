package tests.SubjectStatus;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.pages.studyDashboard.Filters;
import mt.siteportal.utils.Enums.SubjectDetailsFields;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

@Test(groups = { "DiscontinuedManually", "E-Sign" })
public class DiscontinuedManuallyTest extends AbstractSubjectStatus{
	protected static final String study = "Auto - SubjectStatusLabel";
	protected static final String site = "Site_2 - Bill Miles";
	protected static final String subjectName = "DiscontinuedManually";
	private List<String> subjectDetailsFieldsList = new ArrayList<>();
	private List<String> newValues = new ArrayList<>();

	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		subjectDetailsFieldsList.add(SubjectDetailsFields.STATUS.getValue());
		newValues.add(Filters.DISCONTINUED);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass(){
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}

	/**
	 * Subject Status - Discontinued Manually
	 * 
	 * Jama ID: SFB-TC-738
	 * 
	 * Test Objective(s):
	 * 	1. Ability for user to manually assign discontinued status for a subject
	 * 	2. Ability for any user to revert back the subject status from Current to previously assigned status
	 * 
	 * Prerequisite(s):
	 * 	1. A study with at least one subject NOT having discontinued status
	 * 
	 * Action & Expected Result:
	 *	1. Navigate to Subject Details view for the subject identified in prerequisite and enable edit mode
	 *		- Subject Status is editable
	 *
	 *	2. Select Discontinued in the Status drop-down
	 * 		- Status Comment textbox appeared
	 * 
	 *	3. Specify tester defined Status comment and Save subject
	 * 		- Subject status is changed to Discontinued and specified Status comment is shown	
	 * 
	 *	4. Logout
	 * 		- Logout successful
	 * 
	 * 	5. Login as different user and open Edit Subject Details view for the subject
	 * 		- Status Comment is editable
	 * 
	 * 	6. Expand Status drop-down
	 * 		- Previous Status of the subject is available
	 * 
	 * 	7. Select previous Status in the Status drop-down and save the change
	 * 		- Subject Status is changed to the previous one and Status Comment is deleted
	 */
	@Test(groups = { "AssignDiscontinuedStatusAndRevertBack",
			"SFB-TC-738" }, description = "Check manually assign discontinued status for a subject and reverting back")
	public void assignDiscontinuedStatusAndRevertBackTest() {
		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
		subjectSteps.editSubject(subjectName, subjectDetailsFieldsList, newValues, true, adminLogin, adminPasword);
		subjectSteps.subjectFieldValueVerification(SubjectDetailsFields.STATUS, newValues.get(0));

		afterSteps.logout();

		beforeSteps.loginAndOpenAllSubjects(siteportalUserAccountName, siteportalUserAccountPassword, study, site);
		newValues.remove(Filters.DISCONTINUED);
		newValues.add("");
		subjectSteps.editSubject(subjectName, subjectDetailsFieldsList, newValues, true, siteportalUserAccountName,
				siteportalUserAccountPassword);
		subjectSteps.subjectFieldValueVerification(SubjectDetailsFields.STATUS, newValues.get(0));
		subjectSteps.discontinuedStatusAreaVerification(false);
	}
}