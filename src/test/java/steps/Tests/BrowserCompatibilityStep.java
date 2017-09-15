package steps.Tests;

import org.openqa.selenium.By;

import hu.siteportal.pdfreport.StepVerify;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import steps.Abstract.AbstractStep;


public class BrowserCompatibilityStep extends AbstractStep{

	private By dissmissButton = new By.ByXPath("//input[@id='dismissBtn']");
	private By warningMessage = new By.ByCssSelector("h1.col-xs-offset-1.col-xs-10");
	private By loginButton = new By.ByCssSelector("#WebLogin_Login");
	
	/*public void compatibilityWaringMessageVerification(String msg) {
		Log.logStep("Checking browser compatibility warning message is present...");
		String warningText = UiHelper.findElement(warningMessage).getText().toString();
		if (warningText.equalsIgnoreCase(msg)) {
			Log.logInfo("Your browser is not supported message is displayed");
		} else {
			Log.logInfo("Your browser is  supportted message is  displayed");
		}

	}

	public void isPresentDissmissButton() {
		Log.logStep("Checking Dissmiss button is present on this..");
		boolean isDissmissButton = UiHelper.isPresent(dissmissButton);
		if (isDissmissButton) {
			Log.logInfo(" Dissmiss button is present ");
		} else {
			Log.logInfo(" Dissmiss button is not present ");
		}
	}

	public void dissmissWarningMessageVerification() {
		HardVerify.True(UiHelper.isClickable(dissmissButton),
				"Verifying dissmiss button is present to dissmiss the warning message", "[PASSED]",
				"Dissmiss button not found. [FAILED]");
		UiHelper.click(dissmissButton);
		HardVerify.True(UiHelper.isPresentAndVisible(loginButton),
				"Verifying login button is visible after clicking dismiss button", "[PASSED]",
				"Login button not found. [FAILED]");
	}*/
	
	public void compatibilityWaringMessageVerification(String msg) {
		Log.logStep("Checking browser compatibility warning message is present...");
		String warningText = UiHelper.findElement(warningMessage).getText().toString();
		StepVerify.True(warningText.equalsIgnoreCase(msg),
				"Checking browser compatibility warning message is present...",
				"'Your browser is not supported' message is displayed",
				"'Your browser is  supportted' message is  displayed");
		if (warningText.equalsIgnoreCase(msg)) {
			Log.logInfo("'Your browser is not supported' message is displayed");
		} else {
			Log.logInfo("'Your browser is  supportted' message is  displayed");
		}
	}

	public void isPresentDissmissButton() {
		Log.logStep("Checking Dissmiss button is present on this..");
		boolean isDissmissButton = UiHelper.isPresent(dissmissButton);
		StepVerify.True(isDissmissButton, "Checking Dissmiss button is present on this..",
				" Dissmiss button is present ", " Dissmiss button is not present ");
		if (isDissmissButton) {
			Log.logInfo(" Dissmiss button is present ");
		} else {
			Log.logInfo(" Dissmiss button is not present ");
		}
	}

	public void dissmissWarningMessageVerification() throws InterruptedException {
		StepVerify.True(UiHelper.isClickable(dissmissButton),
				"Verifying dissmiss button is present to dissmiss the warning message",
				"Dissmiss button is present to dissmiss the warning message", "Dissmiss button not found.");
		HardVerify.True(UiHelper.isClickable(dissmissButton),
				"Verifying dissmiss button is present to dissmiss the warning message", "[PASSED]",
				"Dissmiss button not found. [FAILED]");
		UiHelper.click(dissmissButton);
		Thread.sleep(3000);

		StepVerify.True(UiHelper.isPresentAndVisible(loginButton),
				"Verifying login button is visible after clicking dismiss button", "Login button  found",
				"Login button not found");
		HardVerify.True(UiHelper.isPresentAndVisible(loginButton),
				"Verifying login button is visible after clicking dismiss button", "[PASSED]",
				"Login button not found. [FAILED]");
	}
}
