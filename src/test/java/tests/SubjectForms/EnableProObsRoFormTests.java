package tests.SubjectForms;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.Enums.SubjectDetailsFields;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 Created by maksym.tkachov on 2/17/2016.
 * Test Objective(s):
 * Upon enabling the disabled 'PRO' or ObsRO'
 * •	Cannot view the PRO or ObsRO form for visit scheduled for Subject before enabling
 * •	Can view the PRO or ObsRO form for new visit scheduled after enabling
 *
 * Prerequisite(s):
 * 1. Logged in user is having Site Rater - Type 5 role
 * 2. A study exists with the following:
 * 2.1 at least one 'PRO' and 'ObsRO' form type configured for the study
 * 2.2 at least one subject having:
 * 2.2.1 at least one visit with disabled 'PRO' and 'ObsRO' form.
 * 2.2.2 at least one visit with 'PRO' and 'ObsRO' form where visit is not scheduled
 *
 * Steps:
 * 1: Navigate to Subject details page of subject identified in prerequisite #2.2.1 and click on edit subject
 *      -Edit-mode is activated.  Check-boxes 'Disable PRO' and 'Disable ObsRO' are available for unselecting and fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO' are editable.
 * 2: Unselect check-boxes 'Disable PRO' and 'Disable ObsRO'
 *      -Fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO' dissappear.
 * 3: Select 'Disable PRO' and 'Disable ObsRO' again within this editing session
 *      -Fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO' appear with previously entered text.
 * 4: Unselect check-boxes and click on Cancel
 *      -Changes are not saved. Check-boxes are selected and the fields 'Reason for disabling PRO'
 *      and 'Reason for disabling ObsRO' are filled with previously entered text and date when PRO/ObsRO form disabled for Subject is displayed
 * 5: Click on visit identified in prerequisite #2.2.2
 *      -Assessment List for selected visit is displayed with the all forms, without PRO and ObsRO forms.
 * 6: Activate edit-mode again, unselect check-boxes for 'Disable PRO' and 'Disable ObsRO' and click on Save
 *      -Changes are saved. Date, when PRO/ObsRO forms was disabled for Subject and fields 'Reason for disabling PRO' and 'Reason for disabling ObsRO' dissappear.
 * 7: Click on visit identified in prerequisite #2.2.1
 * -Assessment List for selected visit is displayed with the all forms, without PRO and ObsRO forms..
 */
@Test(groups = { "EnableProObsRoForm" })
public class EnableProObsRoFormTests extends AbstractSubjectForms {
      
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAndOpenSubject(adminLogin, adminPasword, study, site, subjectName);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
//		dbSteps.deleteSubjectVisit(subjectName, sceduledvisitName);
		afterSteps.unScheduleVisitAndDeleteSubject(scheduledVisitName, scheduledVisitPos);
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}

	@Test(groups = { "ProObsRoFormViewAfterEnable",
			"SFB-TC-875" }, dependsOnGroups = "ProObsRoFormViewAfterDisable", description = "Verify form view for a scheduled visit upon enabling PRO and ObsRO form")
	public void proObsRoFormViewAfterEnableTest() {
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEPRO, disabledDate);
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEOBSRO, disabledDate);
		steps.clickOnEditSubjectButton();
		steps.subjectEditableVerification();
		steps.reasonFieldVerification(SubjectDetailsFields.DISABLEPRO, false);
		steps.reasonFieldVerification(SubjectDetailsFields.DISABLEOBSRO, false);
		steps.cancelChanges();
		steps.reasonContentVerification(SubjectDetailsFields.REASONPRO, reason);
		steps.reasonContentVerification(SubjectDetailsFields.REASONOBSRO, reason);
		
		steps.clickOnEditSubjectButton();
		steps.reasonFieldVerification(SubjectDetailsFields.DISABLEPRO, false);
		steps.reasonFieldVerification(SubjectDetailsFields.DISABLEOBSRO, false);
		steps.cancelChanges();
		
		steps.selectVisitInPosition(notScheduledVisitName, notScheduledVisitPos);
		templateSteps.templateCountVerification(0);
		steps.clickOnEditSubjectButton();
		steps.enableForm(SubjectDetailsFields.DISABLEPRO);
		steps.enableForm(SubjectDetailsFields.DISABLEOBSRO);
		steps.saveChangesWithEsign(adminLogin, adminPasword);
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEPRO, "");
		steps.subjectFieldValueVerification(SubjectDetailsFields.DISABLEOBSRO, "");
		steps.selectVisitInPosition(scheduledVisitName, scheduledVisitPos);
		templateSteps.templateCountVerification(scheduledVisitTemplates);
		steps.selectVisitInPosition(notScheduledVisitName, notScheduledVisitPos);
		templateSteps.templateCountVerification(notScheduledVisitTemplates);
	}
}
