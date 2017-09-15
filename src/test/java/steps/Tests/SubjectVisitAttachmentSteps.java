package steps.Tests;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import mt.siteportal.details.AttachmentsTab;
import mt.siteportal.details.CommonDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.objects.ConfirmPopUp;
import mt.siteportal.objects.UploadFilesPopUp;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.BrowserPDFViewerHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import tests.AssessmentDetails.FileUploadHelper;

/**
 * Created by Abdullah Al Hisham on 01/31/2017
 */

public class SubjectVisitAttachmentSteps extends CommonSteps {

	private CommonDetails commonDetails;
	private SubjectDetails subjectDetails;
	private VisitDetails visitDetails;
	private AttachmentsTab attachmentsTab;

	public SubjectVisitAttachmentSteps() {
		super();
		commonDetails = PageFactory.initElements(Browser.getDriver(), CommonDetails.class);
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
//		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
	}

	/*public void verifyAttachmentTabIsPresent(String context, boolean isPresent) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
		switch (context) {
		case "Subject":
			if (isPresent) {
				HardVerify.True(subjectDetails.attachmentsTab.isAttachmentsTabLinkPresent(),
						"Verifying 'Attachments' tab is present", "[PASSED]", "Attachments tab not found [FAILED]");
			} else {
				HardVerify.False(subjectDetails.attachmentsTab.isAttachmentsTabLinkPresent(),
						"Verifying 'Attachments' tab is not present", "[PASSED]", "Attachments tab found [FAILED]");
			}
			break;
		
		case "Visit":
			if (isPresent) {
				HardVerify.True(visitDetails.attachmentsTab.isAttachmentsTabLinkPresent(),
						"Verifying 'Attachments' tab is present", "[PASSED]", "Attachments tab not found [FAILED]");
			} else {
				HardVerify.False(visitDetails.attachmentsTab.isAttachmentsTabLinkPresent(),
						"Verifying 'Attachments' tab is not present", "[PASSED]", "Attachments tab found [FAILED]");
			}
			break;
		
		default:
			Log.logError("Context not matched.");
			break;
		}
	}*/
	
	public void verifyAttachmentTabIsPresent(boolean isPresent) {
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		if (isPresent) {
			HardVerify.True(attachmentsTab.isAttachmentsTabLinkPresent(), "Verifying 'Attachments' tab is present",
					"[PASSED]", "Attachments tab not found [FAILED]");
		} else {
			HardVerify.False(attachmentsTab.isAttachmentsTabLinkPresent(), "Verifying 'Attachments' tab is not present",
					"[PASSED]", "Attachments tab found [FAILED]");
		}
	}

	public void verifyAddVisitIsPresent(boolean isPresent) {
		if (isPresent) {
			HardVerify.NotNull(subjectDetails.visitTable.getAddVisitButton(), "Verifying 'Add Visit' button is present",
					"[PASSED]", "'Add Visit' button not found [FAILED]");
		} else {
			HardVerify.Null(subjectDetails.visitTable.getAddVisitButton(),
					"Verifying 'Add Visit' button is not present", "[PASSED]", "'Add Visit' button found [FAILED]");
		}
	}

	public void verifyAttachmentTabIsSelected(boolean isSelected) {
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		if (isSelected) {
			HardVerify.True(attachmentsTab.isAttachmentsTabSelected(),
					"Verifying 'Attachments' tab is selected by default", "[PASSED]",
					"'Attachments' tab is not selected by default [FAILED]");
		} else {
			HardVerify.False(attachmentsTab.isAttachmentsTabSelected(),
					"Verifying 'Attachments' tab is not selected by default", "[PASSED]",
					"'Attachments' tab is selected by default [FAILED]");
		}
	}

	public void verifyTabHeaderAttachmentCount(int expectedCount) {
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		HardVerify.Equals(expectedCount, attachmentsTab.getAttachmentCount(),
				"Verifying Attachments count on tab header is: [" + expectedCount + "]", "[PASSED]",
				"'Attachments' count found: [" + attachmentsTab.getAttachmentCount() + "] [FAILED]");
	}

	public void clickAttachmentsTabLink() {
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		Log.logStep("Clicking the Attachments Tab Link...");
		attachmentsTab = attachmentsTab.clickAttachmentsTabLink();
	}

	public void verifyUploadAttachmentButtonIsPresent(boolean isPresent) {
		if (isPresent) {
			HardVerify.True(attachmentsTab.isUploadAttachmentsButtonPresent(),
					"Verifying 'Upload Attachment' button is present", "[PASSED]",
					"'Upload Attachment' button not found [FAILED]");
		} else {
			HardVerify.False(attachmentsTab.isUploadAttachmentsButtonPresent(),
					"Verifying 'Upload Attachment' button is not present", "[PASSED]",
					"'Upload Attachment' button found [FAILED]");
		}
	}

	private UploadFilesPopUp uploadFilesPopUp;

	public void clickAddAttachmentButton() {
		uploadFilesPopUp = attachmentsTab.clickUploadAttachmentsButton();
	}

