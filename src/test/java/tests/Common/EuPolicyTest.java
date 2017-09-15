/**
 * @author ubiswas
 */
package tests.Common;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Tests.EuPolicySteps;

@Test(groups = { "EuPolicy" })
public class EuPolicyTest extends AbstractCommon{
	
	EuPolicySteps euPolicyStep;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Nav.toURL(sitePortalUrl);
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		euPolicyStep = new EuPolicySteps();
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		afterSteps.logout();
		Log.logTestMethodEnd(method, result);
	}

	@DataProvider
	public Object[][] LoginCredentials() {
		return new Object[][] {
			{ adminLogin, adminPasword },
			{ siteportalUserAccountName, siteportalUserAccountPassword }
		};
	}

	@Test(groups = { "HeaderEuPolicy",
			"JamaNA" }, description = "Check users with different roles & claims can access the MedAvante Privacy Policy via portal header menu link", dataProvider = "LoginCredentials")
	public void euPolicyHeaderTest(String userName, String password) {
		beforeSteps.loginAs(userName, password);
		euPolicyStep.homePrivacyPolicyVerificationFromHeader();
		euPolicyStep.studyPrivacyPolicyVerificationFromHeader();
		euPolicyStep.administrationPrivacyPolicyVerificationFromHeader();

	}

	@Test(groups = { "FooterEuPolicy",
			"JamaNA" }, description = "Check users with different roles & claims can access the MedAvante Privacy Policy via portal footer menu link", dataProvider = "LoginCredentials")
	public void euPolicyFooterTest(String userName, String password) {
		beforeSteps.loginAs(userName, password);
		euPolicyStep.homePrivacyPolicyVerificationFromFooter();
		euPolicyStep.studyPrivacyPolicyVerificationFromFooter();
		euPolicyStep.administrationPrivacyPolicyVerificationFromFooter();

	}
}
