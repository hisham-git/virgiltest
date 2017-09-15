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

@Test(groups = "MaskingValidation")
public class MaskingValidationTests extends AbstractSubjectMask {

	private static final String site = "ABC-dhmsy1";
	private List<Object[]> maskList = new ArrayList<Object[]>();
	/*private List<String> characterList = Arrays.asList("#", "\\", "*", "-", "_", "?", "0", "9", "B", "D", "E", "H", "L",
			"M", "N", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z", "b", "d", "e", "h", "l", "m", "n", "q", "r", "s",
			"t", "v", "x", "y");*/
//	private List<String> maskCharacterList = Arrays.asList("#", "\\#", "?", "L", "\\L", "SN", "*", "-", "_", "h", "m",
//			"d", "s", "y");
	
	private List<String> maskCharacterList = Arrays.asList("#", "?", "L", "SN");

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		steps = new SubjectSteps();
		errorSteps = new ErrorSteps();
		maskingSteps = new SubjectMaskingSteps();
		Nav.toURL(sitePortalUrl);
		beforeSteps.loginAndChooseStudy(maportalUserAccountName, maportalUserAccountPassword, study);
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
	public Iterator<Object[]> maskData() {
		maskList = maskingSteps.generateMask(maskCharacterList, 3);
		return maskList.iterator();
	}
	
	/*@DataProvider
	public static Object[][] maskData() {
		return new Object[][] { { "?\\N" }, { "?Lh" }, { "?SN" }, { "?lL" }, { "?dy" }, { "?hN" }, { "?ml" },
				{ "#\\y" }, { "#?h" }, { "#LS" }, { "#LN" }, { "#SN" }, { "LSN" }, { "SN?" }, { "SN#" }, { "SNL" },
				{ "l##" }, { "l#L" }, { "l#S" }, { "lLl" }, { "ys?" }, { "ys#" }, { "ysL" }, };
	}*/
	
	@Test(groups = { "ValidateAdminMaskOnSubjectDetailsTest",
			"JamaNA" }, description = "Checks generated masks both on Custom page and Subject details page", dataProvider = "maskData")
	public void validateAdminMaskOnSubjectDetailsTest(String mask) {
		Nav.toURL(studyLink);
		maskingSteps.getToCustomTab();
		maskingSteps.saveMask(mask);
		Nav.toURL(newSubjectLink);
		maskingSteps.verifyMaskWithPartialCoverage(mask, site);
	}
}
