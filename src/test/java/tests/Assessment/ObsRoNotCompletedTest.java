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
 * Created by maksym.tkachov on 3/25/2016.
 *
 * Test objectives:Submit ObsRO assessment by marking it as 'Not Completed' assessment
 * Prerequisite(s):  At least two 'pending' ObsRO assessments exists for subject visit
 *
 * Steps:
 * 1: Navigate to assessment details page of assessment identified in prerequisite (#1)
 *      -Assessment details page is opened. Form thumbnail is displayed with 'Pending' label. Option to mark the form as Not completed is displayed.
 * 2: Select checkbox with 'Mark as Not Completed' label
 *      -Checkbox is set. 'Confirm' control is appeared
 * 3: Unselect checkbox with 'Mark as not administered' label
 *      - Checkbox is removed. 'Confirm' button is disappeared
 * 4: Select checkbox with 'Mark as Not Completed' label and click 'Confirm'
 *      - E-sign dialog appears . Input fields for 'username' and 'password' are displayed. 'OK' control is available (active). 'Cancel' control is available (active).
 * 5: Input correct credentials and click 'OK' control
 *      - E-sign dialog is closed. Assessment thumbnail indicates the processing' status. 'Mark as Not Completed' checkbox is not displayed
 * 6: Allow few minutes and refresh the page
 *      -Assessment thumbnail displays with  'Complete' status. 'Not completed' disabled check box is displayed instead 'Mark as Not Completed'.
 *      'Attachment' tab appeared with Files section and 'Upload attachment' control. 'Edit Form' control is displayed
 * 7: Navigate to associated  Subject details page using 'Subject' link in assessment details section
 *      -Subject Details page is opened. 'Not Completed' ObsRO assessment submitted in step #6 is displayed as 'not completed' . Appropriate submission information with 'Not Completed' label are displayed. Another 'Pending' ObsRO assessment identified in prerequisite #1 is displayed with 'Mark as 'Not Completed' link
 * 8: Click on 'Mark as 'Not Completed' link
 *      - E-sign dialog appears. 'Ok' control is displayed (active). 'Cancel' control is displayed (active)
 * 9: Input correct credentials and click 'OK" control
 *      - E-sign dialog is closed. Assessment thumbnail indicates the processing' status.
 * 10: Allow few minutes and refresh the page
 *      - Assessment thumbnail displays with  'Complete' status.  'Not completed' label is displayed.
 */
@Test(groups = { "ObsRoNotCompleted", "E-Sign" })
public class ObsRoNotCompletedTest extends AbstractAssessment{

    private String visitName = "AssignedObsRo";
    private int visitPos = 2;
    AssessmentSteps steps = new AssessmentSteps();
    TemplateSteps templateSteps = new TemplateSteps();
    EsignSteps esignSteps = new EsignSteps();

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        Log.logTestClassStart(this.getClass());
       // dbSteps.deleteSubjectVisit(subjectName,visitName);
        beforeSteps = new BeforeSteps();
        beforeSteps.loginAndScheduleVisit(adminLogin, adminPasword, study, site, subjectName, visitName, visitPos);
        steps.returnToDashboard(study);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
      //  dbSteps.deleteSubjectVisit(subjectName,visitName);
        afterSteps.logout();
    }

	@Test(groups = { "ObsRoNotCompletedAssessment",
			"SFB-TC-908" }, dependsOnGroups = "ClinRoNotAdministeredAssessment", description = "Check submitting ObsRO assessment by marking it as 'Not Completed' assessment")
	public void obsRoNotCompletedAssessmentTest() {
        steps.openAllAssessments();
        steps.openAssessmentDetails(subjectName,visitName);
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
        esignSteps.esignDialogConfirm(adminLogin,adminPasword);
//TODO: steps.assessmentStatusVerification(Constants.ASSESSMENTSTATUSPROCESSING);
        UiHelper.sleep(Constants.PROCCESINGTIME);
        steps.refreshAssessmentDetails();
        steps.assessmentStatusVerification(Constants.ASSESSMENTSTATUSCOMPLETE);
        steps.addAttachmentSectionVerification();
        steps.addAttachmentButtonVerification();
        steps.returnToDashboard(study);
        steps.openAllVisits();
        steps.openVisitDetails(subjectName,visitName);
        templateSteps.templateStatusVerification(Constants.NOTCOMPLETED);
       
//      dbSteps.deleteSubjectVisit(subjectName+"_2",visitName);
        steps.returnToDashboard(study);
        steps.openAllSubjects();
        steps.openSubjectDetails(subjectName+"_2");
        subjectStep.selectVisitInPosition(visitName, visitPos);
        steps.scheduleVisit();
        templateSteps.makeTemplateAsNotCompleted();
        esignSteps.esignDialogPredefinedReason(dropDownValue);
        esignSteps.esignDialogConfirm(adminLogin,adminPasword);
        UiHelper.sleep(Constants.PROCCESINGTIME);
        templateSteps.templateStatusVerification(Constants.PROCESSING);
        steps.refreshAssessmentDetails();
        templateSteps.templateStatusVerification(Constants.NOTCOMPLETED);
        
    }
}
