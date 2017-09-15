package steps.Tests;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import steps.Abstract.AbstractStep;
import steps.Configuration.CommonSteps;

public class EuPolicySteps extends AbstractStep {
	
	CommonSteps commonSteps = new CommonSteps();

	public void homePrivacyPolicyVerificationFromHeader() {
		Log.logInfo("Checking Privacy policy is present on header application menu from Virgil home");	
		commonSteps.headerPrivacyPolicyActionVerification();
		this.getOriginalWindow();
		
		
	}

	public void studyPrivacyPolicyVerificationFromHeader() {
		Log.logInfo("Checking Privacy policy is present on header application menu from Virgil Study dashboard");	
		commonSteps.getToStudyDashboard();		
		commonSteps.headerPrivacyPolicyActionVerification();
		this.getOriginalWindow();
		commonSteps.openHomePage();
	}
	
	
	public void getOriginalWindow(){
		String originalHandle = Browser.getDriver().getWindowHandle();
	    Log.logStep("Closing Privacy Policy browser tab..");
	    for(String handle : Browser.getDriver().getWindowHandles()) {
	        if (!handle.equals(originalHandle)) {
	        	Browser.getDriver().switchTo().window(handle);
	        	Browser.getDriver().close();
	        }
	    }
	    
	    Browser.getDriver().switchTo().window(originalHandle);
	}

	public void administrationPrivacyPolicyVerificationFromHeader() {
		Log.logInfo("Checking Privacy policy is present on header application menu from Virgil Administration dashboard");	
		commonSteps.openAdministration();
		commonSteps.headerPrivacyPolicyActionVerification();
		this.getOriginalWindow();
		commonSteps.openHomePage();
		
	}

	public void homePrivacyPolicyVerificationFromFooter() {	
		Log.logInfo("Checking Privacy policy is present on header application menu from Virgil home");
		commonSteps.footerPrivacyPolicyActionVerification();
		this.getOriginalWindow();
		commonSteps.openHomePage();
	}

	public void studyPrivacyPolicyVerificationFromFooter() {
		Log.logInfo("Checking Privacy policy is present on footer application menu from Virgil Study dashboard");
		commonSteps.getToStudyDashboard();
		commonSteps.footerPrivacyPolicyActionVerification();
		this.getOriginalWindow();
		commonSteps.openHomePage();
	}

	public void administrationPrivacyPolicyVerificationFromFooter() {
		Log.logInfo("Checking Privacy policy is present on footer application menu from Virgil Administration dashboard");
		commonSteps.openAdministration();
		commonSteps.footerPrivacyPolicyActionVerification();
		this.getOriginalWindow();
		commonSteps.openHomePage();
	}

}