	public void verifyPopUpIsDisplayed() {
		HardVerify.True(uploadFilesPopUp.isOpen(), "Verifying 'Upload Files' pop up is displayed", "[PASSED]",
				"'Upload Files' pop up found not displayed [FAILED]");
	}

	public void verifyPopUpControls() {
		HardVerify.NotNull(uploadFilesPopUp.getAddFileButton(),
				"Verifying 'Add File(s)' button is present on 'Upload Files' pop up", "[PASSED]",
				"'Add File(s)' button not found [FAILED]");
		HardVerify.NotNull(uploadFilesPopUp.getUploadButton(),
				"Verifying 'Upload' button is present on 'Upload Files' pop up", "[PASSED]",
				"'Upload' button not found [FAILED]");
		HardVerify.NotNull(uploadFilesPopUp.getCancelButton(),
				"Verifying 'Cancel' button is present on 'Upload Files' pop up", "[PASSED]",
				"'Cancel' button not found [FAILED]");
	}

	public void clickUploadFilesPopUpControl(String control) {
		switch (control) {
		case "Add File(s)":
			uploadFilesPopUp.clickAddFileButton();
			break;
		case "Upload":
			uploadFilesPopUp.clickUploadButton();
			uploadFilesPopUp.waitForUploadToFinish();
			break;
		case "Cancel":
			uploadFilesPopUp.clickCancelButton();
			break;
		default:
			Log.logError("Control doesn't match");
			break;
		}
	}

	public void selectAttachment(String filePath) {
		Log.logStep("Selecting attachment file from File Browser...");
		FileUploadHelper.inputFilePath(filePath);
	}

	public void verifyUploadedAttachment(String fileName) {
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		List<String> attachments = attachmentsTab.getFileNamesFromAttachmentTab();
		HardVerify.True(attachments.contains(fileName),
				"Verifying uploaded file: [" + fileName
						+ "] is displayed in the list of attachments in the 'Attachment' tab",
				"[PASSED]", "Uploaded file not displayed in the list of attachments [FAILED]");
	}

	public void verifyDeleteAttachmentButton(String fileName, boolean isPresent) {
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		if (isPresent) {
			HardVerify.NotNull(attachmentsTab.getDeleteAttachmentsButton(fileName),
					"Verifying 'Delete Attachment' button is present for attachment: [" + fileName + "]", "[PASSED]",
					"'Delete Attachment' button not found [FAILED]");
		} else {
			HardVerify.Null(attachmentsTab.getDeleteAttachmentsButton(fileName),
					"Verifying 'Delete Attachment' button is not present for attachment: [" + fileName + "]",
					"[PASSED]", "'Delete Attachment' button found [FAILED]");
		}
	}

	private int number_of_windows_before_click;

	public void clickAttachmentLinkFor(String fileName) {
		number_of_windows_before_click = Browser.getDriver().getWindowHandles().size();
		attachmentsTab.clickAttachmentLinkFor(fileName);
	}

	public void verifyAttachmentIsOpened(boolean isOpened) {
		if (isOpened) {
			HardVerify.Equals(number_of_windows_before_click + 1, Browser.getDriver().getWindowHandles().size(),
					"Verifying attachment is displayed on a popup window", "[PASSED]",
					"The number of browser windows did not increase by one. Number should have been ["
							+ (number_of_windows_before_click + 1) + "] but was ["
							+ Browser.getDriver().getWindowHandles().size() + "]. [FAILED]");
		} else {
			HardVerify.Equals(number_of_windows_before_click, Browser.getDriver().getWindowHandles().size(),
					"Verifying attachment is not displayed", "[PASSED]",
					"The number of browser windows did increase by one. Number should have been ["
							+ (number_of_windows_before_click) + "] but was ["
							+ Browser.getDriver().getWindowHandles().size() + "]. [FAILED]");
		}
	}

	public void closeAttachmentPopUp() {
		Log.logStep("Closing the attachment popup window...");
		BrowserPDFViewerHelper.close();
	}

	private ConfirmPopUp confirmPopUp;

	public void clickDeleteAttachmentFor(String fileName) {
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
		Log.logStep("Clicking on the delete attachment control for file: [" + fileName + "]");
		confirmPopUp = attachmentsTab.clickDeleteAttachmentFor(fileName);
	}

	public void verifyConfirmDialogIsOpened(boolean isOpened) {
		if (isOpened) {
			HardVerify.True(confirmPopUp.isOpen(), "Verifying 'Confirm' dialog is displayed...", "[PASSED]",
					"Confirm popup found not opened [FAILED]");
		} else {
			HardVerify.False(confirmPopUp.isOpen(), "Verifying 'Confirm' dialog is not displayed...", "[PASSED]",
					"Confirm popup found opened [FAILED]");
		}
	}

	public void clickConfirmDialogControl(String control) {
		Log.logStep("Clicking on " + control + " on Confirm popup...");
		switch (control) {
		case "Yes":
			confirmPopUp.clickYesButton();
			break;
		case "No":
			confirmPopUp.clickNoButton();
			break;
		default:
			Log.logError("Control doesn't match");
			break;
		}
	}
}
