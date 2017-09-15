package tests.SubjectStatus;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Tests.AssessmentSteps;
import steps.Tests.SubjectSteps;
import steps.Tests.TemplateSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "SubjectStatus", "MultiEnvironment", "Sanity" })
public abstract class AbstractSubjectStatus extends AbstractTest {
	
	// ------------------------------------------------------------------------------------------------------------------------
	protected static final String study = "Auto - Subject";
	protected static final String site = "Site_1 - Francis Gibbes";
	protected static final String studyNameDB = "SubjectStatusLabel";
	
	SubjectSteps subjectSteps = new SubjectSteps();
	AssessmentSteps assessmentSteps =  new AssessmentSteps();
	TemplateSteps templateSteps = new TemplateSteps();
	
	// ------------------------------------------------------------------------------------------------------------------------
	/**
	 * Before starting every Class log the ClassName
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
	}
	
	/**
	 * Before every test, log the method that is being run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
	}
	
	/**
	 * After every test, Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		Log.logTestMethodEnd(method, result);
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
