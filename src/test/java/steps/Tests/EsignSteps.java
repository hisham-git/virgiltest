package steps.Tests;

import mt.siteportal.pages.studyDashboard.EsignDialog;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.support.PageFactory;

/**
 * Created by maksym.tkachov on 7/1/2016.
 */
public class EsignSteps extends AbstractStep {

    public EsignSteps (){
        esignDialog=PageFactory.initElements(Browser.getDriver(),EsignDialog.class);
    }
    
	public void esignDialogPredefinedReason(String dropDownvalue) {
		esignDialog.inputPredefinedReason(dropDownvalue);
	}
	
	public void esignDialogOthersReason(String comment) {
		esignDialog.inputOthersReason(comment);
	}
    
    public void esignDialogConfirm(String loginName, String password){
        esignDialog.loginAs(loginName,password);
    }

	public boolean dialogIsOpenedVerification() {
		Log.logVerify("Verifying E-sign dialog is opened...");
		boolean isOpened = esignDialog.isDialogOpened();
		Verify.True(isOpened, "The E-sign dialog is not opened or doesn't contain all content");
		return isOpened;
	}
	
    public void invalidCredentialsMessageVerification(String login, String pass) {
        Log.logVerify("the displaying of error message");
        Verify.True(esignDialog.isCredentialsValid(login, pass), "the error message is not displayed");
    }

    public void cancelButtonActionVerification() {
        Log.logVerify("Cancel button behavior");
        esignDialog.cancelEsign();
        Verify.False(esignDialog.isDialogOpened(), "The esign dialog is opened but should be closed");
    }
}
