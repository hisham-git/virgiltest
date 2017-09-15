package tests.SubjectFields;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.AssessmentSteps;
import steps.Tests.SubjectSteps;
import steps.Tests.TemplateSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "SubjectFields", "MultiEnvironment", "Sanity" })
public abstract class AbstractSubjectFields extends AbstractTest {
	
	// ------------------------------------------------------------------------------------------------------------------------
	protected final static String study = "Auto - SubjectTemporaryID";
	protected final static String site = "Site_1 - Francis Gibbes";
	protected final static String visitName = "Visit 1";
	protected final static int visitPos = 0;

	protected final static DateFormat subjectFormat = new SimpleDateFormat("MMddyy");
	protected final static String formattedDate = subjectFormat.format(new Date());
	protected final static String suffix = "-" + CommonSteps.generateRandomNames(2);
	protected final static String subjectName = "SubjectFieldsTest#" + formattedDate + suffix;
	protected final static String subjectNameUpdated = subjectName + "-UPD";
	
	protected SubjectSteps steps = new SubjectSteps();
	protected AssessmentSteps assessmentSteps =  new AssessmentSteps();
	protected TemplateSteps templateSteps = new TemplateSteps();
	
	// ------------------------------------------------------------------------------------------------------------------------
	
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
}
