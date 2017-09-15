package tests.CentralRating;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Tests.CentralRatingSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "CentralRating" })
public abstract class AbstractCentralRating extends AbstractTest {
	
	// ------------------------------------------------------------------------------------------------------------------------
	protected static String adminLogin = "testma13";
	protected static String adminPasword = "#2sftest";
	protected static final String study = "Auto - CR Study";
	protected static final String site = "1 - MST Investigator";
	
	protected static final String comment = "Test Comment";
	protected static final String alias = "Test Alias";
	
	protected static final String subjectName = "TestNotificationDate";
	protected static final String[] visitName = { "Visit1", "Visit2", "Visit3" };
	protected static final int[] visitPos = { 0, 1, 2 };
	protected static final int dateSpan = 14;
	
	protected static final List<String> clinicians = Arrays.asList("MST MA Clinician1", "MST MA Clinician2", "MST MA Clinician3");
	protected static final String clinician = clinicians.get(0);
	protected static final String newClinician = clinicians.get(1);
	protected static final String userTimeZone = "Europe/Helsinki";
	
	protected static Date currentDateTime, randCurrentDateTime, futureDateTime, tzDateTime, ntfDateTime;
	protected static DateFormat dateFormat, timeFormat;
	protected static Calendar cal;

	protected static String[] splitDate, splitTime;
	protected static String formattedDate, formattedTime, nMonth, nDate, nYear, sMonth, sDate, sYear, hour, min, amPm;
	
	protected SubjectSteps subjectSteps;
	protected CentralRatingSteps crSteps;
	ErrorSteps errorSteps;
	
	// ------------------------------------------------------------------------------------------------------------------------
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
}