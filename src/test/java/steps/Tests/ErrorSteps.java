package steps.Tests;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.objects.ConfirmPopUp;
import mt.siteportal.pages.ErrorContainer;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import hu.siteportal.pages.CentralRating.CrAppointmentDetails;
import hu.siteportal.pages.CentralRating.SelfCRAppointment;
import hu.siteportal.pdfreport.StepVerify;

/**
 * Created by maksym.tkachov on 7/11/2016.
 */
public class ErrorSteps extends AbstractStep {
	
	protected CentralRatingSteps crSteps;
	private final String successStatus = "Passed";
	private final String failStatus = "Failed";

	public ErrorSteps() {
		error = PageFactory.initElements(Browser.getDriver(), ErrorContainer.class);
	}

	public void errorContainerVerification(String errorMessage) {
		StepVerify.True(error.isContainerDisplayed(), "Verifying Error container is displayed while saving as a duplicate subject",
				"Error container is displayed", "Error container is not displayed");
		String messageFound = error.getErrorMessage();
		StepVerify.EqualsIgnoreCase(errorMessage, messageFound, "Verifying error container shows expected message...",
				"Error container shows expected message", successStatus,
				"Expected error message: [" + errorMessage + "] but found: [" + messageFound + "].", failStatus);
	}
	
	public void errorContainerVerification(String shouldDisplay, String errorMessage, String crType) {
		if (Boolean.parseBoolean(shouldDisplay)) {
			HardVerify.True(error.isContainerDisplayed(), "Verifying Error container is displayed...", successStatus,
					"Error container is not displayed. [FAILED]");
			String messageFound = error.getErrorMessage();
			HardVerify.EqualsIgnoreCase(errorMessage, messageFound,
					"Verifying error container shows expected message...", successStatus,
					"Expected error message: [" + errorMessage + "] but found: [" + messageFound + "]. [FAILED]");
			closeError();
		} else {
			switch (crType) {
			case "SelfCR":
				SelfCRAppointment selfCRAppointment = PageFactory.initElements(Browser.getDriver(),
						SelfCRAppointment.class);
				ConfirmPopUp confirmPopUp = new ConfirmPopUp(
						UiHelper.findElement(By.cssSelector("div#cancel-self-cr-modal")));
				if (UiHelper.isVisible(selfCRAppointment.getModalContent())) {
					HardVerify.False(error.isContainerDisplayed(), "Verifying Error container is not displayed...",
							successStatus, "Error container is displayed. [FAILED]");
					selfCRAppointment.clickModalCloseButton();
				} else if (confirmPopUp.isOpen()) {
					confirmPopUp.clickNoButton();
				}
				break;

			case "CR":
				CentralRatingSteps crSteps = new CentralRatingSteps();
				crSteps.appointmentDetailsVerification();
				HardVerify.False(error.isContainerDisplayed(), "Verifying Error container is not displayed...",
						successStatus, "Error container is displayed. [FAILED]");
				crSteps.backToSubject();

				break;
				
			case "NonCR":
				HardVerify.False(error.isContainerDisplayed(), "Verifying Error container is not displayed...",
						successStatus, "Error container is displayed. [FAILED]");
				break;

			default:
				break;
			}
		}
	}

	public void closeError() {
		Log.logStep("Closing error message container...");
		error.close();
	}
}