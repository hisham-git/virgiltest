package tests.Observer;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.tools.Log;
import steps.Tests.ObserverSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

public abstract class AbstractObserver extends AbstractTest {
	
	/*protected String subjectName = "WithoutObservers";
	protected String subjectName2 = "WithObservers";
	protected String study = "Auto - ProObsro";
	protected String site = "Site_1 - Francis Gibbes";
	protected String relation = "Max";
	protected String alias = "Tkachov";
	protected String visitName = "VisitProObsro1";
	protected String relation1 = "Existing";
	protected String alias1 = "Observer";*/
	protected String subjectName = "WithoutObservers";
	protected String subjectName2 = "WithObservers";
	protected String study = "Auto - SubjectObserver";
	protected String site = "Site_1 - Francis Gibbes";
	protected String relation = "Max";
	protected String alias = "Tkachov";
	protected String visitName = "Visit 1";
	protected String relation1 = "Existing";
	protected String alias1 = "Observer";
    ObserverSteps steps = new ObserverSteps();
    SubjectSteps subjectSteps = new SubjectSteps();
    
	/**
	 * Before all the Tests, login as Site Rater to the SitePortal
	 * 
	 * @param testContext
	 */
	/*@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext testContext) {
		Log.logInfo("[Beginning New Test] => " + testContext.getName());
		Browser.getDriver().manage().deleteAllCookies();
		beforeSteps.loginAs(username, password);
		pptSteps.chooseStudy(studyName);
		pptSteps.chooseAllSites();
	}*/
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
	}
	
	/**
	 * After all the tests of a class has run, log the name of the class that
	 * has been completed
	 */
    @AfterClass(alwaysRun = true)
    public void afterClass(){
        afterSteps.logout();
        Log.logTestClassEnd(this.getClass());
    }
}
