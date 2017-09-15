package tests.SubjectMasking;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectSteps;

@Test(groups = "SubjectMasking")
public class SubjectMaskingTests extends AbstractSubjectMask {

	private static final String site = "#SN-dhmsy_1.1";
	private List<Object[]> maskList = new ArrayList<Object[]>();
	private List<String> characterList = Arrays.asList("#", "*", "-", "_", "?", "0", "9", "B", "D", "E", "H", "L",
			 "M", "N", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z", "b", "d", "e", "h", "l", "m", "n", "q",
			"r", "s", "t", "v", "x", "y");

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		steps = new SubjectSteps();
		errorSteps = new ErrorSteps();
		Nav.toURL(adminLink);
		beforeSteps.loginAs(adminLogin, adminPasword);
		Nav.toURL(studyLink);
		custom = maskingSteps.getToCustomTab();
		validStudyCharCount = maskingSteps.checkString(dataList, custom.getScreeningInput());
		afterSteps.logout();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
		afterSteps.logout();
	}

	@BeforeMethod(alwaysRun = true)
	public synchronized void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, "");
	}

	@AfterMethod(alwaysRun = true)
	public synchronized void afterMethod(Method method, ITestResult result) {		
		Log.logTestMethodEnd(method, result);
	}

	@Test(groups = { "SubjectFields",
			"JamNA" }, description = "Check custom field valid/invalid subject fields on subject details page")
	public void subjectFieldsTest() {
		maskingSteps.openNewSubject(site);
		maskingSteps.verifySubjectFieldValidCharacters(dataList, validStudyCharCount);
	}
	
	/**
	 * Returns sites with query as data for given study
	 * 
	 * @return Iterator<Object[]> querySites
	 * 			- sites with queries
	 */
	@DataProvider
	public Iterator<Object[]> maskData() {
		maskList = maskingSteps.generateMask(characterList, 2);
		return maskList.iterator();
	}
	
	@Test(groups = { "MaskValidation",
			"JamaNA" }, description = "Checks generated masks both on Custom page and Subject details page", dataProvider = "maskData")
	public void maskValidationTest(String querySite) {
		
	}
}
