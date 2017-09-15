package tests.SubjectForms;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Configuration.CommonSteps;
import steps.Tests.StudyDashboardSteps;
import steps.Tests.SubjectSteps;
import steps.Tests.TemplateSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "SubjectForms", "E-Sign" })
public abstract class AbstractSubjectForms extends AbstractTest {
	
	protected static DateFormat subjectFormat;
	protected static String formattedSubject, suffix, subjectName;
	protected static int scheduledVisitTemplates, notScheduledVisitTemplates;
	
//	protected String subjectName = "DisableProObsro";
	protected static final String study = "Auto - ProObsro";
	protected static final String site = "Site_1 - Francis Gibbes";
	protected static final String scheduledVisitName = "VisitProObsro1";
	protected static final int scheduledVisitPos = 0;
	protected static final String notScheduledVisitName = "VisitProObsro2";
	protected static final int notScheduledVisitPos = 1;
	protected static final String reason = "The form is obsolete";
	protected static final String disabledDate = "Disabled: "+ Constants.DATE_FORMAT.format(new Date());
	protected static final List<String> formTypes = Arrays.asList("PRO", "ObsRO");
    
    SubjectSteps steps = new SubjectSteps();
    TemplateSteps templateSteps = new TemplateSteps();
    SubjectSteps subjectSteps = new SubjectSteps();
	
    /**
	 * Before all the test classes run:  
	 * 1. log the name of the TestNG test
	 * 2. Open new browser window
	 * 3. Navigate to AUT URL
	 * 
	 * @param browser - optional
	 * @param testContext
	 */
	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public void beforeTest(@Optional("chrome") String browser, ITestContext testContext) throws MalformedURLException {
		Log.logTestStart(testContext);
		
		subjectFormat = new SimpleDateFormat("MMddyy");
		formattedSubject = subjectFormat.format(new Date());
		suffix = "-" + CommonSteps.generateRandomNames(2);
		subjectName = "DisableProObsroTest#" + formattedSubject + suffix;
		
		newBrowser = new Browser(browser);
		Log.logInfo("Opening " + browser);
		Log.logInfo("Navigating to Login page...");
		Nav.toURL(sitePortalUrl);
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenAllSubjects(adminLogin, adminPasword, study, site);
		subjectSteps.createNewSubject(subjectName);
		
		subjectSteps.selectVisitInPosition(scheduledVisitName, scheduledVisitPos);
		scheduledVisitTemplates = subjectSteps.getFormsCountForTypes(formTypes);
		subjectSteps.selectVisitInPosition(notScheduledVisitName, notScheduledVisitPos);
		notScheduledVisitTemplates = subjectSteps.getFormsCountForTypes(formTypes);

		afterSteps.logoutByClearingCookie();
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
	}
	
	/**
	 * After all the tests of a class has run, log the name of the class that
	 * has been completed
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logTestClassEnd(this.getClass());
	}
}
