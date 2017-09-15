package tests.SubjectVisitAttachment;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * Manage and view attachments on the Visit Details page
 * 
 * Jama ID: SFB-TC-1663
 * 
 * Release: 2016.3
 * 
 * Test Objective(s):
 *	- Users with the claim "canManageAttachments" can manage (upload/delete) a document via the Portal.
 *	- Users without claim "canManageAttachments" can't manage (upload/delete) a document via the Portal.
 *	- Users with the claim "canViewAttachments" can view attachments.
 *	- For each uploaded file the attachment count is increased by "1".
 *	- File(s) added at subject level can be viewed and deleted from visit details page.
 * 
 * Prerequisite(s):
 * 	1. At least 1 Medavante user having claim "canManageAttachments" (Site Rater Type 4) exists.
 * 	2. At least 1 Medavante user having claim "canViewAttachments"
 * 		and without claim "canManageAttachments" (Site Rater Type 1) exists.
 * 	3. At least 1 Study has been configured.
 * 	4. At least 1 Site has been configured for the Study(Pr#3).
 * 	5. At least 1 Subject exists under the site(Pr#4).
 * 	6. At least 1 pending visit is available for the subject(Pr#5).
 * 	7. At least 1 file has been attached from Subject(Pr#5) details page  
 * 
 * Action & Expected Result:
 *	1. Login to Site Portal as the User (Pr#2) and navigate to Visit details page (Pr#6)
 *		- User login is successful.
 *		- Visit details page is displayed.
 *		- Attachment Tab is found
 *		- Attachment count is visible on the tab header and is "1"
 * 		
 *	2. Select the attachment tab
 * 		- Add and delete attachment controls are not available
 * 		- The attachment (Pr#7) is listed out
 * 
 *	3. Click on the attachment file name(Pr#7)
 * 		- Attachment is displayed on a popup window
 * 
 *	4. Close the popup window
 * 		- The attachment file display is closed
 * 
 * 	5. Login to Site Portal as the User (Pr#1) and navigate to Visit(Pr#6) details page
 * 		- User login is successful.
 * 		- Visit details page of the selected visit is displayed.
 * 		- Attachment Tab is found and is not selected by default
 * 	
 * 	6. Select the Attachment tab
 * 		- Attachment tab is visible
 * 		- Attachment count is visible on the tab header and is shown as "1" 
 * 		- Add attachment control is found
 * 		- Attachment list contains 1 file(Pr#7)
 * 		- Delete attachment control is found
 * 
 * 	7. Click on the file attached on subject details page (Pr#7)
 * 		- File is opened on a popup window
 * 
 * 	8. Close the popup window
 * 		- File display is closed
 * 
 * 	9. Upload a file using the add attachment control
 * 		- Attachment is added and uploaded successfully. 
 * 		- Uploaded file name is displayed in the list of attachments on the 'Attachment' tab 
 * 		- A delete control is available for each attachment 
 * 		- New attachment count = attachment count of Step#6 + 1
 * 
 * 	10. Open the attachment of Step#9
 * 		- Attachment is displayed on a popup window
 * 
 * 	11. Close the attachment.
 * 		- Attachment is closed
 * 
 * 	12. Delete all the attachments of this visit.
 * 		- All attachments are removed successfully.
 * 		- Attachment count is no longer displayed on the tab header
 */
@Test(groups = "VisitDetailsAttachment")
public class VisitDetailsAttachmentTest extends AbstractSubjectVisitAttachment {
	
	/**
	 * Before every test, log the method that is being run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		if (method.getName().equalsIgnoreCase("visitDetailsCanViewAttachmentsTest")) {
			beforeSteps.loginAndOpenAllVisits(siteportalUserAccountName, siteportalUserAccountPassword, study, site);
		} else {
			beforeSteps.loginAndOpenAllVisits(adminLogin, adminPasword, study, site);
		}
		steps.openVisitDetails(subjectName, visitName);
		steps.visitDetailsVerification(visitName);
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
		if (method.getName().equalsIgnoreCase("visitDetailsCanViewAttachmentsTest"))
			afterSteps.logout();
		Log.logTestMethodEnd(method, result);
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		steps.getToStudyDashboardViaStudyLink();
		steps.openAllSubjects();
		steps.openSubjectDetails(subjectName);
		subjectSteps.selectVisitInPosition(visitName, 0);
		subjectSteps.unScheduleVisit();
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
	
	@Test(groups = { "VisitDetailsCanViewAttachments",
			"SFB-TC-1663" }, dependsOnGroups = "SubjectDetailsCanViewAttachments", description = "View attachments on Visit details page")
	public void visitDetailsCanViewAttachmentsTest() {
		steps.verifyAttachmentTabIsPresent(true);
		steps.verifyAttachmentTabIsSelected(false);
		steps.verifyTabHeaderAttachmentCount(2);

		steps.clickAttachmentsTabLink();
		steps.verifyUploadAttachmentButtonIsPresent(false);
		steps.verifyDeleteAttachmentButton(fileName[1], false);
		steps.verifyDeleteAttachmentButton(fileName[2], false);

		steps.clickAttachmentLinkFor(fileName[1]);
		steps.verifyAttachmentIsOpened(true);

		steps.closeAttachmentPopUp();
		steps.verifyAttachmentIsOpened(false);
	}

	@Test(groups = { "VisitDetailsCanManageAttachments",
			"SFB-TC-1663" }, dependsOnGroups = "VisitDetailsCanViewAttachments", description = "Manage and view attachments on Visit details page")
	public void visitDetailsCanManageAttachmentsTest() {
		steps.verifyAttachmentTabIsPresent(true);
		steps.verifyAttachmentTabIsSelected(false);

		steps.clickAttachmentsTabLink();
		steps.verifyAttachmentTabIsSelected(true);
		steps.verifyTabHeaderAttachmentCount(2);
		steps.verifyUploadAttachmentButtonIsPresent(true);
		steps.verifyDeleteAttachmentButton(fileName[1], true);
		steps.verifyDeleteAttachmentButton(fileName[2], true);

		steps.clickAttachmentLinkFor(fileName[1]);
		steps.verifyAttachmentIsOpened(true);

		steps.closeAttachmentPopUp();
		steps.verifyAttachmentIsOpened(false);

		steps.clickAddAttachmentButton();
		steps.clickUploadFilesPopUpControl("Add File(s)");
		steps.selectAttachment(filePath + fileName[0]);
		steps.clickUploadFilesPopUpControl("Upload");
		steps.visitDetailsVerification(visitName);
		steps.verifyUploadedAttachment(fileName[0]);
		steps.verifyDeleteAttachmentButton(fileName[0], true);
		steps.verifyDeleteAttachmentButton(fileName[1], true);
		steps.verifyDeleteAttachmentButton(fileName[2], true);
		steps.verifyTabHeaderAttachmentCount(3);

		steps.clickAttachmentLinkFor(fileName[0]);
		steps.verifyAttachmentIsOpened(true);

		steps.closeAttachmentPopUp();
		steps.verifyAttachmentIsOpened(false);

		steps.clickDeleteAttachmentFor(fileName[0]);
		steps.clickConfirmDialogControl("Yes");
		steps.clickDeleteAttachmentFor(fileName[1]);
		steps.clickConfirmDialogControl("Yes");
		steps.clickDeleteAttachmentFor(fileName[2]);
		steps.clickConfirmDialogControl("Yes");
		steps.verifyTabHeaderAttachmentCount(0);
	}
}

