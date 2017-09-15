package tests.Subject;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.utils.Enums.SubjectDetailsFields;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectSteps;

@Test(groups = { "EditSubject", "E-Sign" })
public class EditSubjectTests extends AbstractSubject {

	private String language = "English (US)" ;
	private String languageUpdated = "Spanish (US)" ;
	private final String errorMessageUpdate = "Failed to update subject. Subject with the same screening number already exists.";
    private String field = "Initials";
    List<String> subjectDetailsFieldsList = new ArrayList<>();
    List<String> newValues = new ArrayList<>();

    SubjectSteps steps = new SubjectSteps();
    ErrorSteps errorSteps = new ErrorSteps();

	@BeforeClass(alwaysRun = true)
	@Parameters({ "envTarget" })
	public void beforeClass(String envTarget) {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		if (envTarget.equalsIgnoreCase("at")) {
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

	@Test(groups = { "EditExistingSubject",
			"SFB-TC-103" }, dependsOnGroups = "AddNewSubject", description = "Check edit existing subject, validation of duplicate & required fields")
	public void editExistingSubjectTest() {
		steps.openSubjectDetailsAndVerify(subjectName);
		
//		steps.subjectDetailsVerification(subjectName);
		
		steps.clickOnEditSubjectAndVerify();
		
//		steps.subjectEditableVerification();
		
		steps.editSubjectFieldValueAndVerify(SubjectDetailsFields.SCREENING.getValue(), subjectNameUpdated);
		steps.editSubjectFieldValueAndVerify(SubjectDetailsFields.LANGUAGE.getValue(), languageUpdated);
		
		steps.consentDropdownVerification(true); // Release 2016.2
		
		steps.invalidSymbolsInFieldVerification(field);
		steps.maxLengthFieldVerification(field, "4");
		steps.validSymbolsInFieldVerification(field);
		
		steps.cancelChangesAndVerify(subjectName, language);
		
//		steps.subjectFieldValueVerification(SubjectDetailsFields.SCREENING, subjectName);
//		steps.subjectFieldValueVerification(SubjectDetailsFields.LANGUAGE, language);
		
		steps.clickOnEditSubjectAndVerify();
		
		steps.clearFieldAndVerifyIsRequired(SubjectDetailsFields.SCREENING.getValue());

		/*subjectDetailsFieldsList.add(SubjectDetailsFields.SCREENING.getValue());
		subjectDetailsFieldsList.add(SubjectDetailsFields.LANGUAGE.getValue());
		newValues.add(subjectNameUpdated);
		newValues.add(languageUpdated);*/
		
		steps.saveEditedSubjectAndVerify(subjectNameUpdated, languageUpdated, adminLogin, adminPasword);
		
//		steps.editSubject(subjectName, subjectDetailsFieldsList, newValues, true, adminLogin, adminPasword);

		/*steps.subjectFieldValueVerification(SubjectDetailsFields.SCREENING, subjectNameUpdated);
		steps.subjectFieldValueVerification(SubjectDetailsFields.LANGUAGE, languageUpdated);*/
				
		steps.returnToAllSubjects();
		steps.openSubjectDetails(subjectNameUpdated);

		subjectDetailsFieldsList.add(SubjectDetailsFields.SCREENING.getValue());
		newValues.add("DuplicateSubject");
		
		steps.editSubject(subjectNameUpdated, subjectDetailsFieldsList, newValues, true, adminLogin, adminPasword);
			
		errorSteps.errorContainerVerification(errorMessageUpdate);
		errorSteps.closeError();
	}
}
