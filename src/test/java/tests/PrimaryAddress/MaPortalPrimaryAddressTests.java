package tests.PrimaryAddress;

import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.utils.dataProvider.ExcelDataMapper;
import steps.Configuration.BeforeSteps;
import steps.Tests.CentralRatingSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectSteps;

@Test(groups = "MaPortalPrimaryAddress")
public class MaPortalPrimaryAddressTests extends AbstractPrimaryAddress{
	
	@Factory(dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public static Object[] maPortalPrimaryAddress(Map<String, String> configData) {
		return new Object[] { new MaPortalPrimaryAddressTests(configData) };
	}
	
	public MaPortalPrimaryAddressTests(Map<String, String> configData) {
		login = configData.get("UserLogin");
		pass = configData.get("UserPass");
		study = configData.get("Study");
		site = configData.get("Site");
		subjectName = configData.get("Subject");
		showErrorMessage = configData.get("ErrorMessage");
	}
	
	@BeforeClass(alwaysRun = true)
	public synchronized void  beforeClass() {
		Log.logTestClassStart(this.getClass());
		subjectSteps = new SubjectSteps();
		crSteps = new CentralRatingSteps();
		errorSteps = new ErrorSteps();
		beforeSteps = new BeforeSteps();
		
		Log.logInfo("Navigating to Login page...");
		Nav.toURL(maPortalUrl);
		beforeSteps.loginAndOpenAllSubjects(login, pass, study, site);
		subjectSteps.openSubjectDetails(subjectName);
	}

	@Test(groups = { "MaPortalCrVisit",
			"SFB-TC-1890" }, description = "Check appearence of error message for CR appointments if user has no/deactivated/activated primary address", dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public void maPortalCrVisitTest(Map<String, String> testData) {
		subjectSteps.selectVisitByName(testData.get("VisitType"), testData.get("VisitName"));
		subjectSteps.selectVisitAction(testData.get("VisitType"), testData.get("Action"));
		subjectSteps.addUnscheduledVisit(testData.get("VisitType"), testData.get("CRType"), testData.get("VisitName"));

		errorSteps.errorContainerVerification(showErrorMessage, errorMsg, testData.get("CRType"));
	}
}