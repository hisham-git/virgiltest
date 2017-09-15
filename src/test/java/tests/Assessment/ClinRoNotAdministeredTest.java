package tests.Assessment;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.AssessmentSteps;
import steps.Tests.EsignSteps;
import steps.Tests.TemplateSteps;

/**
 * Created by maksym.tkachov on 3/14/2016.
 *
 * Test objectives:
 * Submit ClinRO assessment by marking it as 'Not Administered' assessment
 * Prerequisite(s):
 * 1. At least one 'pending' ClinRO assessment exists for subject visit
 * 2. Logged user is Site Rater assigned to assessment identified in prerequisite #1
 * Steps:
 * 1: Navigate to assessment details page of assessment identified in prerequisite (#1)
 *      - Assessment details page is opened. Form thumbnail is displayed with 'Pending' label. Checkbox with 'Mark as not administered' label is displayed.
 * 2: Select checkbox with 'Mark as not administered' label
 *      - Checkbox is set. 'Confirm' control is appeared
 * 3: Unselect checkbox with 'Mark as not administered' label
 *      - Checkbox is removed. 'Confirm' button is disappeared
 * 4: Select checkbox with with 'Mark as Not administered' label and click 'Confirm'
 *      - Esign dialog appears . Input fields for 'username' and 'password' are displayed. 'OK' control is available (active). 'Cancel' control is available (active).
 * 5: Input correct credentials and click 'OK' control
 *      - E-sign dialog is closed. Assessment receives 'Processing' status. 'Mark as Not Administered' checkbox is not displayed
 * 6: Click 'Refresh' page after few seconds pass
 *      - Assessment receives 'Complete' status. Assessment form thumbnail design changed accordingly to 'Not administered' option.
 *      'Attachment' tab is appeared with Files section and 'Upload attachment' control.
 *      'Not Completed' disabled checkbox is displayed instead of 'Mark as Not Administered'
 * 7: Navigate to associated  Subject details page using 'Subject' link in assessment details section
 *      - Subject Details page is opened. 'Not administered' submitted in step #6 is displayed with designed 'not administered' form thumbnail.
 *      Appropriate submission information with 'Not administered' label are displayed.
 * 8: Navigate to associated Visit Details page
 *      - Visit Details page is opened. 'Not administered' submitted in step #6 is displayed with designed 'not administered' form thumbnail.
 *      Appropriate submission information with 'Not administered' label are displayed.
 */
@Test(groups = { "ClinRoNotAdministered", "E-Sign" })
public class ClinRoNotAdministeredTest extends AbstractAssessment{

    private String visitName = "AssignedClinRo";
    private int visitPos = 3, formPos = 1;

    AssessmentSteps steps = new AssessmentSteps();
    TemplateSteps templateSteps = new TemplateSteps();
    EsignSteps esignSteps = new EsignSteps();
    

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
//		dbSteps.deleteSubjectVisit(subjectName,visitName);
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenTemplate(adminLogin, adminPasword, study, site, subjectName, visitName, visitPos,
				formPos, Constants.ME);

	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
//		dbSteps.deleteSubjectVisit(subjectName,visitName);
		afterSteps.logout();
	}

	@Test(groups = { "ClinRoNotAdministeredAssessment",
			"SFB-TC-900" }, dependsOnGroups = "AssignUnassignAssesmentToRater", description = "Check submitting ClinRO assessment by marking it as 'Not Administered' assessment")
	public void clinRoNotAdministeredAssessmentTest() {
		steps.assessmentDetailsIsOpenedVerification();
		steps.assessmentStatusVerification(Constants.ASSESSMENTSTATUSPENDING);
		steps.notAdministredCheckboxVerification();
		steps.selectNotAdministeredCheckbox();
		steps.confirmButtonShowVerification();
		steps.selectNotAdministeredCheckbox();
		steps.confirmButtonHideVerification();
		steps.selectNotAdministeredCheckbox();
		steps.confirmNotAdministredAssessment();
		// updated by uttam
		esignSteps.esignDialogPredefinedReason(dropDownValue);
		esignSteps.esignDialogConfirm(adminLogin.trim(), adminPasword.trim());
		// TODO:
		// steps.assessmentStatusVerification(Constants.ASSESSMENTSTATUSPROCESSING);
		UiHelper.sleep(Constants.PROCCESINGTIME);
		steps.refreshAssessmentDetails();
		steps.assessmentStatusVerification(Constants.ASSESSMENTSTATUSCOMPLETE);
		steps.addAttachmentSectionVerification();
		steps.addAttachmentButtonVerification();
		steps.returnToSubjectDetails(subjectName);
		templateSteps.templateStatusVerification(Constants.NOTADMINISTERED);
		steps.returnToDashboard(study);
		steps.openAllVisits();
		steps.openVisitDetails(subjectName, visitName);
		templateSteps.templateStatusVerification(Constants.NOTADMINISTERED);
	}
}
