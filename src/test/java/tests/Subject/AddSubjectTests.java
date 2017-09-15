package tests.Subject;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import hu.siteportal.pdfreport.PdfLog;
import hu.siteportal.pdfreport.PdfLogging;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectSteps;

@Test(groups = "AddSubject")
public class AddSubjectTests extends AbstractSubject {
	
	private static final String expectedErrorMessage = "Failed to create subject. Subject with the same screening number already exists.";
    private static final String site = "Site_2 - Bill Miles";
    private static final String field = "Initials";
	private static final String comments = "Comments for: " + subjectName;

	@BeforeClass(alwaysRun = true)
	@Parameters({ "envTarget" })
	public void beforeClass(String envTarget) {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		steps = new SubjectSteps();
		errorSteps = new ErrorSteps();
		pdfLog = new PdfLog();
		
		if (envTarget.equalsIgnoreCase("at")) {
			dbSteps.deleteSubject(subjectName);
			dbSteps.deleteSubject(subjectNameUpdated);
			dbSteps.insertStudySubjectField(adminLogin, studyNameDB, field);
		}
		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, "");
	}

	@AfterClass(alwaysRun = true)
	@Parameters({ "envTarget" })
	public void afterClass(String envTarget) {
		if (envTarget.equalsIgnoreCase("at")) {
			dbSteps.deleteStudySubjectField();
		}
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
	
	@Test(groups = { "AddNewSubject",
			"SFB-TC-97" }, description = "Check add new subject for the site, validation of duplicate & required fields")
	public void addNewSubjectTest() {
		steps.addNewSubjectButtonVerification();

		steps.openNewSubjectFormAndVerify(site);

		// steps.requiredFieldsVerification(true);

		steps.fillNotAllRequiredFields(subjectName);

		steps.invalidSymbolsInFieldVerification(field);
		steps.maxLengthFieldVerification(field, "4");
		steps.validSymbolsInFieldVerification(field);
		steps.commentsFieldVerification(comments); // Release 2016.2
		steps.cancelButtonActionVerification();
		
		steps.addSubjectToSite(subjectName, site);
		steps.subjectDetailsVerification(subjectName);
		steps.returnToAllSubjects();
		steps.addSubjectToSite(subjectName, site);
		errorSteps.errorContainerVerification(expectedErrorMessage);
		errorSteps.closeError();
	}
}
