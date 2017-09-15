package tests.AssessmentDetails;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.helpers.BrowserPDFViewerHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;

/**
 * Test Case : SFB-TC-607
 * Test Attachment File Size
 * and
 * Delete Attachment Test
 * 
 * NOTE : The file kept here is not 8GB, which violates the test case prerequisites
 * 
 * @author Abdullah Al Hisham
 */
@Test(groups = { "Attachment" })
public class AttachmentTests extends AbstractAssessmentDetails {

	/*
	 * Attributes used in the Test Case Class
	 */
	private final String fileName = "file.pdf";
	private final String filePath = System.getProperty("user.dir") + "\\src\\test\\java\\tests\\assessmentDetails\\"
			+ fileName;
	
	/*
	 * Configuration Variables
	 */
//	private final String ASSESSMENT_NAME = "ECOgPro", SVID = "139";
	private final int attachmentsIndex = 0; //The index of the file to be deleted
	

	private static String visitName = "Visit1";
	private static String assessmentName = "CDR";

	/**
	 * Before Test class 
	 * 1. Go to assessment list
	 * 2. click an assessment with given name & svid
	 * 3. After assessment detail page is opened, click Attachments tab 
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName,
				siteName);
		// commonSteps.getToStudyDashboard();
		detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
		detailsSteps.clickAttachmentsTabLink();
	}

	/**
	 * Test to check if the Attachment Upload and File Size works correctly
	 *
	 * Steps :
	 * 	1. Click Add Upload Files Button
	 * 	2. Click Add File from the Upload Pop Up
	 * 	3. Paste the correct Filepath and File name to the File Browser
	 * 	4. Press Enter
	 * 	5. Check if the Attachments count increased by 1
	 * 	6. Check if the File is available in the Attachments tab
	 */
	@Test(groups = { "AddAttachment",
			"SFB-TC-607" }, description = "Check if File Attachments for Completed/Edited Assessments work correctly")
	public void addAttachmentTest() {
		detailsSteps.getAttachmentsCount();
		detailsSteps.uploadAttachment(filePath);
		detailsSteps.verifyUploadedAttachment();
	}
	
	
	/**
	 * Test to check that the file can be viewed in a different window
	 * 
	 * Steps : 
	 * 	1. Click on an Attachments Link 
	 * 	2. Check that a new tab is opened
	 */
	@Test(groups = { "ViewAttachment",
			"SFB-TC-607" }, description = "Test if the Attachments can be viewed.", dependsOnGroups = {
					"AddAttachment" })
	public void viewAttachmentTest() {
		detailsSteps.clickAttachmentAtIndex(attachmentsIndex);
		detailsSteps.verifyAttachmentIsOpened();
	}
	
	/**
	 * Checks that the Attachments Delete button works correctly
	 * 
	 * Steps - 
	 * 	1. Click on the Delete Button for a pre-configured File Attachment
	 * 	2. Check that the Number of Attachments has decreased by one by counting the number of Attachments in the Tab
	 * 	3. Check that the Number of Attachments has decreased by one by looking at the Attachments Header Count.
	 */
	@Test(groups = { "DeleteAttachment",
			"SFB-TC-607" }, description = "Test if the Attachments Delete button and functionality works correctly.", dependsOnGroups = {
					"ViewAttachment" })
	public void deleteAttachmentTest() {
		detailsSteps.getAttachmentsCount();
		detailsSteps.deleteAttachment(attachmentsIndex);
		detailsSteps.verifyAttachmentDeleted();
	}
	
	/**
	 * Log that the Method was finished
	 * @param method
	 */
	@Override
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result){
		BrowserPDFViewerHelper.close();
		Log.logTestMethodEnd(method, result);
	}
	
}
