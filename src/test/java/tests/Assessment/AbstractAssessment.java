package tests.Assessment;
/**
 * Author- Uttam
 * Description: Setting prerequisite for package: tests.Assessment, two subject creation
 */

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.EsignReasonFields;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Configuration.CommonSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "Assessment" })
public abstract class AbstractAssessment extends AbstractTest{
	 protected String study = "Auto - ProObsro";
	 protected String site = "Site_1 - Francis Gibbes";
	 protected String dateTime;
	 protected static String subjectName = "SUB#";
	 private String suffix = "-" + CommonSteps.generateRandomNames(2);
	 protected String dropDownValue = EsignReasonFields.TECHNICAL.getValue();
	 
	 SubjectSteps subjectStep = new SubjectSteps();
	 
	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
		Log.logInfo("Navigating to Login page...");
		Nav.toURL(URLsHolder.getHolder().getSiteportalURL());
		
		dateTime = this.getCurrentDateTime();
		subjectName += dateTime + suffix;
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(adminLogin, adminPasword, study, site);
		subjectStep.openAllSubjects();
		subjectStep.addSubjectToSite(subjectName, site);
		subjectStep.returnToAllSubjects();
		subjectStep.addSubjectToSite(subjectName+"_2", site);
		afterSteps.logout();
	}
	
	private String getCurrentDateTime() {
		// TODO Auto-generated method stub
		return new SimpleDateFormat("yyyyMMddss").format(Calendar.getInstance().getTime());
	}

}
