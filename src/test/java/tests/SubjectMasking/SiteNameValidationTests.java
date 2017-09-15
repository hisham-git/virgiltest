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
import steps.Tests.SubjectMaskingSteps;
import steps.Tests.SubjectSteps;

@Test(groups = "SiteNameValidation")
public class SiteNameValidationTests extends AbstractSubjectMask {
	private List<Object[]> maskList = new ArrayList<Object[]>();
	/*private List<String> characterList = Arrays.asList("#", "\\", "*", "-", "_", "?", "0", "9", "B", "D", "E", "H", "L",
			"M", "N", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z", "b", "d", "e", "h", "l", "m", "n", "q", "r", "s",
			"t", "v", "x", "y");*/
	private List<String> siteCharacterList = Arrays.asList("\\", "?", "#", "L", "S", "N", "l", "d", "h", "m", "s", "y");

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		steps = new SubjectSteps();
		errorSteps = new ErrorSteps();
		maskingSteps = new SubjectMaskingSteps();
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAs(maportalUserAccountName, maportalUserAccountPassword);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
		afterSteps.logout();
	}

	@BeforeMethod(alwaysRun = true)
	public synchronized void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		
	}

	@AfterMethod(alwaysRun = true)
	public synchronized void afterMethod(Method method, ITestResult result) {		
		Log.logTestMethodEnd(method, result);
	}
	
	/**
	 * Returns sites with query as data for given study
	 * 
	 * @return Iterator<Object[]> querySites
	 * 			- sites with queries
	 */
	@DataProvider
	public Iterator<Object[]> siteData() {
		maskList = maskingSteps.generateSiteData(siteCharacterList, 1);
		return maskList.iterator();
	}
	
	@Test(groups = { "ValidateSiteData",
			"JamaNA" }, description = "Checks generated masks both on Custom page and Subject details page", dataProvider = "siteData")
	public void validateSiteDataTest(String siteId) {
		Nav.toURL(studyLink);
		maskingSteps.getToSitesTab();
		maskingSteps.verifySitedata(siteId);
		maskingSteps.saveSiteId(siteId);

		for (Object mask[] : maskingSteps.generateMask(siteCharacterList, 1)) {
			String data = "SN" + mask[0].toString();
			maskingSteps.getToCustomTab();
			maskingSteps.saveMask(data);
			Nav.toURL(newSubjectLink);
			maskingSteps.verifyMaskWithPartialCoverage(data, siteId);
			Nav.toURL(studyLink);
		}
	}
}
