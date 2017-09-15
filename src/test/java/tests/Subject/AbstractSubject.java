package tests.Subject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

import hu.siteportal.pdfreport.PdfLog;
import steps.Configuration.CommonSteps;
import steps.Tests.ErrorSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "Subject", "MultiEnvironment", "Sanity" })
public abstract class AbstractSubject extends AbstractTest {
	
	// ------------------------------------------------------------------------------------------------------------------------
	protected final static String study = "Auto - SubjectStatusLabel";
	protected final static String studyNameDB = "SubjectStatusLabel";

	protected final static DateFormat subjectFormat = new SimpleDateFormat("MMddyy");
	protected final static String formattedDate = subjectFormat.format(new Date());
	protected final static String suffix = "-" + CommonSteps.generateRandomNames(2);
	protected final static String subjectName = "AddSubjectTest#" + formattedDate + suffix;
	protected final static String subjectNameUpdated = subjectName + "-UPD";
	
	protected SubjectSteps steps = new SubjectSteps();
	protected ErrorSteps errorSteps = new ErrorSteps();
	protected PdfLog pdfLog = new PdfLog();
	
	// ------------------------------------------------------------------------------------------------------------------------
}
