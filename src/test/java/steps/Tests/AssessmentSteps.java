package steps.Tests;

import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.EsignReasonFields;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

/**
 * Created by maksym.tkachov on 6/27/2016.
 */
public class AssessmentSteps extends AbstractStep{
	
	private EsignSteps esignSteps = new EsignSteps();

    public AssessmentSteps(){
        assessmentDetails = PageFactory.initElements(Browser.getDriver(),AssessmentDetails.class);
        assessmentsTable = PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
    }

    public void selectNotAdministeredCheckbox(){
        assessmentDetails.asNotAdministered();
    }

    public void confirmNotAdministredAssessment(){
        assessmentDetails.confirmAssesment();
    }

    public void refreshAssessmentDetails(){
        assessmentDetails.clickRefresh();
    }

    public void assessmentDetailsIsOpenedVerification(){
        Log.logVerify("AssessmentDetails is opened");
        Verify.True(assessmentDetails.isOpened(), "The assessment details is not opened");
    }

    public void assessmentStatusVerification(String status){
        Log.logVerify("Assessment status is "+status);
        Verify.Equals(status, assessmentDetails.getAssessmentStatus(), "The status is incorrect");
    }

    public void notAdministredCheckboxVerification(){
        Log.logVerify("Checkbox not administered is displayed");
        Verify.True(UiHelper.isPresent(assessmentDetails.getNotAdminsteredCheckbox()), "The checkbox is not displayed");
    }

	public void confirmButtonShowVerification() {
		HardVerify.True(UiHelper.isClickable(assessmentDetails.confirm), "Verifying 'Confirm' button is displayed",
				"Confirm button is displayed [Passed]", "Confirm button is not displayed [Failed]");
	}

	public void confirmButtonHideVerification() {
		HardVerify.False(UiHelper.isClickable(assessmentDetails.confirm), "Verifying 'Confirm' button is not displayed",
				"Confirm button is not displayed [Passed]", "Confirm button is displayed [Failed]");
	}
    public void addAttachmentSectionVerification(){
        Log.logVerify("Add attachment section is displayed");
        Verify.True(UiHelper.isPresent(assessmentDetails.mtAttachments), "The attachment section is not displayed");
    }

    public void addAttachmentButtonVerification(){
        Log.logVerify("Add attachment button is displayed");
        Verify.True(UiHelper.isPresent(assessmentDetails.mtAddAttachment), "The Add attachment button is not displayed");
    }

    public int getAssessmentCountInTable(){
        return assessmentsTable.getTable().size();
    }

	public void makeTemplateNotAdministered(String userName, String pass) {
		selectNotAdministeredCheckbox();
		assessmentDetails.confirmAssesment();
		if (esignSteps.dialogIsOpenedVerification()) {
			esignSteps.esignDialogPredefinedReason(EsignReasonFields.TECHNICAL.getValue());
			esignSteps.esignDialogConfirm(userName, pass);
			String status = assessmentDetails.getStatusFromThumbnail();
			Log.logInfo("Assessment is in " + status.toUpperCase() + " status");
			Log.logStep("Processing the request...");
			while (!status.equalsIgnoreCase("Complete")) {
				refreshAssessmentDetails();
				UiHelper.sleep(5000);
				status = assessmentDetails.getStatusFromThumbnail();
			}
			Log.logInfo("Assessment is in COMPLETE status");
		} else {
			throw new SkipException("E-sign dialogue couldn't be opened. Skipping tests...");
		}
	}
}
