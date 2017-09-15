package tests.SubjectForms;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.Enums.SubjectDetailsFields;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Created by maksym.tkachov on 2/12/2016.
 * Test Objective(s):
 * Upon disable PRO and ObsRO form
 * •	Can still view the PRO or ObsRO form for visit scheduled for Subject already
 * •	Cannot view the PRO or ObsRO form for new visit scheduled after disabling the form
 *
 * Prerequisite(s):
 * 1. Logged in user is having Site Rater - Type 5 role
 * 2. A study exists with the following:
 * 2.1. Forms with 'PRO'/'ObsRO' type are configured for Study
 * 2.2  At least one subject exists
 * 2.3. Subject has at least one scheduled visit with 'PRO' and 'ObsRO' forms
 * 2.4. Subject has at least one visit with configured 'PRO' and 'ObsRO' forms which is not created yet
 *
 * Steps
 * 1: Navigate to Subject details page of subject  identified in prerequisite #2.2 and click to edit subject
 *      - Edit-mode is activated.  Check-boxes 'Disable PRO' and 'Disable ObsRO' are available for selecting
 * 2: Select check-boxes 'Disable PRO' and 'Disable ObsRO
 *      - Fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO' appear and are highlighted
 * 3: Attempt to click "Save" without filling the fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO'
 *      - Save is disabled until the required fields are answered.
 * 4: Fill the fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO' and click on Cancel
 *      - Changes are not saved. Subject is stayed with unselected check-boxes for 'Disable PRO' and 'Disable ObsRO
 * 5: Activate edit-mode again, select check-boxes for 'Disable PRO' and 'Disable ObsRO'. Fill the fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO' and click on Save
 *      - Changes are saved. Date, when PRO and ObsRO forms was disabled for Subject is appeared.
 * 6: Click on visit identified in prerequisite #2.3.
 *      - Assessment List for selected visit is displayed with the all forms, including PRO and ObsRO forms.
 * 7: Click on add visit identified in prerequisite #2.4.
 *      - New visit is added. Assessment List for selected visit is displayed without PRO and ObsRO forms
 */
@Test(groups = { "DisableProObsRoForms" })
public class DisableProObsRoFormsTests extends AbstractSubjectForms {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Nav.toURL(sitePortalUrl);
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndScheduleVisit(adminLogin, adminPasword, study, site, subjectName, scheduledVisitName, scheduledVisitPos);
	}

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        afterSteps.logout();
        Log.logTestClassEnd(this.getClass());
    }

	@Test(groups = { "ProObsRoFormViewAfterDisable", "SFB-TC-873" }, description = "Verify form view for a scheduled visit upon disabling PRO and ObsRO form")
	public void proObsRoFormViewAfterDisableTest() {
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEPRO, "");
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEOBSRO, "");
		steps.clickOnEditSubjectButton();
		steps.subjectEditableVerification();
		steps.reasonFieldVerification(SubjectDetailsFields.DISABLEPRO, true);
		steps.reasonFieldVerification(SubjectDetailsFields.DISABLEOBSRO, true);
		steps.saveButtonVerification(true);
		steps.fillTheReasonTextArea(SubjectDetailsFields.DISABLEPRO, reason);
		steps.fillTheReasonTextArea(SubjectDetailsFields.DISABLEOBSRO, reason);
		steps.saveButtonVerification(false);
		steps.cancelChanges();
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEPRO, "");
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEOBSRO, "");
		steps.clickOnEditSubjectButton();
		steps.disableForm(SubjectDetailsFields.DISABLEPRO);
		steps.disableForm(SubjectDetailsFields.DISABLEOBSRO);
		steps.saveChangesWithEsign(adminLogin, adminPasword);
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEPRO, disabledDate);
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEOBSRO, disabledDate);
		steps.selectVisitInPosition(scheduledVisitName, scheduledVisitPos); //Hisham
		templateSteps.templateCountVerification(scheduledVisitTemplates);
		steps.selectVisitInPosition(notScheduledVisitName, notScheduledVisitPos); //Hisham
		templateSteps.templateCountVerification(0);
	}
}
