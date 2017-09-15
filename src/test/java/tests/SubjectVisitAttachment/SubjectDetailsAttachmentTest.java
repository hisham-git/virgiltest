package tests.SubjectVisitAttachment;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * Manage and view attachments on Subject details page
 * 
 * Jama ID: SFB-TC-1661
 * 
 * Release: 2016.3
 * 
 * Test Objective(s):
 *	- Users with the claim "canManageAttachments" can manage (upload/delete) a document via the Portal.
 *	- Users without claim "canManageAttachments" can't manage (upload/delete) a document via the Portal.
 *	- Users with the claim "canViewAttachments" can view attachments.
 *	- For each uploaded file the attachment count is increased by "1".
 * 
 * Prerequisite(s):
 * 	1. At least 1 Medavante user having claim "canManageAttachments" (Site Rater Type 4) exists.
 * 	2. At least 1 Medavante user having claim "canViewAttachments"
 * 		and without claim "canManageAttachments" (Site Rater Type 1) exists.
 * 	3. At least 1 Study has been configured.
 * 	4. At least 1 Site has been configured for the Study(Pr#3).
 * 	5. At least 1 Subject exists under the site(Pr#4).
 * 	6. At least 1 new visit is available (but not added) for the subject(Pr#5)  
 * 
 * Action & Expected Result:
 *	1. Login to Site Portal as the User (Pr#1) and navigate to Study(Pr#3) Subject(Pr#5) details page
 *		- User login is successful.
 *		- Subject details page is displayed.
 * 		
 *	2. Select the visit(Pr#6)
 * 		- No attachment tab appears
 * 		- Add visit controls is available
 * 
 *	3. Add the visit
 * 		- Attachments tab appears and is not selected
 * 		- No attachment count appears on the tab header
 * 
 *	4. Select the Attachment tab
 * 		- Attachment tab is selected with control to add new attachments
 * 
 * 	5. Click on the add attachment control
 * 		- 'Upload Files' pop up is displayed.
 * 		- Upload, Add files and Cancel controls are found
 * 	
 * 	6. Click on the Add files control
 * 		- File selection dialog window is opened
 * 			and user can select one or multiple files at a time.
 * 
 * 	7. Select and Upload a file
 * 		- Attachment is added and uploaded successfully.
 * 		- Popup is closed automatically and Subject details page is displayed.
 * 		- Uploaded file name is displayed in the list of attachments in the 'Attachment' tab
 * 		- A delete control is available for each attachment
 * 		- The attachment count displayed on the tab header becomes "1"
 * 
 * 	8. Perform Step#5 and click on the Cancel control
 * 		- Popup is closed and no file is uploaded
 * 
 * 	9. Click on the attachment file name of Step#7
 * 		- Attachment is displayed on a popup window
 * 
 * 	10. Close the popup window
 * 		- The attachment file display is closed
 * 
 * 	11. Click on the delete attachment control for the Step#7 file
 * 		- 'Confirm' dialog is displayed with "Yes" and "No" options
 * 
 * 	12. Click on 'No' control
 * 		- The 'Confirm' dialog is closed
 * 			and the attachment is not deleted.
 * 
 * 	13. Click on the delete attachment control for the Step#7 file
 * 		- 'Confirm' dialog is displayed with "Yes" and "No" options
 * 
 *	14. Click on 'Yes' control
 * 		- The 'Confirm' dialog is closed
 * 			and the attachment of Step#7 is removed from the list
 * 
 * 	15. Upload 2 new files
 * 		- New attachment count = attachment count of Step#14 + 2
 * 
 * 	16. Login to Site Portal as the User (Pr#2)
 * 		and navigate to the Subject(Pr#5) details page
 * 		- User login is successful.
 * 		- Subject details page is displayed.
 * 
 * 	17. Select the visit (Pr#6) and select the attachment tab
 * 		- Attachments Tab is found
 * 		- Attachments of Step#15 are listed out
 * 		- Add and Delete attachment controls are not available
 * 
 * 	18. Click on one of the file names of Step#15
 * 		- Attachment is displayed on a popup window
 * 
 * 	19. Close the popup window
 * 		- The attachment file display is closed
 */
@Test(groups = "SubjectDetailsAttachment")
public class SubjectDetailsAttachmentTest extends AbstractSubjectVisitAttachment {
	
	/**
	 * Before every test, log the method that is being run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		if (method.getName().equalsIgnoreCase("subjectDetailsCanManageAttachmentsTest")) {
			beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
		} else {
			beforeSteps.loginAndOpenAllSubjects(siteportalUserAccountName, siteportalUserAccountPassword, study, site);
		}
		steps.openSubjectDetails(subjectName);
		steps.subjectDetailsVerification(subjectName);
//		beforeSteps.focusBrowserWindow();
	}
	
	/**
	 * After every test: 
	 * 1. Return to Study dashboard from first link on breadcrumbs
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		afterSteps.logout();
		Log.logTestMethodEnd(method, result);
	}
	
	@Test(groups = { "SubjectDetailsCanManageAttachments",
			"SFB-TC-1661" }, description = "Manage and view attachments on Subject details page")
	public void subjectDetailsCanManageAttachmentsTest() {
		subjectSteps.selectVisitInPosition(visitName, 0);
		steps.verifyAttachmentTabIsPresent(false);
		steps.verifyAddVisitIsPresent(true);

		subjectSteps.scheduleVisit();
		steps.verifyAttachmentTabIsPresent(true);
		steps.verifyAttachmentTabIsSelected(false);
		steps.verifyTabHeaderAttachmentCount(0);

		steps.clickAttachmentsTabLink();
		steps.verifyUploadAttachmentButtonIsPresent(true);

		steps.clickAddAttachmentButton();
		steps.verifyPopUpIsDisplayed();
		steps.verifyPopUpControls();

		steps.clickUploadFilesPopUpControl("Add File(s)");
		steps.selectAttachment(filePath + fileName[0]);
		steps.clickUploadFilesPopUpControl("Upload");
		steps.subjectDetailsVerification(subjectName);
		steps.verifyUploadedAttachment(fileName[0]);
		steps.verifyDeleteAttachmentButton(fileName[0], true);
		steps.verifyTabHeaderAttachmentCount(1);

		steps.clickAddAttachmentButton();
		steps.verifyPopUpIsDisplayed();
		steps.verifyPopUpControls();
		steps.clickUploadFilesPopUpControl("Cancel");
		steps.subjectDetailsVerification(subjectName);
		steps.verifyTabHeaderAttachmentCount(1);

		steps.clickAttachmentLinkFor(fileName[0]);
		steps.verifyAttachmentIsOpened(true);

		steps.closeAttachmentPopUp();
		steps.verifyAttachmentIsOpened(false);

		steps.clickDeleteAttachmentFor(fileName[0]);
		steps.verifyConfirmDialogIsOpened(true);

		steps.clickConfirmDialogControl("No");
		steps.verifyConfirmDialogIsOpened(false);
		steps.verifyTabHeaderAttachmentCount(1);

		steps.clickDeleteAttachmentFor(fileName[0]);
		steps.verifyConfirmDialogIsOpened(true);

		steps.clickConfirmDialogControl("Yes");
		steps.verifyConfirmDialogIsOpened(false);
		steps.verifyTabHeaderAttachmentCount(0);

		steps.clickAddAttachmentButton();
		steps.verifyPopUpIsDisplayed();
		steps.verifyPopUpControls();
		steps.clickUploadFilesPopUpControl("Add File(s)");
		steps.selectAttachment(filePath + fileName[1]);
		steps.selectAttachment(filePath + fileName[2]);
		steps.clickUploadFilesPopUpControl("Upload");
		steps.subjectDetailsVerification(subjectName);
		steps.verifyTabHeaderAttachmentCount(2);
	}
	
	@Test(groups = { "SubjectDetailsCanViewAttachments",
			"SFB-TC-1661" }, dependsOnGroups = "SubjectDetailsCanManageAttachments", description = "View attachments on Subject details page")
	public void subjectDetailsCanViewAttachmentsTest() {
		subjectSteps.selectVisitInPosition(visitName, 0);
		steps.clickAttachmentsTabLink();
		steps.verifyTabHeaderAttachmentCount(2);
		steps.verifyUploadAttachmentButtonIsPresent(false);
		steps.verifyDeleteAttachmentButton(fileName[1], false);
		steps.verifyDeleteAttachmentButton(fileName[2], false);

		steps.clickAttachmentLinkFor(fileName[1]);
		steps.verifyAttachmentIsOpened(true);

		steps.closeAttachmentPopUp();
		steps.verifyAttachmentIsOpened(false);
	}
}
