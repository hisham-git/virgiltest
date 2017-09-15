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

@Test(groups = "SitePortalPrimaryAddress")
public class SitePortalPrimaryAddressTests extends AbstractPrimaryAddress{
	
	@Factory(dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public static Object[] sitePortalPrimaryAddress(Map<String, String> configData) {
		return new Object[] { new SitePortalPrimaryAddressTests(configData) };
	}
	
	public SitePortalPrimaryAddressTests(Map<String, String> configData) {
		login = configData.get("UserLogin");
		pass = configData.get("UserPass");
		study = configData.get("Study");
		site = configData.get("Site");
		subjectName = configData.get("Subject");
		showErrorMessage = configData.get("ErrorMessage");
	}
	
	@BeforeClass(alwaysRun = true)
	public void  beforeClass() {
		Log.logTestClassStart(this.getClass());
		subjectSteps = new SubjectSteps();
		crSteps = new CentralRatingSteps();
		errorSteps = new ErrorSteps();
		beforeSteps = new BeforeSteps();
		
		Log.logInfo("Navigating to Login page...");
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAndOpenAllSubjects(login, pass, study, site);
		subjectSteps.openSubjectDetails(subjectName);
	}
	
	@Test(groups = { "SitePortalSelfCrVisit",
			"SFB-TC-1890" }, description = "Check appearence of error message for Self-CR appointments if user has no/deactivated/activated primary address", dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public void sitePortalSelfCrVisitTest(Map<String, String> testData) {
		subjectSteps.selectVisitByName(testData.get("VisitType"), testData.get("VisitName"));
		subjectSteps.selectVisitAction(testData.get("VisitType"), testData.get("Action"));
		subjectSteps.addUnscheduledVisit(testData.get("VisitType"), testData.get("CRType"), testData.get("VisitName"));

		errorSteps.errorContainerVerification(showErrorMessage, errorMsg, testData.get("CRType"));
	}

	@Test(groups = { "SitePortalNonCrVisit",
			"SFB-TC-1890" }, description = "Check non-appearence of error message for Non-CR appointments if user has no/deactivated/activated primary address", dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public void sitePortalNonCrVisitTest(Map<String, String> testData) {
		subjectSteps.selectVisitByName(testData.get("VisitType"), testData.get("VisitName"));
		subjectSteps.selectVisitAction(testData.get("VisitType"), testData.get("Action"));
		subjectSteps.addUnscheduledVisit(testData.get("VisitType"), testData.get("CRType"), testData.get("VisitName"));

		errorSteps.errorContainerVerification("FALSE", errorMsg, testData.get("CRType"));

		subjectSteps.revertVisitStatus(testData.get("VisitType"), testData.get("VisitName"),
				testData.get("RevertAction"), login, pass);
	}
}